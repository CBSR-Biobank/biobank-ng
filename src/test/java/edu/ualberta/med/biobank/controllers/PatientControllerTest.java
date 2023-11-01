package edu.ualberta.med.biobank.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import com.jayway.jsonpath.JsonPath;

import org.exparity.hamcrest.date.InstantMatchers;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeAll;
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

import edu.ualberta.med.biobank.domain.CollectionEvent;
import edu.ualberta.med.biobank.domain.Comment;
import edu.ualberta.med.biobank.domain.Patient;
import edu.ualberta.med.biobank.domain.Specimen;
import edu.ualberta.med.biobank.test.BaseTest;
import edu.ualberta.med.biobank.test.Factory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import net.datafaker.Faker;

@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PatientControllerTest extends BaseTest {

    private final Logger logger = LoggerFactory.getLogger(PatientControllerTest.class);

    private final String ENDPOINT_URL = "/patients/:pnumber";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MockMvc mvc;

    @Rule
    public TestName testname = new TestName();

    private String name;

    private Factory factory;

    private static Faker faker;

    @BeforeAll
    public static void init() throws Exception {
        faker = new Faker();
    }

    @BeforeEach
    public void setup() throws Exception {
        name = getMethodNameR();
        this.factory = new Factory(em);
    }

    @Test
    @WithMockUser
    public void getWhenEmptyTableIs404() throws Exception {
        this.mvc.perform(get(endpointUrl(faker.lorem().word())))
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
        var patient = fixture();

        // logger.info("patient: {}", LoggingUtils.prettyPrintJson(patient));

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
        return ENDPOINT_URL.replace(":pnumber", pnumber);
    }

    private Patient fixture() {
        return fixture(Optional.of(1), Optional.of(1));
    }

    private Patient fixture(Optional<Integer> numCollectionEvents, Optional<Integer> numSpecimens) {
        Patient patient = factory.createPatient();

        Comment comment = factory.createComment();
        comment.setMessage(getMethodNameR());
        patient.getComments().add(comment);

        var ceCount = numCollectionEvents.orElse(1);
        var spcCount = numSpecimens.orElse(1);

        for (int j = 0; j < ceCount; ++j) {
            CollectionEvent cevent = factory.createCollectionEvent();
            comment = factory.createComment();
            comment.setMessage(getMethodNameR());
            cevent.getComments().add(comment);

            for (int k = 0; k < spcCount; ++k) {
                Specimen specimen = factory.createParentSpecimen();
                comment = factory.createComment();
                comment.setMessage(getMethodNameR());
                specimen.getComments().add(comment);
            }
        }

        return patient;
    }
}
