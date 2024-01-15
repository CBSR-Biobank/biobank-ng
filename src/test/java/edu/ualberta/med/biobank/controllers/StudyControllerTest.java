package edu.ualberta.med.biobank.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import edu.ualberta.med.biobank.controllers.endpoints.StudiesListEndpoint;
import edu.ualberta.med.biobank.controllers.endpoints.StudyEndpoint;
import edu.ualberta.med.biobank.dtos.StudyDTO;
import edu.ualberta.med.biobank.matchers.StudyMatcher;
import edu.ualberta.med.biobank.test.ControllerTest;
import net.datafaker.Faker;

class StudyControllerTest extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(StudyControllerTest.class);

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
    @WithMockUser
    void getPageWhenEmptyTableIsOkAndEmpty() throws Exception {

        this.mvc.perform(get(new StudiesListEndpoint().url()))
            .andExpectAll(paginationMatchers(0, 0, 0))
            .andDo(MockMvcResultHandlers.print());
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
            .andExpectAll(paginationMatchers(1, 1, 1))
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(value = "testuser")
    void getSinglePresentIsOk() throws Exception {
        var study = factory.createStudy();

        MvcResult result =
            this.mvc.perform(get(new StudyEndpoint(study.getNameShort()).url()))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        StudyDTO dto = mapper.readValue(result.getResponse().getContentAsString(), StudyDTO.class);
        assertThat(dto, StudyMatcher.matches(study));
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
