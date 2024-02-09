package edu.ualberta.med.biobank.controllers.patients;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;
import edu.ualberta.med.biobank.controllers.endpoints.PatientCommentsListEndpoint;
import edu.ualberta.med.biobank.domain.User;
import edu.ualberta.med.biobank.dtos.CommentDTO;
import edu.ualberta.med.biobank.matchers.CommentMatcher;
import edu.ualberta.med.biobank.repositories.UserRepository;
import edu.ualberta.med.biobank.test.ControllerTest;
import edu.ualberta.med.biobank.test.fixtures.PatientFixtureBuilder;
import edu.ualberta.med.biobank.util.LoggingUtils;
import jakarta.transaction.Transactional;
import net.datafaker.Faker;

@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PatientCommentsListTests extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(PatientListTests.class);

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser(value = "testuser")
    void get_when_has_comments_is_ok() throws Exception {
        User testuser = userRepository.getReferenceById(2); // KLUDGE: ID 2 belongs to testuser - see V1__init.sql
        var patient = new PatientFixtureBuilder().numPatientComments(1).commentUsername(testuser).build(factory);

        MvcResult result =
            this.mvc.perform(get(new PatientCommentsListEndpoint(patient.getPnumber()).url()))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<CommentDTO> dto = List.of(mapper.readValue(result.getResponse().getContentAsString(), CommentDTO[].class));
        logger.debug("result: {}", LoggingUtils.prettyPrintJson(dto));
        assertThat(dto, CommentMatcher.containsAll(patient.getComments()));
    }

    @Test
    void get_when_present_and_anonymous_is_unauthorized() throws Exception {
        var patient = new PatientFixtureBuilder().numPatientComments(1).build(factory);
        this.mvc.perform(get(new PatientCommentsListEndpoint(patient.getPnumber()).url()))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(value = "non_member_user")
    void get_when_present_and_not_member_is_forbidden() throws Exception {
        createSingleStudyUser("non_member_user");
        var patient = new PatientFixtureBuilder().numPatientComments(1).build(factory);

        this.mvc.perform(get(new PatientCommentsListEndpoint(patient.getPnumber()).url()))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*permission.*")));
    }

    @Test
    @WithMockUser(value = "testuser")
    void get_when_patient_not_exist_is_not_found() throws Exception {
        var badPnumber = new Faker().internet().username();

        this.mvc.perform(get(new PatientCommentsListEndpoint(badPnumber).url()))
            .andExpect(status().isNotFound());
    }
}
