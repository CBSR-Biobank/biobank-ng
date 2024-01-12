package edu.ualberta.med.biobank.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import edu.ualberta.med.biobank.controllers.endpoints.LoggingLatestEndpoint;
import edu.ualberta.med.biobank.test.ControllerTest;

class LoggingControllerTest extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(LoggingControllerTest.class);

    protected final String ENDPOINT_INDEX_URL = "/logging";

    @Test
    @WithMockUser
    void getWhenEmptyTableIsOkAndEmpty() throws Exception {
        this.mvc.perform(get(new LoggingLatestEndpoint().url()))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getWhenPresentAndUnauthorized() throws Exception {
        this.mvc.perform(get(new LoggingLatestEndpoint().url()))
            .andExpect(status().isUnauthorized())
            .andDo(MockMvcResultHandlers.print());
    }
}
