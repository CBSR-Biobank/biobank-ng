package edu.ualberta.med.biobank.controllers.collectionEvents;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import edu.ualberta.med.biobank.controllers.endpoints.CollectionEventUpdateEndpoint;
import edu.ualberta.med.biobank.domain.Status;
import edu.ualberta.med.biobank.domain.Study;
import edu.ualberta.med.biobank.dtos.AnnotationDTO;
import edu.ualberta.med.biobank.dtos.AnnotationTypeDTO;
import edu.ualberta.med.biobank.dtos.CollectionEventDTO;
import edu.ualberta.med.biobank.dtos.CollectionEventUpdateDTO;
import edu.ualberta.med.biobank.test.ControllerTest;
import edu.ualberta.med.biobank.test.fixtures.PatientFixtureBuilder;
import edu.ualberta.med.biobank.test.fixtures.StudyFixtureBuilder;
import edu.ualberta.med.biobank.util.DateUtil;
import edu.ualberta.med.biobank.util.JsonUtil;
import edu.ualberta.med.biobank.util.LoggingUtils;
import jakarta.transaction.Transactional;
import net.datafaker.Faker;

@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CollectionEventAnnotationUpdateTests extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(CollectionEventAnnotationUpdateTests.class);

    private static Stream<Arguments> provideValidValues() {
        var faker = new Faker();
        String[] selectValues = new String[] { "one", "two"};

        return Stream.of(
            Arguments.of("text", "test_label", new String[0], faker.lorem().sentence()),
            Arguments.of("number", "test_label", new String[0], Long.toString(faker.number().randomNumber())),
            Arguments.of("date_time", "test_label", new String[0], faker.date().past(1000, TimeUnit.DAYS, DateUtil.DATE_TIME_PATTERN)),
            Arguments.of("select_single", "test_label", selectValues, selectValues[1]),
            Arguments.of("select_multiple", "test_label", selectValues, String.format("%s;%s", selectValues[0], selectValues[1]))
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidValues")
    @WithMockUser(value = "testuser")
    void put_succeeds(String type, String label, String[] validValues, String value) throws Exception {
        AnnotationTypeDTO annotationType = new AnnotationTypeDTO(type, label, false, validValues);

        new StudyFixtureBuilder().setEntityManger(em).addAttribute(annotationType).build(factory);

        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var newData = new CollectionEventUpdateDTO(
            collectionEvent.getVisitNumber(),
            collectionEvent.getActivityStatus().getName(),
            List.of(new AnnotationDTO(annotationType.type(), annotationType.label(), value))
        );

        MvcResult result = mvc
            .perform(
                put(new CollectionEventUpdateEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(newData))
            )
            .andExpect(status().isOk())
            .andReturn();

        CollectionEventDTO resultDto = objectMapper()
            .readValue(result.getResponse().getContentAsString(), CollectionEventDTO.class);
        logger.debug("HTTP Response: {}", LoggingUtils.prettyPrintJson(resultDto));
    }

    @Test
    @WithMockUser(value = "testuser")
    void put_with_invalid_label_fails_with_bad_request() throws Exception {
        Faker faker = new Faker();
        AnnotationTypeDTO annotationType = new AnnotationTypeDTO("test_text", "test_label", false, new String[0]);

        new StudyFixtureBuilder().setEntityManger(em).addAttribute(annotationType).build(factory);

        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var newData = new CollectionEventUpdateDTO(
            collectionEvent.getVisitNumber(),
            collectionEvent.getActivityStatus().getName(),
            List.of(new AnnotationDTO(annotationType.type(), faker.lorem().word(), faker.lorem().sentence()))
        );

        this.mvc.perform(
                put(new CollectionEventUpdateEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(newData))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*annotation not found.*")));
    }

    @Test
    @WithMockUser(value = "testuser")
    void put_with_non_active_fails_with_bad_request() throws Exception {
        Faker faker = new Faker();
        AnnotationTypeDTO annotationType = new AnnotationTypeDTO("text", "test_label", false, new String[0]);

        Study study = new StudyFixtureBuilder().setEntityManger(em).addAttribute(annotationType).build(factory);

        study.getStudyEventAttrs().stream().findFirst().get().setActivityStatus(Status.CLOSED);
        em.persist(study);

        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var newData = new CollectionEventUpdateDTO(
            collectionEvent.getVisitNumber(),
            collectionEvent.getActivityStatus().getName(),
            List.of(new AnnotationDTO(annotationType.type(), annotationType.label(), faker.lorem().sentence()))
        );

        this.mvc.perform(
                put(new CollectionEventUpdateEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(newData))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*not active.*")));
    }

    @Test
    @WithMockUser(value = "testuser")
    void put_with_select_and_invalid_value_fails_with_bad_request() throws Exception {
        Faker faker = new Faker();
        String[] validValues = new String[] { "one", "two" };
        AnnotationTypeDTO annotationType = new AnnotationTypeDTO("select_single", "test_label", false, validValues);

        new StudyFixtureBuilder().setEntityManger(em).addAttribute(annotationType).build(factory);

        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var newData = new CollectionEventUpdateDTO(
            collectionEvent.getVisitNumber(),
            collectionEvent.getActivityStatus().getName(),
            List.of(new AnnotationDTO(annotationType.type(), annotationType.label(), faker.lorem().sentence()))
        );

        this.mvc.perform(
                put(new CollectionEventUpdateEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(newData))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*invalid annotation value.*")));
    }

    @Test
    @WithMockUser(value = "testuser")
    void put_with_multiple_select_and_invalid_value_fails_with_bad_request() throws Exception {
        Faker faker = new Faker();
        String[] validValues = new String[] { "one", "two" };
        AnnotationTypeDTO annotationType = new AnnotationTypeDTO(
            "select_multiple",
            "test_label",
            false,
            validValues
        );

        new StudyFixtureBuilder().setEntityManger(em).addAttribute(annotationType).build(factory);

        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var invalidValue = String.format("%s;%s", validValues[0], faker.lorem().word());
        var newData = new CollectionEventUpdateDTO(
            collectionEvent.getVisitNumber(),
            collectionEvent.getActivityStatus().getName(),
            List.of(new AnnotationDTO(annotationType.type(), annotationType.label(), invalidValue))
        );

        this.mvc.perform(
                put(new CollectionEventUpdateEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(newData))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*invalid annotation value.*")));
    }

    @Test
    @WithMockUser(value = "testuser")
    void put_with_numeric_and_invalid_value_fails_with_bad_request() throws Exception {
        Faker faker = new Faker();
        AnnotationTypeDTO annotationType = new AnnotationTypeDTO(
            "number",
            "test_label",
            false,
            new String[0]
        );

        new StudyFixtureBuilder().setEntityManger(em).addAttribute(annotationType).build(factory);

        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var newData = new CollectionEventUpdateDTO(
            collectionEvent.getVisitNumber(),
            collectionEvent.getActivityStatus().getName(),
            List.of(new AnnotationDTO(annotationType.type(), annotationType.label(), faker.lorem().word()))
        );

        this.mvc.perform(
                put(new CollectionEventUpdateEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(newData))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*value is not numeric.*")));
    }

    @Test
    @WithMockUser(value = "testuser")
    void put_with_datetime_and_invalid_value_fails_with_bad_request() throws Exception {
        Faker faker = new Faker();
        AnnotationTypeDTO annotationType = new AnnotationTypeDTO(
            "date_time",
            "test_label",
            false,
            new String[0]
        );

        new StudyFixtureBuilder().setEntityManger(em).addAttribute(annotationType).build(factory);

        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var newData = new CollectionEventUpdateDTO(
            collectionEvent.getVisitNumber(),
            collectionEvent.getActivityStatus().getName(),
            List.of(new AnnotationDTO(annotationType.type(), annotationType.label(), faker.lorem().word()))
        );

        this.mvc.perform(
                put(new CollectionEventUpdateEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(newData))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*value is not date time.*")));
    }

    @Test
    @WithMockUser(value = "testuser")
    void put_with_required_and_empty_value_fails_with_bad_request() throws Exception {
        Faker faker = new Faker();
        AnnotationTypeDTO annotationType = new AnnotationTypeDTO(
            "text",
            "test_label",
            true,
            new String[0]
        );

        new StudyFixtureBuilder().setEntityManger(em).addAttribute(annotationType).build(factory);

        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var newData = new CollectionEventUpdateDTO(
            collectionEvent.getVisitNumber(),
            collectionEvent.getActivityStatus().getName(),
            List.of(new AnnotationDTO(annotationType.type(), annotationType.label(), ""))
        );

        this.mvc.perform(
                put(new CollectionEventUpdateEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(newData))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*value is required.*")));
    }
}
