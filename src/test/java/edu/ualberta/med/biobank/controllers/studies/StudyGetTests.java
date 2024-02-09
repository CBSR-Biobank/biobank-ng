package edu.ualberta.med.biobank.controllers.studies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import edu.ualberta.med.biobank.controllers.endpoints.StudyEndpoint;
import edu.ualberta.med.biobank.dtos.StudyDTO;
import edu.ualberta.med.biobank.matchers.StudyMatcher;
import edu.ualberta.med.biobank.test.ControllerTest;
import net.datafaker.Faker;

class StudyGetTests extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(StudyGetTests.class);

    @Test
    @WithMockUser(value = "testuser")
    void get_when_present_is_ok() throws Exception {
        var study = factory.createStudy();

        MvcResult result =
            this.mvc.perform(get(new StudyEndpoint(study.getNameShort()).url())).andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        StudyDTO dto = mapper.readValue(result.getResponse().getContentAsString(), StudyDTO.class);
        assertThat(dto, StudyMatcher.matches(study));
    }

    @Test
    @WithMockUser(value = "testuser")
    void get_when_not_exist_is_not_found() throws Exception {
        var badname = new Faker().lorem().fixedString(10);

        this.mvc.perform(get(new StudyEndpoint(badname).url()))
            .andExpect(status().isNotFound());
    }
}
