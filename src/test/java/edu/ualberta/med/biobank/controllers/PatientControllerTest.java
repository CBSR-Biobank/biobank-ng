package edu.ualberta.med.biobank.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ualberta.med.biobank.controllers.endpoints.PatientCreateEndpoint;
import edu.ualberta.med.biobank.controllers.endpoints.PatientNumberEndpoint;
import edu.ualberta.med.biobank.domain.Study;
import edu.ualberta.med.biobank.dtos.PatientCreateDTO;
import edu.ualberta.med.biobank.dtos.PatientDTO;
import edu.ualberta.med.biobank.matchers.PatientMatcher;
import edu.ualberta.med.biobank.test.ControllerTest;
import edu.ualberta.med.biobank.test.TestFixtures;
import edu.ualberta.med.biobank.util.JsonUtil;
import edu.ualberta.med.biobank.util.LoggingUtils;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

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
        logger.info("result: {}", LoggingUtils.prettyPrintJson(dto));
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

        this.mvc.perform(
                post(new PatientCreateEndpoint().url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(dto))
            )
            .andExpect(status().isCreated());
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
}
