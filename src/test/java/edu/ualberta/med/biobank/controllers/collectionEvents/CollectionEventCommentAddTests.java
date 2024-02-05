package edu.ualberta.med.biobank.controllers.collectionEvents;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import com.jayway.jsonpath.JsonPath;
import org.exparity.hamcrest.date.InstantMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.junit.jupiter.Testcontainers;
import edu.ualberta.med.biobank.controllers.endpoints.CeventCommentAddEndpoint;
import edu.ualberta.med.biobank.dtos.CommentAddDTO;
import edu.ualberta.med.biobank.test.ControllerTest;
import edu.ualberta.med.biobank.test.fixtures.PatientFixtureBuilder;
import edu.ualberta.med.biobank.util.JsonUtil;
import jakarta.transaction.Transactional;
import net.datafaker.Faker;

@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CollectionEventCommentAddTests extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(CollectionEventCommentAddTests.class);

    @Test
    @WithMockUser(value = "testuser")
    void post_succeeds() throws Exception {
        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var dto = new CommentAddDTO(new Faker().lorem().paragraph(2));

        MvcResult result = mvc
            .perform(
                post(new CeventCommentAddEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(dto))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message", Matchers.is(dto.message())))
            .andExpect(jsonPath("$.user", Matchers.is("testuser")))
            .andReturn();

        String createdAt = JsonPath.read(result.getResponse().getContentAsString(), "$.createdAt");
        assertThat(Instant.parse(createdAt), InstantMatchers.within(1, ChronoUnit.MINUTES, new Date().toInstant()));
    }

    @Test
    void post_when_anonymous_is_unauthorized() throws Exception {
        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var dto = new CommentAddDTO(new Faker().lorem().paragraph(2));

        this.mvc.perform(
                post(new CeventCommentAddEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(dto))
            )
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(value = "non_member_user")
    void post_when_non_member_user_is_forbidden() throws Exception {
        createSingleStudyUser("non_member_user");

        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var dto = new CommentAddDTO(new Faker().lorem().paragraph(2));

        this.mvc.perform(
                post(new CeventCommentAddEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(dto))
            )
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*permission.*")));
    }

    @Test
    @WithMockUser(value = "testuser")
    void post_fails_when_invalid_vnumber_is_not_found() throws Exception {
        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var badVnumber = collectionEvent.getVisitNumber() + 10;
        var dto = new CommentAddDTO(new Faker().lorem().paragraph(2));

        mvc
            .perform(
                post(new CeventCommentAddEndpoint(patient.getPnumber(), badVnumber).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(dto))
            )
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex("collection event.*")));
    }

    @Test
    @WithMockUser(value = "testuser")
    void post_fails_with_blank_message() throws Exception {
        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var badCommentDTO = new CommentAddDTO("");

        this.mvc.perform(
                post(new CeventCommentAddEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(badCommentDTO))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*cannot be blank.*")));
    }
}
