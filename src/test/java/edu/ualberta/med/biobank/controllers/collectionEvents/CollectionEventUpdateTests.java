package edu.ualberta.med.biobank.controllers.collectionEvents;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import java.util.stream.Stream;
import org.hamcrest.Matchers;
import org.json.JSONObject;
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
import edu.ualberta.med.biobank.dtos.CollectionEventDTO;
import edu.ualberta.med.biobank.dtos.CollectionEventUpdateDTO;
import edu.ualberta.med.biobank.test.ControllerTest;
import edu.ualberta.med.biobank.test.fixtures.PatientFixtureBuilder;
import edu.ualberta.med.biobank.util.JsonUtil;
import edu.ualberta.med.biobank.util.LoggingUtils;
import jakarta.transaction.Transactional;

@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CollectionEventUpdateTests extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(CollectionEventUpdateTests.class);

    private static List<Status> provideStatusValues() {
        return Status.valuesList();
    }

    private static Stream<Arguments> provideInvalidValues() {
        return Stream.of(
            Arguments.of(null, Status.ACTIVE.getName(), "visit number.*empty"),
            Arguments.of(0, Status.ACTIVE.getName(), "visit number.*greater than.*"),
            Arguments.of(1, null, "status.*blank.*"),
            Arguments.of(1, "abcdef", "invalid status.*")
        );
    }

    @Test
    @WithMockUser(value = "testuser")
    void put_succeeds() throws Exception {
        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var newData = new CollectionEventUpdateDTO(
            collectionEvent.getVisitNumber(),
            collectionEvent.getActivityStatus().getName(),
            List.of()
        );

        MvcResult result = mvc
            .perform(
                put(new CollectionEventUpdateEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(newData))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pnumber", Matchers.is(patient.getPnumber())))
            .andExpect(jsonPath("$.vnumber", Matchers.is(newData.vnumber())))
            .andReturn();

        CollectionEventDTO resultDto = objectMapper().readValue(result.getResponse().getContentAsString(), CollectionEventDTO.class);
        logger.debug("HTTP Response: {}", LoggingUtils.prettyPrintJson(resultDto));
    }

    @ParameterizedTest
    @MethodSource("provideStatusValues")
    @WithMockUser(value = "testuser")
    void put_succeeds_with_status(Status status) throws Exception {
        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var newData = new CollectionEventUpdateDTO(
            collectionEvent.getVisitNumber(),
            status.getName(),
            List.of()
        );

        MvcResult result = mvc
            .perform(
                put(new CollectionEventUpdateEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(newData))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pnumber", Matchers.is(patient.getPnumber())))
            .andExpect(jsonPath("$.vnumber", Matchers.is(newData.vnumber())))
            .andExpect(jsonPath("$.status", Matchers.is(status.getName())))
            .andReturn();

        CollectionEventDTO resultDto = objectMapper().readValue(result.getResponse().getContentAsString(), CollectionEventDTO.class);
        // FIXME: check for match
        logger.debug("HTTP Response: {}", LoggingUtils.prettyPrintJson(resultDto));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidValues")
    @WithMockUser(value = "testuser")
    void put_fails_with_invalid_values(Integer vnumber, String status, String errMessage) throws Exception {
        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();

        JSONObject data = new JSONObject();
        data.put("vnumber", vnumber);
        data.put("status", status);

        this.mvc.perform(
                put(new CollectionEventUpdateEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(data.toString())
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(errMessage)));
    }

    @Test
    @WithMockUser(value = "testuser")
    void put_fails_with_existing_vnumber() throws Exception {
        var patient = new PatientFixtureBuilder().numCollectionEvents(2).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var newData = new CollectionEventUpdateDTO(
            collectionEvent.getVisitNumber() + 1,
            collectionEvent.getActivityStatus().getName(),
            List.of()
        );

        this.mvc.perform(
                put(new CollectionEventUpdateEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(newData))
            )
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex("visit number exists.*")));
    }

    @Test
    @WithMockUser(value = "non_member_user")
    void put_with_non_member_on_new_study_is_forbidden() throws Exception {
        createSingleStudyUser("non_member_user");

        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var newData = new CollectionEventUpdateDTO(
            collectionEvent.getVisitNumber(),
            collectionEvent.getActivityStatus().getName(),
            List.of()
        );

        mvc
            .perform(
                put(new CollectionEventUpdateEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(newData))
            )
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*permission.*study.*")));
    }
}
