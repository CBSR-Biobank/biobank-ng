package edu.ualberta.med.biobank.controllers.clinics;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import edu.ualberta.med.biobank.controllers.endpoints.ClinicNamesEndpoint;
import edu.ualberta.med.biobank.domain.Status;
import edu.ualberta.med.biobank.dtos.ClinicNameDTO;
import edu.ualberta.med.biobank.matchers.ClinicMatcher;
import edu.ualberta.med.biobank.test.ControllerTest;

class ClinicNameListTests extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(ClinicNameListTests.class);

    private static ResultMatcher[] paginationMatchers(int numElements, int totalElements, int totalPages) {
        return new ResultMatcher[] {
            status().isOk(),
            jsonPath("$.content", hasSize(numElements)),
            jsonPath("$.numberOfElements", is(numElements)),
            jsonPath("$.totalElements", is(totalElements)),
            jsonPath("$.totalPages", is(totalPages))
        };
    }

    @ParameterizedTest

    @MethodSource("provideStatus")
    @WithMockUser(value = "testuser")
    void get_when_none_present_is_not_found(List<String> statuses) throws Exception {
        ClinicNamesEndpoint endpoint = new ClinicNamesEndpoint(statuses.toArray(new String[0]));
        this.mvc.perform(get(endpoint.url())).andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @MethodSource("provideStatus")
    @WithMockUser(value = "testuser")
    void get_when_present_is_ok(List<String> statuses) throws Exception {
        var clinic = factory.createClinic();
        ClinicNamesEndpoint endpoint = new ClinicNamesEndpoint(statuses.toArray(new String[0]));

        if (statuses.size() == 1) {
            clinic.setActivityStatus(Status.fromName(statuses.getFirst()));
            em.persist(clinic);
            em.flush();
        }

        MvcResult result = this.mvc.perform(get(endpoint.url())).andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<ClinicNameDTO> dtos = List.of(mapper.readValue(result.getResponse().getContentAsString(), ClinicNameDTO[].class));
        assertThat(dtos, ClinicMatcher.containsNames(List.of(clinic)));
    }

    @ParameterizedTest
    @MethodSource("provideStatus")
    @WithMockUser(value = "single_clinic_user")
    void get_when_present_and_is_single_clinic_user_is_ok(List<String> statuses) throws Exception {
        var clinic = factory.createClinic();
        createSingleStudyUser("single_clinic_user");
        ClinicNamesEndpoint endpoint = new ClinicNamesEndpoint(statuses.toArray(new String[0]));

        if (statuses.size() == 1) {
            clinic.setActivityStatus(Status.fromName(statuses.getFirst()));
            em.persist(clinic);
            em.flush();
        }

        MvcResult result = this.mvc.perform(get(endpoint.url())).andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<ClinicNameDTO> dtos = List.of(mapper.readValue(result.getResponse().getContentAsString(), ClinicNameDTO[].class));
        assertThat(dtos, ClinicMatcher.containsNames(List.of(clinic)));
    }

    private static List<List<String>> provideStatus() {
        List<List<String>> results = new ArrayList<>();
        results.add(List.of());
        Status.valuesList().stream().forEach(s -> results.add(List.of(s.toString().toLowerCase())));
        results.add(Status.valuesList().stream().map(s -> s.toString().toLowerCase()).toList());
        return results;
    }
}
