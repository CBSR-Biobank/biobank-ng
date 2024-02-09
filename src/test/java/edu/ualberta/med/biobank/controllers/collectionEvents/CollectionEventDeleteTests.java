package edu.ualberta.med.biobank.controllers.collectionEvents;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;
import edu.ualberta.med.biobank.controllers.endpoints.VisitNumberEndpoint;
import edu.ualberta.med.biobank.dtos.CollectionEventDTO;
import edu.ualberta.med.biobank.matchers.CollectionEventMatcher;
import edu.ualberta.med.biobank.test.ControllerTest;
import edu.ualberta.med.biobank.test.fixtures.PatientFixtureBuilder;
import edu.ualberta.med.biobank.util.LoggingUtils;
import jakarta.transaction.Transactional;

@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CollectionEventDeleteTests extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(CollectionEventDeleteTests.class);

    @Test
    @WithMockUser
    void delete_not_exists_not_found() throws Exception {
        var patient = factory.createPatient();

        this.mvc.perform(delete(new VisitNumberEndpoint(patient.getPnumber(), 9999).url()))
            .andExpect(status().isNotFound());
    }

    @Test
    void delet_when_exists_and_unauthorized() throws Exception {
        var patient = new PatientFixtureBuilder()
            .numCollectionEvents(1)
            .build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();

        this.mvc.perform(delete(new VisitNumberEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url()))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(value = "testuser")
    void delete_when_present_is_no_content() throws Exception {
        var patient = new PatientFixtureBuilder()
            .numCollectionEvents(1)
            .build(factory);

        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();

        this.mvc.perform(delete(new VisitNumberEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url()))
            .andExpect(status().isNoContent())
            .andReturn();
    }

    @Test
    @WithMockUser(value = "non_member_user")
    void delete_when_present_and_notmember_is_forbidden() throws Exception {
        createSingleStudyUser("non_member_user");

        var patient = new PatientFixtureBuilder()
            .numCollectionEvents(1)
            .build(factory);

        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();

        this.mvc.perform(delete(new VisitNumberEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "testuser")
    void delete_when_has_specimens_is_forbidden() throws Exception {
        var patient = new PatientFixtureBuilder()
            .numCollectionEvents(1)
            .numSpecimens(1)
            .build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();

         this.mvc.perform(delete(new VisitNumberEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url()))
            .andExpect(status().isForbidden());
    }
}
