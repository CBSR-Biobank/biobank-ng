package edu.ualberta.med.biobank.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.ualberta.med.biobank.test.ControllerTest;
import edu.ualberta.med.biobank.test.Factory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

class StudyControllerTest extends ControllerTest {

    private final Logger logger = LoggerFactory.getLogger(StudyControllerTest.class);

    private final String ENDPOINT_INDEX_URL = "/studies";

    private final String ENDPOINT_URL = ENDPOINT_INDEX_URL + "/{name}";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MockMvc mvc;

    private Factory factory;

    @BeforeEach
    public void setup() throws Exception {
        this.factory = new Factory(em);
    }

    @Test
    @WithMockUser
    public void getWhenEmptyTableIsOkAndEmpty() throws Exception {
        this.mvc.perform(get(ENDPOINT_INDEX_URL))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.content", hasSize(0)))
            .andExpect(jsonPath("$.numberOfElements", is(0)))
            .andExpect(jsonPath("$.totalElements", is(0)))
            .andExpect(jsonPath("$.totalPages", is(0)));
    }

    @Test
    public void getWhenPresentAndUnauthorized() throws Exception {
        this.mvc.perform(get(ENDPOINT_INDEX_URL))
            .andExpect(status().isUnauthorized())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(value = "testuser")
    public void getWhenPresentIsOk() throws Exception {
        factory.createStudy();

        this.mvc.perform(get(ENDPOINT_INDEX_URL))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.content", hasSize(1)))
            .andExpect(jsonPath("$.numberOfElements", is(1)))
            .andExpect(jsonPath("$.totalElements", is(1)))
            .andExpect(jsonPath("$.totalPages", is(1)));
    }

    private String endpointUrl(String studyNameShort) {
        return ENDPOINT_URL.replace("{name}", studyNameShort);
    }
}
