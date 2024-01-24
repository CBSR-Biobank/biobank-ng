package edu.ualberta.med.biobank.controllers.patients;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.stream.Stream;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.junit.jupiter.Testcontainers;
import edu.ualberta.med.biobank.controllers.endpoints.PatientAddEndpoint;
import edu.ualberta.med.biobank.domain.Study;
import edu.ualberta.med.biobank.dtos.PatientAddDTO;
import edu.ualberta.med.biobank.dtos.PatientDTO;
import edu.ualberta.med.biobank.test.ControllerTest;
import edu.ualberta.med.biobank.util.JsonUtil;
import edu.ualberta.med.biobank.util.LoggingUtils;
import jakarta.transaction.Transactional;
import net.datafaker.Faker;

@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PatientCreateTests extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(PatientCreateTests.class);

    private static Stream<Arguments> provideInvalidPatient() {
        var faker = new Faker();
        return Stream.of(
            Arguments.of(null, true, "patient number.*blank"),
            Arguments.of("", true, "patient number.*blank"),
            Arguments.of(faker.internet().username(), false, "study.*name.*blank")
        );
    }

    @Test
    @WithMockUser(value = "testuser")
    void post_succeeds() throws Exception {
        Study study = factory.createStudy();
        var dto = new PatientAddDTO(getMethodNameR(), study.getNameShort());

        MvcResult result = mvc
            .perform(
                post(new PatientAddEndpoint().url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(dto))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.pnumber", Matchers.is(dto.pnumber())))
            .andExpect(jsonPath("$.studyNameShort", Matchers.is(dto.studyNameShort())))
            .andReturn();

        PatientDTO resultDto = objectMapper().readValue(result.getResponse().getContentAsString(), PatientDTO.class);
        logger.debug("HTTP Response: {}", LoggingUtils.prettyPrintJson(resultDto));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPatient")
    @WithMockUser(value = "testuser")
    void post_fails_with_invalid_values(String pnumber, boolean hasStudy, String errMessage) throws Exception {
        var data = new JSONObject();
        data.put("pnumber", pnumber);
        if (hasStudy) {
            Study study = factory.createStudy();
            data.put("studyNameShort", study.getNameShort());
        }

        this.mvc.perform(
                post(new PatientAddEndpoint().url()).contentType(MediaType.APPLICATION_JSON).content(data.toString())
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(errMessage)))
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(value = "testuser")
    void post_fails_when_patient_already_exists() throws Exception {
        var patient = factory.createPatient();
        var dto = new PatientAddDTO(patient.getPnumber(), patient.getStudy().getNameShort());

        this.mvc.perform(
                post(new PatientAddEndpoint().url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(dto))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex("patient.*exists")))
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(value = "testuser")
    void post_fails_when_study_does_not_exist() throws Exception {
        createSingleStudyUser("non_member_user");
        var dto = new PatientAddDTO(getMethodNameR(), getMethodNameR());

        this.mvc.perform(
                post(new PatientAddEndpoint().url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(dto))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(value = "non_member_user")
    void post_fails_when_not_member() throws Exception {
        createSingleStudyUser("non_member_user");
        Study study = factory.createStudy();
        var dto = new PatientAddDTO(getMethodNameR(), study.getNameShort());

        this.mvc.perform(
                post(new PatientAddEndpoint().url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(dto))
            )
            .andExpect(status().isBadRequest());
    }
}
