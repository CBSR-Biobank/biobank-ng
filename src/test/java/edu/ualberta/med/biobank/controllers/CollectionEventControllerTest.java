package edu.ualberta.med.biobank.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
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
class CollectionEventControllerTest extends BaseTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(CollectionEventControllerTest.class);

    private final String ENDPOINT_URL = "/patients/{pnumber}/collection-events/{vnumber}";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MockMvc mvc;

    
    public String testname;


    private String name;
    private Factory factory;

    @BeforeEach
    public void setup(TestInfo testInfo) {
        super.setup(testInfo);
        name = getMethodNameR();
        this.factory = new Factory(em);
    }

    @Test
    @WithMockUser
    void getWhenEmptyTableIs404() throws Exception {
        var patient = factory.createPatient();

        this.mvc.perform(get(endpointUrl(patient.getPnumber(), 9999)))
            .andExpect(status().isNotFound())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getWhenPresentAndUnauthorized() throws Exception {
        var patient = TestFixtures.patientFixture(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();

        this.mvc.perform(get(endpointUrl(patient.getPnumber(), collectionEvent.getVisitNumber())))
            .andExpect(status().isUnauthorized())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(value = "testuser")
    void getWhenPresentIsOk() throws Exception {
        var patient = TestFixtures.patientFixture(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();

        this.mvc.perform(get(endpointUrl(patient.getPnumber(), collectionEvent.getVisitNumber())))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.id", is(collectionEvent.getId())))
            .andExpect(jsonPath("$.visitNumber", is(collectionEvent.getVisitNumber())))
            .andExpect(jsonPath("$.patientId", is(patient.getId())))
            .andExpect(jsonPath("$.patientNumber", is(patient.getPnumber())))
            .andExpect(jsonPath("$.studyId", is(patient.getStudy().getId())))
            .andExpect(jsonPath("$.studyNameShort", is(patient.getStudy().getNameShort())))
            .andExpect(jsonPath("$.sourceSpecimens", hasSize(collectionEvent.getOriginalSpecimens().size())))
            .andExpect(jsonPath("$.status", is(collectionEvent.getActivityStatus().getName())))
            .andReturn();
    }

    @Test
    @WithMockUser(value = "testuser")
    void getWhenNotExistIsNotFound() throws Exception {
        var patient = TestFixtures.patientFixture(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var badVisitNumber = collectionEvent.getVisitNumber() + 9999;

        this.mvc.perform(get(endpointUrl(patient.getPnumber(), badVisitNumber)))
            .andExpect(status().isNotFound())
            .andDo(MockMvcResultHandlers.print());
    }

    private String endpointUrl(String pnumber, Integer visitNumber) {
        return ENDPOINT_URL.replace("{pnumber}", pnumber).replace("{vnumber}", visitNumber.toString());
    }
}
