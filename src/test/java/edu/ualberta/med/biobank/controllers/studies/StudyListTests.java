package edu.ualberta.med.biobank.controllers.studies;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultMatcher;
import edu.ualberta.med.biobank.controllers.endpoints.StudiesListEndpoint;
import edu.ualberta.med.biobank.test.ControllerTest;

class StudyListTests extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(StudyListTests.class);

    private static ResultMatcher[] paginationMatchers(int numElements, int totalElements, int totalPages) {
        return new ResultMatcher[] {
            status().isOk(),
            jsonPath("$.content", hasSize(numElements)),
            jsonPath("$.numberOfElements", is(numElements)),
            jsonPath("$.totalElements", is(totalElements)),
            jsonPath("$.totalPages", is(totalPages))
        };
    }

    @Test
    @WithMockUser(value = "testuser")
    void get_page_when_empty_table_is_ok_and_empty() throws Exception {
        this.mvc.perform(get(new StudiesListEndpoint().url()))
            .andExpectAll(paginationMatchers(0, 0, 0));
    }

    @Test
    void get_when_present_and_unauthorized() throws Exception {
        this.mvc.perform(get(new StudiesListEndpoint().url()))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(value = "testuser")
    void get_page_when_present_is_ok() throws Exception {
        factory.createStudy();

        this.mvc.perform(get(new StudiesListEndpoint().url()))
            .andExpectAll(paginationMatchers(1, 1, 1));
    }

    @Test
    @WithMockUser(value = "single_study_user")
    void get_page_when_present_and_is_single_study_user_is_ok() throws Exception {
        createSingleStudyUser("single_study_user");
        factory.createStudy();

        this.mvc.perform(get(new StudiesListEndpoint().url()))
            .andExpectAll(paginationMatchers(1, 1, 1));
    }
}
