package edu.ualberta.med.biobank.controllers.collectionEvents;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ualberta.med.biobank.controllers.endpoints.CeventCommentsListEndpoint;
import edu.ualberta.med.biobank.domain.User;
import edu.ualberta.med.biobank.dtos.CommentDTO;
import edu.ualberta.med.biobank.matchers.CommentMatcher;
import edu.ualberta.med.biobank.repositories.UserRepository;
import edu.ualberta.med.biobank.test.ControllerTest;
import edu.ualberta.med.biobank.test.fixtures.PatientFixtureBuilder;
import edu.ualberta.med.biobank.util.LoggingUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CollectionEventCommentsListTests extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(CollectionEventCommentsListTests.class);

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser(value = "testuser")
    void get_when_has_comments_is_ok() throws Exception {
        User testuser = userRepository.getReferenceById(2); // KLUDGE: ID 2 belongs to testuser - see V1__init.sql
        var patient = new PatientFixtureBuilder()
            .numCollectionEvents(1)
            .commentUsername(testuser)
            .numCollectionEventComments(1)
            .build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();

        MvcResult result =
            this.mvc.perform(
                    get(new CeventCommentsListEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url())
                )
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<CommentDTO> dto = List.of(mapper.readValue(result.getResponse().getContentAsString(), CommentDTO[].class));
        logger.debug("result: {}", LoggingUtils.prettyPrintJson(dto));
        assertThat(dto, CommentMatcher.containsAll(collectionEvent.getComments()));
    }

    @Test
    void get_when_present_and_anonymous_is_unauthorized() throws Exception {
        var patient = new PatientFixtureBuilder().numCollectionEvents(1).numCollectionEventComments(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();

        this.mvc.perform(
                get(new CeventCommentsListEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url())
            )
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(value = "non_member_user")
    void get_when_present_and_not_member_is_forbidden() throws Exception {
        createSingleStudyUser("non_member_user");
        var patient = new PatientFixtureBuilder().numCollectionEvents(1).numCollectionEventComments(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();

        this.mvc.perform(
                get(new CeventCommentsListEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url())
            )
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*permission.*")));
    }

    @Test
    @WithMockUser(value = "testuser")
    void get_when_patient_not_exist_is_not_found() throws Exception {
        var patient = new PatientFixtureBuilder().numCollectionEvents(1).numCollectionEventComments(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var badVnumber = collectionEvent.getVisitNumber() + 10;

        this.mvc.perform(get(new CeventCommentsListEndpoint(patient.getPnumber(), badVnumber).url()))
            .andExpect(status().isNotFound());
    }
}
