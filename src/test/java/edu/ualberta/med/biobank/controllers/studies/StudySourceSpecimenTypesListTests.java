package edu.ualberta.med.biobank.controllers.studies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ualberta.med.biobank.controllers.endpoints.StudySourceSpecimenTypesEndpoint;
import edu.ualberta.med.biobank.domain.Study;
import edu.ualberta.med.biobank.dtos.SourceSpecimenTypeDTO;
import edu.ualberta.med.biobank.matchers.StudyMatcher;
import edu.ualberta.med.biobank.test.ControllerTest;
import edu.ualberta.med.biobank.test.fixtures.StudyFixtureBuilder;
import java.util.List;
import net.datafaker.Faker;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

class StudySourceSpecimenTypesListTests extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(StudySourceSpecimenTypesListTests.class);

    @Test
    @WithMockUser(value = "testuser")
    void get_when_none_present_is_not_found() throws Exception {
        String badName = new Faker().lorem().word();
        StudySourceSpecimenTypesEndpoint endpoint = new StudySourceSpecimenTypesEndpoint(badName);

        this.mvc.perform(get(endpoint.url()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex("study")))
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(value = "testuser")
    void get_whith_none_present_is_ok_and_empty_result() throws Exception {
        Study study = new StudyFixtureBuilder().setEntityManger(em).build(factory);
        StudySourceSpecimenTypesEndpoint endpoint = new StudySourceSpecimenTypesEndpoint(study.getNameShort());

        MvcResult result = this.mvc.perform(get(endpoint.url())).andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<SourceSpecimenTypeDTO> dtos = List.of(
            mapper.readValue(result.getResponse().getContentAsString(), SourceSpecimenTypeDTO[].class)
        );
        assertThat(dtos, Matchers.hasSize(0));
    }

    @Test
    @WithMockUser(value = "testuser")
    void get_whith_present_is_ok() throws Exception {
        Study study = new StudyFixtureBuilder()
            .setEntityManger(em)
            .withSourceSpecimenType("test_specimen_type", "test_specimen_type_short", false)
            .build(factory);

        StudySourceSpecimenTypesEndpoint endpoint = new StudySourceSpecimenTypesEndpoint(study.getNameShort());

        MvcResult result = this.mvc.perform(get(endpoint.url())).andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<SourceSpecimenTypeDTO> dtos = List.of(
            mapper.readValue(result.getResponse().getContentAsString(), SourceSpecimenTypeDTO[].class)
        );
        assertThat(dtos, StudyMatcher.containsSourceSpecimenTypes(study));
    }

    @Test
    @WithMockUser(value = "non_member_user")
    void get_whith_present_and_non_member_user_is_forbidden() throws Exception {
        createSingleStudyUser("non_member_user");
        Study study = new StudyFixtureBuilder()
            .setEntityManger(em)
            .withSourceSpecimenType("test_specimen_type", "test_specimen_type_short", false)
            .build(factory);

        StudySourceSpecimenTypesEndpoint endpoint = new StudySourceSpecimenTypesEndpoint(study.getNameShort());

        this.mvc.perform(get(endpoint.url()))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message", Matchers.matchesRegex(".*permission.*")));
    }
}
