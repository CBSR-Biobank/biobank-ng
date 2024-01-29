package edu.ualberta.med.biobank.controllers.patients;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;
import edu.ualberta.med.biobank.controllers.endpoints.PatientNumberEndpoint;
import edu.ualberta.med.biobank.dtos.PatientDTO;
import edu.ualberta.med.biobank.matchers.PatientMatcher;
import edu.ualberta.med.biobank.test.ControllerTest;
import edu.ualberta.med.biobank.test.fixtures.PatientFixtureBuilder;
import edu.ualberta.med.biobank.util.LoggingUtils;
import jakarta.transaction.Transactional;
import net.datafaker.Faker;

@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PatientListTests extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(PatientListTests.class);

    @Test
    @WithMockUser
    void get_when_empty_table_is_not_found() throws Exception {
        this.mvc.perform(get(new PatientNumberEndpoint(factory.getFaker().lorem().word()).url()))
            .andExpect(status().isNotFound());
    }

    @Test
    void get_when_present_and_anonymous_is_unauthorized() throws Exception {
        var patient = factory.createPatient();
        this.mvc.perform(get(new PatientNumberEndpoint(patient.getPnumber()).url()))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(value = "testuser")
    void get_when_present_is_ok() throws Exception {
        var patient = new PatientFixtureBuilder().numCollectionEvents(1).numSpecimens(1).build(factory);

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
    void get_when_present_and_not_member_is_forbidden() throws Exception {
        createSingleStudyUser("non_member_user");

        var patient = new PatientFixtureBuilder().build(factory);

        this.mvc.perform(get(new PatientNumberEndpoint(patient.getPnumber()).url()))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*permission.*")));
    }

    @Test
    @WithMockUser(value = "testuser")
    void get_when_not_exist_is_not_found() throws Exception {
        var badname = new Faker().internet().username();

        this.mvc.perform(get(new PatientNumberEndpoint(badname).url())).andExpect(status().isNotFound());
    }
}
