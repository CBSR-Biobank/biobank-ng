package edu.ualberta.med.biobank.controllers.collectionEvents;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.ualberta.med.biobank.controllers.endpoints.CollectionEventEndpoint;
import edu.ualberta.med.biobank.domain.Status;
import edu.ualberta.med.biobank.dtos.CollectionEventAddDTO;
import edu.ualberta.med.biobank.test.ControllerTest;
import edu.ualberta.med.biobank.test.fixtures.PatientFixtureBuilder;
import edu.ualberta.med.biobank.util.JsonUtil;
import jakarta.transaction.Transactional;
import net.datafaker.Faker;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CollectionEventCreateTests extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(CollectionEventCreateTests.class);

    @Test
    @WithMockUser("testuser")
    void post_succeeds() throws Exception {
        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var data = new CollectionEventAddDTO(patient.getCollectionEvents().size() + 1);

        this.mvc.perform(
                post(new CollectionEventEndpoint(patient.getPnumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(data))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.vnumber", Matchers.is(data.vnumber())))
            .andExpect(jsonPath("$.patientId", Matchers.is(patient.getId())))
            .andExpect(jsonPath("$.pnumber", Matchers.is(patient.getPnumber())))
            .andExpect(jsonPath("$.studyId", Matchers.is(patient.getStudy().getId())))
            .andExpect(jsonPath("$.studyNameShort", Matchers.is(patient.getStudy().getNameShort())))
            .andExpect(jsonPath("$.status", Matchers.is(Status.ACTIVE.getName())))
            .andExpect(jsonPath("$.annotations", Matchers.hasSize(0)))
            .andExpect(jsonPath("$.sourceSpecimens", Matchers.hasSize(0)))
            //.andDo(MockMvcResultHandlers.print())
            .andReturn();
    }

    @Test
    @WithMockUser("testuser")
    void post_when_vnumber_exists_is_bad_request() throws Exception {
        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var data = new CollectionEventAddDTO(patient.getCollectionEvents().stream().findFirst().get().getVisitNumber());

        this.mvc.perform(
                post(new CollectionEventEndpoint(patient.getPnumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(data))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*visit.*exists.*")));
    }

    @Test
    @WithMockUser("testuser")
    void post_with_bad_pnumber_is_not_found() throws Exception {
        var badPnumber = new Faker().internet().username();
        var data = new CollectionEventAddDTO(1);

        this.mvc.perform(
                post(new CollectionEventEndpoint(badPnumber).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(data))
            )
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*patient.*")));
    }

    @Test
    @WithMockUser("testuser")
    void post_with_bad_vnumber_is_bad_request() throws Exception {
        var patient = new PatientFixtureBuilder().build(factory);
        var data = new CollectionEventAddDTO(0);

        this.mvc.perform(
                post(new CollectionEventEndpoint(patient.getPnumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(data))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*greater than.*")));
    }

    @Test
    @WithMockUser("non_member_user")
    void post_with_non_member_user_is_forbidden() throws Exception {
        createSingleStudyUser("non_member_user");
        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var data = new CollectionEventAddDTO(
            patient.getCollectionEvents().stream().findFirst().get().getVisitNumber() + 1
        );

        this.mvc.perform(
                post(new CollectionEventEndpoint(patient.getPnumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(data))
            )
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*permission.*collection.*")));
    }
}
