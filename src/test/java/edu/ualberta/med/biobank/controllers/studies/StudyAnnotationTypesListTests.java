package edu.ualberta.med.biobank.controllers.studies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ualberta.med.biobank.controllers.endpoints.StudyAnnotationTypesEndpoint;
import edu.ualberta.med.biobank.domain.Status;
import edu.ualberta.med.biobank.domain.Study;
import edu.ualberta.med.biobank.dtos.AnnotationTypeDTO;
import edu.ualberta.med.biobank.matchers.StudyMatcher;
import edu.ualberta.med.biobank.test.ControllerTest;
import edu.ualberta.med.biobank.test.fixtures.StudyFixtureBuilder;
import edu.ualberta.med.biobank.util.LoggingUtils;
import java.util.List;
import net.datafaker.Faker;
import org.assertj.core.api.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

class StudyAnnotationTypesListTests extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(StudyAnnotationTypesListTests.class);

    @ParameterizedTest
    @MethodSource("edu.ualberta.med.biobank.controllers.studies.StudyNameListTests#provideStatus")
    @WithMockUser(value = "testuser")
    void get_when_none_present_is_not_found(List<String> statuses) throws Exception {
        String badName = new Faker().lorem().word();
        StudyAnnotationTypesEndpoint endpoint = new StudyAnnotationTypesEndpoint(
            badName,
            statuses.toArray(new String[0])
        );

        this.mvc.perform(get(endpoint.url()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex("study")))
            .andDo(MockMvcResultHandlers.print());
    }

    @ParameterizedTest
    @MethodSource("edu.ualberta.med.biobank.controllers.studies.StudyNameListTests#provideStatus")
    @WithMockUser(value = "testuser")
    void get_whith_none_present_is_not_found(List<String> statuses) throws Exception {
        Study study = new StudyFixtureBuilder().setEntityManger(em).build(factory);
        StudyAnnotationTypesEndpoint endpoint = new StudyAnnotationTypesEndpoint(
            study.getNameShort(),
            statuses.toArray(new String[0])
        );

        MvcResult result =  this.mvc.perform(get(endpoint.url())).andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<AnnotationTypeDTO> dtos = List.of(
            mapper.readValue(result.getResponse().getContentAsString(), AnnotationTypeDTO[].class)
        );
        assertThat(dtos, Matchers.hasSize(0));
    }

    @ParameterizedTest
    @MethodSource("edu.ualberta.med.biobank.controllers.studies.StudyNameListTests#provideStatus")
    @WithMockUser(value = "testuser")
    void get_whith_present_is_ok(List<String> statuses) throws Exception {
        String firstStatus = statuses.size() > 0 ? statuses.get(0) : Status.ACTIVE.getName();

        Study study = new StudyFixtureBuilder()
            .setEntityManger(em)
            .withAttributeType("text", "test_label", firstStatus, false)
            .build(factory);

        StudyAnnotationTypesEndpoint endpoint = new StudyAnnotationTypesEndpoint(
            study.getNameShort(),
            statuses.toArray(new String[0])
        );

        MvcResult result = this.mvc.perform(get(endpoint.url())).andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<AnnotationTypeDTO> dtos = List.of(
            mapper.readValue(result.getResponse().getContentAsString(), AnnotationTypeDTO[].class)
        );
        assertThat(dtos, StudyMatcher.containsAttributeTypes(study));
    }

    @ParameterizedTest
    @MethodSource("edu.ualberta.med.biobank.controllers.studies.StudyNameListTests#provideStatus")
    @WithMockUser(value = "non_member_user")
    void get_whith_present_and_non_member_user_is_forbidden(List<String> statuses) throws Exception {
        createSingleStudyUser("non_member_user");
        String firstStatus = statuses.size() > 0 ? statuses.get(0) : Status.ACTIVE.getName();

        Study study = new StudyFixtureBuilder()
            .setEntityManger(em)
            .withAttributeType("text", "test_label", firstStatus, false)
            .build(factory);

        StudyAnnotationTypesEndpoint endpoint = new StudyAnnotationTypesEndpoint.Builder()
            .withNameShort(study.getNameShort())
            .withStatus(firstStatus)
            .build();

        this.mvc.perform(get(endpoint.url()))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*permission.*")));
    }
}
