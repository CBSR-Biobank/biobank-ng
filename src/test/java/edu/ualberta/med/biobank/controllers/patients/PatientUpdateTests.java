package edu.ualberta.med.biobank.controllers.patients;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.ualberta.med.biobank.controllers.endpoints.PatientAddEndpoint;
import edu.ualberta.med.biobank.controllers.endpoints.PatientUpdateEndpoint;
import edu.ualberta.med.biobank.domain.Study;
import edu.ualberta.med.biobank.dtos.PatientAddDTO;
import edu.ualberta.med.biobank.dtos.PatientDTO;
import edu.ualberta.med.biobank.dtos.PatientUpdateDTO;
import edu.ualberta.med.biobank.test.ControllerTest;
import edu.ualberta.med.biobank.test.fixtures.PatientFixtureBuilder;
import edu.ualberta.med.biobank.util.JsonUtil;
import edu.ualberta.med.biobank.util.LoggingUtils;
import jakarta.transaction.Transactional;
import java.util.stream.Stream;
import net.datafaker.Faker;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PatientUpdateTests extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(PatientUpdateTests.class);

    private static Stream<Arguments> provideInvalidValues() {
        var faker = new Faker();
        return Stream.of(
            Arguments.of(null, true, "patient number.*blank"),
            Arguments.of("", true, "patient number.*blank"),
            Arguments.of(faker.internet().username(), false, "study.*name.*blank")
        );
    }

    @Test
    @WithMockUser(value = "testuser")
    void put_succeeds() throws Exception {
        var patient = new PatientFixtureBuilder().build(factory);
        Study newStudy = factory.createStudy();
        var newData = new PatientUpdateDTO(getMethodNameR(), newStudy.getNameShort());

        MvcResult result = mvc
            .perform(
                put(new PatientUpdateEndpoint(patient.getPnumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(newData))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pnumber", Matchers.is(newData.pnumber())))
            .andExpect(jsonPath("$.studyNameShort", Matchers.is(newData.studyNameShort())))
            .andReturn();

        PatientDTO resultDto = objectMapper().readValue(result.getResponse().getContentAsString(), PatientDTO.class);
        logger.debug("HTTP Response: {}", LoggingUtils.prettyPrintJson(resultDto));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidValues")
    @WithMockUser(value = "testuser")
    void put_fails_with_invalid_values(String pnumber, boolean hasStudy, String errMessage) throws Exception {
        var patient = new PatientFixtureBuilder().build(factory);
        var data = new JSONObject();
        data.put("pnumber", pnumber);
        if (hasStudy) {
            Study study = factory.createStudy();
            data.put("studyNameShort", study.getNameShort());
        }

        this.mvc.perform(
                put(new PatientUpdateEndpoint(patient.getPnumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(data.toString())
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(errMessage)));
    }

    @Test
    @WithMockUser(value = "testuser")
    void put_with_invalid_study_name_is_not_found() throws Exception {
        var patient = new PatientFixtureBuilder().build(factory);
        var badStudyNameShort = new Faker().internet().username();
        var newData = new PatientUpdateDTO(getMethodNameR(), badStudyNameShort);

        mvc
            .perform(
                put(new PatientUpdateEndpoint(patient.getPnumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(newData))
            )
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*study.*")));
    }

    @Test
    @WithMockUser(value = "testuser")
    void put_with_existing_pnumber_is_bad_request() throws Exception {
        var patient = new PatientFixtureBuilder().build(factory);
        var patient2 = new PatientFixtureBuilder().build(factory);
        var newData = new PatientUpdateDTO(patient2.getPnumber(), patient.getStudy().getNameShort());

        mvc
            .perform(
                put(new PatientUpdateEndpoint(patient.getPnumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(newData))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*patient.*exists.*")));
    }

    @Test
    @WithMockUser(value = "non_member_user")
    void put_with_non_member_on_new_study_is_forbidden() throws Exception {
        createSingleStudyUser("non_member_user");
        var newStudy = factory.createStudy();

        var patientStudy = factory.createStudy();
        var patient = new PatientFixtureBuilder().build(factory);
        var newData = new PatientUpdateDTO(patient.getPnumber(), newStudy.getNameShort());

        mvc
            .perform(
                put(new PatientUpdateEndpoint(patient.getPnumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(newData))
            )
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*permission.*study.*")));
    }

    @Test
    @WithMockUser(value = "non_member_user")
    void put_with_non_member_on_patient_study_is_forbidden() throws Exception {
        var patientStudy = factory.createStudy();
        patientStudy.setNameShort("patient_study");
        em.persist(patientStudy);

        var patient = new PatientFixtureBuilder().build(factory);

        var newStudy = factory.createStudy();
        newStudy.setNameShort("new_study");
        em.persist(newStudy);
        createSingleStudyUser("non_member_user", true); // all permissions required on "new_study"

        var newData = new PatientUpdateDTO(patient.getPnumber(), newStudy.getNameShort());

        mvc
            .perform(
                put(new PatientUpdateEndpoint(patient.getPnumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(newData))
            )
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*permission.*study.*")))
            .andExpect(jsonPath("$.message", Matchers.containsString(patientStudy.getNameShort())));
    }
}
