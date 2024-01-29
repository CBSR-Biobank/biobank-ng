package edu.ualberta.med.biobank.controllers.patients;

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
import edu.ualberta.med.biobank.controllers.endpoints.PatientCommentAddEndpoint;
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
class PatientCommentAddTests extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(PatientCommentAddTests.class);

    @Test
    @WithMockUser(value = "testuser")
    void patient_comment_post_succeeds() throws Exception {
        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var dto = new CommentAddDTO(new Faker().lorem().paragraph(2));

        MvcResult result = mvc
            .perform(
                post(new PatientCommentAddEndpoint(patient.getPnumber()).url())
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
    void patient_comment_post_when_anonymous_is_unauthorized() throws Exception {
        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var dto = new CommentAddDTO(new Faker().lorem().paragraph(2));

        this.mvc.perform(
                post(new PatientCommentAddEndpoint(patient.getPnumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(dto))
            )
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(value = "non_member_user")
    void patient_comment_post_when_non_member_user_is_forbidden() throws Exception {
        createSingleStudyUser("non_member_user");

        var patient = new PatientFixtureBuilder().numCollectionEvents(1).build(factory);
        var dto = new CommentAddDTO(new Faker().lorem().paragraph(2));

        this.mvc.perform(
                post(new PatientCommentAddEndpoint(patient.getPnumber()).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(dto))
            )
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*permission.*")));
    }

    @Test
    @WithMockUser(value = "testuser")
    void patient_comment_post_fails_when_invalid_pnumber_is_not_found() throws Exception {
        var badPnumber = new Faker().internet().username();
        var dto = new CommentAddDTO(new Faker().lorem().paragraph(2));

        mvc
            .perform(
                post(new PatientCommentAddEndpoint(badPnumber).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(dto))
            )
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex("patient.*not exist.*")))
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(value = "testuser")
    void patient_comment_post_fails_with_blank_message() throws Exception {
        var badPnumber = new Faker().internet().username();
        var dto = new CommentAddDTO("");

        this.mvc.perform(
                post(new PatientCommentAddEndpoint(badPnumber).url())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.asJsonString(dto))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*cannot be blank.*")))
            .andDo(MockMvcResultHandlers.print());
    }
}
