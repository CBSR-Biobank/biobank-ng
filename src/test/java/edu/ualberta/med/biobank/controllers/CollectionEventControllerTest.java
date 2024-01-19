package edu.ualberta.med.biobank.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.junit.jupiter.Testcontainers;
import edu.ualberta.med.biobank.controllers.endpoints.VisitNumberEndpoint;
import edu.ualberta.med.biobank.dtos.CollectionEventDTO;
import edu.ualberta.med.biobank.matchers.CollectionEventMatcher;
import edu.ualberta.med.biobank.test.ControllerTest;
import edu.ualberta.med.biobank.test.TestFixtures;
import edu.ualberta.med.biobank.util.LoggingUtils;
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
        var patient = new TestFixtures.PatientFixtureBuilder()
            .numCollectionEvents(1)
            .build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();

        this.mvc.perform(get(new VisitNumberEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url()))
            .andExpect(status().isUnauthorized())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(value = "testuser")
    void getWhenPresentIsOk() throws Exception {
        var patient = new TestFixtures.PatientFixtureBuilder()
            .numCollectionEvents(1)
            .numComments(1)
            .numSpecimens(1)
            .numAliquots(1)
            .build(factory);

        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();

        MvcResult result =
            this.mvc.perform(get(new VisitNumberEndpoint(patient.getPnumber(), collectionEvent.getVisitNumber()).url()))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        CollectionEventDTO dto = mapper.readValue(result.getResponse().getContentAsString(), CollectionEventDTO.class);
        logger.info("HTTP response: {}", LoggingUtils.prettyPrintJson(dto));
        assertThat(dto, CollectionEventMatcher.matches(collectionEvent));
    }

    @Test
    @WithMockUser(value = "testuser")
    void getWhenNotExistIsNotFound() throws Exception {
        var patient = new TestFixtures.PatientFixtureBuilder()
            .numCollectionEvents(1)
            .build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var badVisitNumber = collectionEvent.getVisitNumber() + 9999;

        this.mvc.perform(get(new VisitNumberEndpoint(patient.getPnumber(), badVisitNumber).url()))
            .andExpect(status().isNotFound())
            .andDo(MockMvcResultHandlers.print());
    }
}
