package edu.ualberta.med.biobank.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import edu.ualberta.med.biobank.test.ControllerTest;
import edu.ualberta.med.biobank.test.Factory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

class LoggingControllerTest extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(LoggingControllerTest.class);

    private final String ENDPOINT_INDEX_URL = "/logs";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    
    private MockMvc mvc;
    @BeforeEach
    void setup() throws Exception {
        this.factory = new Factory(em);
    }

    @Test
    @WithMockUser
    void getWhenEmptyTableIsOkAndEmpty() throws Exception {
        this.mvc.perform(get(ENDPOINT_INDEX_URL))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getWhenPresentAndUnauthorized() throws Exception {
        this.mvc.perform(get(ENDPOINT_INDEX_URL))
            .andExpect(status().isUnauthorized())
            .andDo(MockMvcResultHandlers.print());
    }
}
