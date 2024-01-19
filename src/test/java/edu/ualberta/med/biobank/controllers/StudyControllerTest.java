package edu.ualberta.med.biobank.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import edu.ualberta.med.biobank.controllers.endpoints.StudiesListEndpoint;
import edu.ualberta.med.biobank.controllers.endpoints.StudyEndpoint;
import edu.ualberta.med.biobank.controllers.endpoints.StudyNamesEndpoint;
import edu.ualberta.med.biobank.domain.Status;
import edu.ualberta.med.biobank.dtos.StudyDTO;
import edu.ualberta.med.biobank.dtos.StudyNameDTO;
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
    @WithMockUser(value = "testuser")
    void getPageWhenEmptyTableIsOkAndEmpty() throws Exception {
        this.mvc.perform(get(new StudiesListEndpoint().url()))
            .andExpectAll(paginationMatchers(0, 0, 0));
    }

    @Test
    void getWhenPresentAndUnauthorized() throws Exception {
        this.mvc.perform(get(new StudiesListEndpoint().url()))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(value = "testuser")
    void getPageWhenPresentIsOk() throws Exception {
        factory.createStudy();

        this.mvc.perform(get(new StudiesListEndpoint().url()))
            .andExpectAll(paginationMatchers(1, 1, 1));
    }

    @Test
    @WithMockUser(value = "single_study_user")
    void getPageWhenPresentAndIsSingleStudyUserIsOk() throws Exception {
        createSingleStudyUser("single_study_user");
        factory.createStudy();

        this.mvc.perform(get(new StudiesListEndpoint().url()))
            .andExpectAll(paginationMatchers(1, 1, 1));
    }

    @Test
    @WithMockUser(value = "testuser")
    void getSinglePresentIsOk() throws Exception {
        var study = factory.createStudy();

        MvcResult result =
            this.mvc.perform(get(new StudyEndpoint(study.getNameShort()).url())).andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        StudyDTO dto = mapper.readValue(result.getResponse().getContentAsString(), StudyDTO.class);
        assertThat(dto, StudyMatcher.matches(study));
    }

    @Test
    @WithMockUser(value = "testuser")
    void getSingleWhenNotExistIsNotFound() throws Exception {
        var badname = new Faker().lorem().fixedString(10);

        this.mvc.perform(get(new StudyEndpoint(badname).url()))
            .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @MethodSource("provideStatus")
    @WithMockUser(value = "testuser")
    void getStudyNamesWhenNonePresentIsNotFound(List<String> status) throws Exception {
        String[] statusArray;

        if (status.isEmpty()) {
            statusArray = null;
        } else {
            statusArray = status.toArray(new String[0]);
        }

        this.mvc.perform(get(new StudyNamesEndpoint(statusArray).url())).andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @MethodSource("provideStatus")
    @WithMockUser(value = "testuser")
    void getStudyNamesWhenPresentIsOk(List<String> status) throws Exception {
        var study = factory.createStudy();
        String[] statusArray;

        if (status.isEmpty()) {
            statusArray = null;
        } else {
            statusArray = status.toArray(new String[0]);
        }

        if (status.size() == 1) {
            study.setActivityStatus(Status.fromName(status.getFirst()));
            em.persist(study);
            em.flush();
        }

        MvcResult result = this.mvc.perform(get(new StudyNamesEndpoint(statusArray).url())).andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<StudyNameDTO> dtos = List.of(mapper.readValue(result.getResponse().getContentAsString(), StudyNameDTO[].class));
        assertThat(dtos, StudyMatcher.containsNames(List.of(study)));
    }

    @ParameterizedTest
    @MethodSource("provideStatus")
    @WithMockUser(value = "single_study_user")
    void getStudyNamesWhenPresentAndIsSingleStudyUserIsOk(List<String> status) throws Exception {
        var study = factory.createStudy();
        createSingleStudyUser("single_study_user");
        String[] statusArray;

        if (status.isEmpty()) {
            statusArray = null;
        } else {
            statusArray = status.toArray(new String[0]);
        }

        if (status.size() == 1) {
            study.setActivityStatus(Status.fromName(status.getFirst()));
            em.persist(study);
            em.flush();
        }

        MvcResult result = this.mvc.perform(get(new StudyNamesEndpoint(statusArray).url())).andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<StudyNameDTO> dtos = List.of(mapper.readValue(result.getResponse().getContentAsString(), StudyNameDTO[].class));
        assertThat(dtos, StudyMatcher.containsNames(List.of(study)));
    }

    private static List<List<String>> provideStatus() {
        List<List<String>> results = new ArrayList<>();
        results.add(List.of());
        Status.valuesList().stream().forEach(s -> results.add(List.of(s.toString().toLowerCase())));
        results.add(Status.valuesList().stream().map(s -> s.toString().toLowerCase()).toList());
        return results;
    }
}
