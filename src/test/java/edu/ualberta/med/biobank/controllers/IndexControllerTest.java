package edu.ualberta.med.biobank.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class IndexControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void rootWhenUnauthenticatedThen401() throws Exception {
        this.mvc.perform(get("/")).andExpect(status().isUnauthorized());
    }

    @Test
    void rootWithBasicStatusIsUnauthorized() throws Exception {
        this.mvc.perform(get("/").with(httpBasic("testuser", "test"))).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void rootWithMockUserStatusIsOK() throws Exception {
        this.mvc.perform(get("/")).andExpect(status().isOk());
    }
}
