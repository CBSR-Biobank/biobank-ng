package edu.ualberta.med.biobank.controllers.specimens;

import edu.ualberta.med.biobank.util.DateUtil;
import edu.ualberta.med.biobank.util.JsonUtil;
import edu.ualberta.med.biobank.util.LoggingUtils;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.junit.jupiter.Testcontainers;
import edu.ualberta.med.biobank.controllers.endpoints.SourceSpecimenAddEdnpoint;
import edu.ualberta.med.biobank.controllers.endpoints.VisitNumberEndpoint;
import edu.ualberta.med.biobank.domain.CollectionEvent;
import edu.ualberta.med.biobank.domain.Patient;
import edu.ualberta.med.biobank.domain.Specimen;
import edu.ualberta.med.biobank.domain.Status;
import edu.ualberta.med.biobank.dtos.SourceSpecimenAddDTO;
import edu.ualberta.med.biobank.dtos.SourceSpecimenDTO;
import edu.ualberta.med.biobank.matchers.SourceSpecimenMatcher;
import edu.ualberta.med.biobank.test.ControllerTest;
import edu.ualberta.med.biobank.test.fixtures.PatientFixtureBuilder;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.params.provider.Arguments;
import net.datafaker.Faker;
import org.exparity.hamcrest.date.DateMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SourceSpecimenCreateTest extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(SourceSpecimenCreateTest.class);

    @ParameterizedTest
    @MethodSource("provideInvalidValues")
    @WithMockUser(value = "testuser")
    void put_fails_with_invalid_values(String propertyName, Object invalidValue, String errMessage) throws Exception {
        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var dto = createSpecimen(patient, collectionEvent);
        JSONObject data = createInvalidJson(dto, propertyName, invalidValue);

        mvc.perform(
                post(new SourceSpecimenAddEdnpoint().url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(data.toString())
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(errMessage)));
    }

    @Test
    @WithMockUser(value = "testuser")
    void put_succeeds_when_valid() throws Exception {
        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var dto = createSpecimen(patient, collectionEvent);

        MvcResult result = mvc.perform(
                post(new SourceSpecimenAddEdnpoint().url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(dto)))
            .andExpect(status().isCreated())
            //.andDo(MockMvcResultHandlers.print())
            .andReturn();

        SourceSpecimenDTO resultDto = objectMapper().readValue(result.getResponse().getContentAsString(), SourceSpecimenDTO.class);
        MatcherAssert.assertThat(resultDto.id(), Matchers.greaterThan(0));
        MatcherAssert.assertThat(resultDto.inventoryId(), Matchers.notNullValue());
        MatcherAssert.assertThat(resultDto.specimenTypeId(), Matchers.greaterThan(0));
        MatcherAssert.assertThat(resultDto.specimenTypeNameShort(), Matchers.equalTo(dto.specimenTypeNameShort()));
        MatcherAssert.assertThat(resultDto.timeDrawn(), DateMatchers.within(1, ChronoUnit.SECONDS, dto.timeDrawn()));
        MatcherAssert.assertThat(resultDto.quantity(), Matchers.closeTo(dto.quantity(), new BigDecimal(0.1)));
        MatcherAssert.assertThat(resultDto.status(), Matchers.equalTo(dto.status()));
        MatcherAssert.assertThat(resultDto.originCenterId(), Matchers.greaterThan(0));
        MatcherAssert.assertThat(resultDto.originCenterNameShort(), Matchers.equalTo(dto.originCenterNameShort()));
        MatcherAssert.assertThat(resultDto.currentCenterId(), Matchers.greaterThan(0));
        MatcherAssert.assertThat(resultDto.currentCenterNameShort(), Matchers.equalTo(dto.originCenterNameShort()));
        MatcherAssert.assertThat(resultDto.hasComments(), Matchers.equalTo(false));
        MatcherAssert.assertThat(resultDto.position(), Matchers.nullValue());
        MatcherAssert.assertThat(resultDto.processingEventId(), Matchers.nullValue());
        MatcherAssert.assertThat(resultDto.worksheet(), Matchers.nullValue());
    }

    @Test
    @WithMockUser(value = "testuser")
    void post_when_specimen_exists_is_bad_request() throws Exception {
        factory.createClinic();
        createSingleStudyUser("non_member_user");

        var patient = new PatientFixtureBuilder().numCollectionEvents(1).numSpecimens(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var specimen = collectionEvent.getAllSpecimens().iterator().next();
        var dto = new SourceSpecimenAddDTO(
            specimen.getInventoryId(),
            specimen.getSpecimenType().getNameShort(),
            specimen.getCreatedAt(),
            specimen.getQuantity(),
            specimen.getActivityStatus().toString(),
            patient.getPnumber(),
            collectionEvent.getVisitNumber(),
            specimen.getOriginInfo().getCenter().getNameShort()
        );

        mvc.perform(
                post(new SourceSpecimenAddEdnpoint().url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*specimen.*exists.*")));
    }

    @Test
    @WithMockUser("non_member_user")
    void post_with_non_member_user_is_forbidden() throws Exception {
        factory.createClinic();
        createSingleStudyUser("non_member_user");

        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var dto = createSpecimen(patient, collectionEvent);

        mvc.perform(
                post(new SourceSpecimenAddEdnpoint().url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(dto)))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*permission.*collection.*")));
            //.andDo(MockMvcResultHandlers.print());
    }

    private SourceSpecimenAddDTO createSpecimen(Patient patient, CollectionEvent collectionEvent) {
        return new SourceSpecimenAddDTO(
            UUID.randomUUID().toString(),
            factory.getDefaultSourceSpecimenType().getNameShort(),
            new Date(),
            new BigDecimal(0),
            Status.ACTIVE.toString(),
            patient.getPnumber(),
            collectionEvent.getVisitNumber(),
            factory.getDefaultClinic().getNameShort()
        );
    }

    private static Stream<Arguments> provideInvalidValues() throws JSONException {
        Faker faker = new Faker();
        return Stream.of(
            Arguments.of("inventoryId", null, "inventory.*cannot be blank"),
            Arguments.of("inventoryId", "", "inventory.*cannot be blank"),
            Arguments.of("specimenTypeNameShort", null, "specimen type.*cannot be blank"),
            Arguments.of("specimenTypeNameShort", "", "specimen type.*cannot be blank"),
            Arguments.of("specimenTypeNameShort", faker.lorem().word(), ".*specimen type.*"),
            Arguments.of("timeDrawn", null, "time drawn.*cannot be blank"),
            Arguments.of("timeDrawn", "", "time drawn.*cannot be blank"),
            Arguments.of("quantity", -1.0, "quantity.*greater than 0"),
            Arguments.of("status", null, "status.*cannot be blank"),
            Arguments.of("status", "", "status.*cannot be blank"),
            Arguments.of("status", faker.lorem().word(), "status.*invalid"),
            Arguments.of("pnumber", null, "patient number.*cannot be blank"),
            Arguments.of("pnumber", "", "patient number.*cannot be blank"),
            Arguments.of("pnumber", faker.lorem().word(), ".*pnumber.*"),
            Arguments.of("vnumber", null, "visit number cannot be null"),
            Arguments.of("vnumber", -1, "visit number.*greater than 0"),
            Arguments.of("vnumber", 99999, ".*vnumber.*"),
            Arguments.of("originCenterNameShort", null, "origin center name.*cannot be blank"),
            Arguments.of("originCenterNameShort", "", "origin center name.*cannot be blank"),
            Arguments.of("originCenterNameShort", faker.lorem().word(), "clinic.*name.*not found")
        );
    }

    private JSONObject toJson(SourceSpecimenAddDTO dto) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("inventoryId", dto.inventoryId());
        data.put("specimenTypeNameShort", dto.specimenTypeNameShort());
        data.put("timeDrawn", DateUtil.datetimeToString(dto.timeDrawn()));
        data.put("quantity", dto.quantity());
        data.put("status", dto.status());
        data.put("pnumber", dto.pnumber());
        data.put("vnumber", dto.vnumber());
        data.put("originCenterNameShort", dto.originCenterNameShort());
        return data;
    }

    private JSONObject createInvalidJson(SourceSpecimenAddDTO dto, String invalidProperty, Object invalidValue) throws JSONException {
        JSONObject data = toJson(dto);
        data.put(invalidProperty, invalidValue);
        return data;
    }
}
