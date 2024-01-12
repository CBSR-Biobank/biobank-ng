package edu.ualberta.med.biobank.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import edu.ualberta.med.biobank.controllers.endpoints.StudiesListEndpoint;
import edu.ualberta.med.biobank.controllers.endpoints.StudyEndpoint;
import edu.ualberta.med.biobank.test.ControllerTest;
import net.datafaker.Faker;

class StudyControllerTest extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(StudyControllerTest.class);

    @Test
    @WithMockUser
    void getPageWhenEmptyTableIsOkAndEmpty() throws Exception {
        this.mvc.perform(get(new StudiesListEndpoint().url()))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.content", hasSize(0)))
            .andExpect(jsonPath("$.numberOfElements", is(0)))
            .andExpect(jsonPath("$.totalElements", is(0)))
            .andExpect(jsonPath("$.totalPages", is(0)));
    }

    @Test
    void getWhenPresentAndUnauthorized() throws Exception {
        this.mvc.perform(get(new StudiesListEndpoint().url()))
            .andExpect(status().isUnauthorized())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(value = "testuser")
    void getPageWhenPresentIsOk() throws Exception {
        factory.createStudy();

        this.mvc.perform(get(new StudiesListEndpoint().url()))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.content", hasSize(1)))
            .andExpect(jsonPath("$.numberOfElements", is(1)))
            .andExpect(jsonPath("$.totalElements", is(1)))
            .andExpect(jsonPath("$.totalPages", is(1)));
    }

    @Test
    @WithMockUser(value = "testuser")
    void getSinglePresentIsOk() throws Exception {
        var study = factory.createStudy();

        this.mvc.perform(get(new StudyEndpoint(study.getNameShort()).url()))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.name", is(study.getName())))
            .andExpect(jsonPath("$.nameShort", is(study.getNameShort())))
            .andExpect(jsonPath("$.activityStatus", is(study.getActivityStatus().getName())));
    }

    @Test
    @WithMockUser(value = "testuser")
    void getSingleWhenNotExistIsNotFound() throws Exception {
        var badname = (new Faker()).lorem().fixedString(10);

        this.mvc.perform(get(new StudyEndpoint(badname).url()))
            .andExpect(status().isNotFound())
            .andDo(MockMvcResultHandlers.print());
    }
}
