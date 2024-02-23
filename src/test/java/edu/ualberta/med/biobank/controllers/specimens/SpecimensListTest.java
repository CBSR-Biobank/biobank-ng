package edu.ualberta.med.biobank.controllers.specimens;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.testcontainers.junit.jupiter.Testcontainers;
import edu.ualberta.med.biobank.controllers.endpoints.VisitNumberEndpoint;
import edu.ualberta.med.biobank.test.ControllerTest;
import jakarta.transaction.Transactional;

@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SpecimensListTest extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(SpecimensListTest.class);

    @Test
    @WithMockUser
    void when_empty_table_is404() throws Exception {
        var patient = factory.createPatient();

        this.mvc.perform(get(new VisitNumberEndpoint(patient.getPnumber(), 9999).url()))
            .andExpect(status().isNotFound());
    }
}
