package edu.ualberta.med.biobank.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import edu.ualberta.med.biobank.controllers.endpoints.PatientCreateEndpoint;
import edu.ualberta.med.biobank.controllers.endpoints.PatientNumberEndpoint;
import edu.ualberta.med.biobank.domain.Study;
import edu.ualberta.med.biobank.dtos.PatientCreateDTO;
import edu.ualberta.med.biobank.dtos.PatientDTO;
import edu.ualberta.med.biobank.matchers.PatientMatcher;
import edu.ualberta.med.biobank.test.ControllerTest;
import edu.ualberta.med.biobank.test.TestFixtures;
import edu.ualberta.med.biobank.util.DateUtil;
import edu.ualberta.med.biobank.util.JsonUtil;
import edu.ualberta.med.biobank.util.LoggingUtils;
import jakarta.transaction.Transactional;
import net.datafaker.Faker;

@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PatientControllerTest extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(PatientControllerTest.class);

    @Test
    @WithMockUser
    void getWhenEmptyTableIs404() throws Exception {
        this.mvc.perform(get(new PatientNumberEndpoint(factory.getFaker().lorem().word()).url()))
            .andExpect(status().isNotFound());
    }

    @Test
    void getWhenPresentAndUnauthorized() throws Exception {
        var patient = factory.createPatient();
        this.mvc.perform(get(new PatientNumberEndpoint(patient.getPnumber()).url()))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(value = "testuser")
    void getWhenPresentIsOk() throws Exception {
        var patient = new TestFixtures.PatientFixtureBuilder().numCollectionEvents(1).numSpecimens(1).build(factory);

        MvcResult result =
            this.mvc.perform(get(new PatientNumberEndpoint(patient.getPnumber()).url()))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        PatientDTO dto = mapper.readValue(result.getResponse().getContentAsString(), PatientDTO.class);
        logger.debug("result: {}", LoggingUtils.prettyPrintJson(dto));
        assertThat(dto, PatientMatcher.matches(patient));
    }

    @Test
    @WithMockUser(value = "non_member_user")
    void getWhenPresentAndNotMemberIsBadRequest() throws Exception {
        createSingleStudyUser("non_member_user");

        var patient = new TestFixtures.PatientFixtureBuilder().build(factory);

        this.mvc.perform(get(new PatientNumberEndpoint(patient.getPnumber()).url())).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(value = "testuser")
    void getWhenNotExistIsNotFound() throws Exception {
        var badname = new Faker().lorem().fixedString(10);

        this.mvc.perform(get(new PatientNumberEndpoint(badname).url())).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(value = "testuser")
    void postSucceeds() throws Exception {
        Study study = factory.createStudy();
        var dto = newPatient(study.getNameShort());

        MvcResult result = mvc
            .perform(
                post(new PatientCreateEndpoint().url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(dto))
            )
            .andExpect(status().isCreated())
            .andDo(MockMvcResultHandlers.print())
            .andReturn();

        PatientDTO resultDto = objectMapper().readValue(result.getResponse().getContentAsString(), PatientDTO.class);
        logger.debug("HTTP Response: {}", LoggingUtils.prettyPrintJson(resultDto));
        //assertThat(resultDto, PatientMatcher.matches(patient));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPatient")
    @WithMockUser(value = "testuser")
    void post_fails_with_empty_pnumber(String pnumber, String createdAt, boolean hasStudy) throws Exception {
        Study study = factory.createStudy();
        var data = new JSONObject();
        data.put("pnumber", pnumber);
        data.put("createdAt", createdAt);
        if (hasStudy) {
            data.put("studyNameShort", study.getNameShort());
        }

        createSingleStudyUser("non_member_user");

        this.mvc.perform(
                post(new PatientCreateEndpoint().url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(data.toString())
            )
            .andExpect(status().isBadRequest())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(value = "testuser")
    void postFailsWithBadStudy() throws Exception {
        createSingleStudyUser("non_member_user");
        var dto = newPatient(getMethodNameR());

        this.mvc.perform(
                post(new PatientCreateEndpoint().url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(dto))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(value = "non_member_user")
    void postFailsWhenNotMember() throws Exception {
        createSingleStudyUser("non_member_user");
        Study study = factory.createStudy();
        var dto = newPatient(study.getNameShort());

        this.mvc.perform(
                post(new PatientCreateEndpoint().url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(dto))
            )
            .andExpect(status().isBadRequest());
    }

    private PatientCreateDTO newPatient(String studyNameShort) {
        var faker = new Faker();
        var createdAt = new Date(faker.date().past(1, TimeUnit.DAYS).getTime());
        return new PatientCreateDTO(getMethodNameR(), createdAt, studyNameShort);
    }

    private static Stream<Arguments> provideInvalidPatient() {
        var faker = new Faker();
        var createdAt = DateUtil.datetimeToString(faker.date().past(1, TimeUnit.DAYS));
        return Stream.of(
            Arguments.of(null, createdAt, true),
            Arguments.of("", createdAt, true),
            Arguments.of(faker.internet().username(), null, true),
            Arguments.of(faker.internet().username(), "abc", true),
            Arguments.of(faker.internet().username(), createdAt, false)
        );
    }
}
