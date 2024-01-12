package edu.ualberta.med.biobank.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.junit.jupiter.Testcontainers;
import edu.ualberta.med.biobank.controllers.endpoints.VisitNumberEndpoint;
import edu.ualberta.med.biobank.test.ControllerTest;
import edu.ualberta.med.biobank.test.TestFixtures;
import jakarta.transaction.Transactional;

@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CollectionEventControllerTest extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(CollectionEventControllerTest.class);

    @Test
    @WithMockUser
    void getWhenEmptyTableIs404() throws Exception {
        var patient = factory.createPatient();

        this.mvc.perform(get(new VisitNumberEndpoint(patient.getPnumber(), 9999).url()))
            .andExpect(status().isNotFound())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getWhenPresentAndUnauthorized() throws Exception {
        var patient = TestFixtures.patientFixture(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();

        this.mvc.perform(get(new VisitNumberEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url()))
            .andExpect(status().isUnauthorized())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(value = "testuser")
    void getWhenPresentIsOk() throws Exception {
        var patient = TestFixtures.patientFixture(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();

        this.mvc.perform(get(new VisitNumberEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url()))
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

        this.mvc.perform(get(new VisitNumberEndpoint(patient.getPnumber(), badVisitNumber).url()))
            .andExpect(status().isNotFound())
            .andDo(MockMvcResultHandlers.print());
    }
}
