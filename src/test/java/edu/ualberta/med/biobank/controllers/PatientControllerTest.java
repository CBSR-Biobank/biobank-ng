package edu.ualberta.med.biobank.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import com.jayway.jsonpath.JsonPath;
import org.exparity.hamcrest.date.InstantMatchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.junit.jupiter.Testcontainers;
import edu.ualberta.med.biobank.controllers.endpoints.PatientNumberEndpoint;
import edu.ualberta.med.biobank.test.ControllerTest;
import edu.ualberta.med.biobank.test.TestFixtures;
import jakarta.transaction.Transactional;
import net.datafaker.Faker;

@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PatientControllerTest extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(PatientControllerTest.class);

    @Test
    @WithMockUser
    void getWhenEmptyTableIs404() throws Exception {
        this.mvc.perform(get(new PatientNumberEndpoint(factory.getFaker().lorem().word()).url()))
            .andExpect(status().isNotFound())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getWhenPresentAndUnauthorized() throws Exception {
        var patient = factory.createPatient();
        this.mvc.perform(get(new PatientNumberEndpoint(patient.getPnumber()).url()))
            .andExpect(status().isUnauthorized())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(value = "testuser")
    void getWhenPresentIsOk() throws Exception {
        var patient = TestFixtures.patientFixture(factory);

        MvcResult result =
            this.mvc.perform(get(new PatientNumberEndpoint(patient.getPnumber()).url()))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.pnumber", is(patient.getPnumber())))
                .andExpect(jsonPath("$.specimenCount", is(1)))
                .andExpect(jsonPath("$.aliquotCount", is(0)))
                .andExpect(jsonPath("$.collectionEvents", hasSize(1)))
                .andExpect(jsonPath("$.studyId", is(patient.getStudy().getId())))
                .andExpect(jsonPath("$.studyNameShort", is(patient.getStudy().getNameShort())))
                .andReturn();

        String createdAt = JsonPath.read(result.getResponse().getContentAsString(), "$.createdAt");
        assertThat(
            Instant.parse(createdAt),
            InstantMatchers.within(1, ChronoUnit.SECONDS, patient.getCreatedAt().toInstant())
        );
    }

    @Test
    @WithMockUser(value = "testuser")
    void getWhenNotExistIsNotFound() throws Exception {
        var badname = new Faker().lorem().fixedString(10);

        this.mvc.perform(get(new PatientNumberEndpoint(badname).url()))
            .andExpect(status().isNotFound())
            .andDo(MockMvcResultHandlers.print());
    }
}
