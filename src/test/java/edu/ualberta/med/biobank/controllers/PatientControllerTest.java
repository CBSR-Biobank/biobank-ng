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
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.junit.jupiter.Testcontainers;

import edu.ualberta.med.biobank.test.BaseTest;
import edu.ualberta.med.biobank.test.Factory;
import edu.ualberta.med.biobank.test.TestFixtures;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PatientControllerTest extends BaseTest {

    private final Logger logger = LoggerFactory.getLogger(PatientControllerTest.class);

    private final String ENDPOINT_URL = "/patients/{pnumber}";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MockMvc mvc;

    @Rule
    public TestName testname = new TestName();

    private String name;

    private Factory factory;

    @BeforeEach
    public void setup() throws Exception {
        name = getMethodNameR();
        this.factory = new Factory(em);
    }

    @Test
    @WithMockUser
    public void getWhenEmptyTableIs404() throws Exception {
        this.mvc.perform(get(endpointUrl(factory.getFaker().lorem().word())))
            .andExpect(status().isNotFound())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getWhenPresentAndUnauthorized() throws Exception {
        var patient = factory.createPatient();

        this.mvc.perform(get(endpointUrl(patient.getPnumber())))
            .andExpect(status().isUnauthorized())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(value = "testuser")
    public void getWhenPresentIsOk() throws Exception {
        var patient = TestFixtures.patientFixture(factory);

        MvcResult result =
            this.mvc.perform(get(endpointUrl(patient.getPnumber())))
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

    private String endpointUrl(String pnumber) {
        return ENDPOINT_URL.replace("{pnumber}", pnumber);
    }
}