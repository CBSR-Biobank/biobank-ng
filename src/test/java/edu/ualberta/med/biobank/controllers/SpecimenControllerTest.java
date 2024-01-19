package edu.ualberta.med.biobank.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ualberta.med.biobank.controllers.endpoints.AliquotsEndpoint;
import edu.ualberta.med.biobank.controllers.endpoints.VisitNumberEndpoint;
import edu.ualberta.med.biobank.dtos.AliquotSpecimenDTO;
import edu.ualberta.med.biobank.matchers.AliquotMatcher;
import edu.ualberta.med.biobank.test.ControllerTest;
import edu.ualberta.med.biobank.test.TestFixtures;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SpecimenControllerTest extends ControllerTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(SpecimenControllerTest.class);

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
            .numSpecimens(1)
            .build(factory);
        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var specimen = collectionEvent.getOriginalSpecimens().stream().findFirst().get();

        this.mvc.perform(get(new AliquotsEndpoint(specimen.getInventoryId()).url()))
            .andExpect(status().isUnauthorized())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(value = "testuser")
    @DisplayName("When source specimen is present returns OK")
    void getWhenPresentIsOk() throws Exception {
        var patient = new TestFixtures.PatientFixtureBuilder()
            .numCollectionEvents(1)
            .numSpecimens(1)
            .numAliquots(1)
            .build(factory);

        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var specimen = collectionEvent.getOriginalSpecimens().stream().findFirst().get();
        var aliquot = collectionEvent
            .getAllSpecimens()
            .stream()
            .filter(s -> s.getParentSpecimen() != null)
            .findFirst()
            .get();

        MvcResult result =
            this.mvc.perform(get(new AliquotsEndpoint(specimen.getInventoryId()).url()))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<AliquotSpecimenDTO> dtos = List.of(mapper.readValue(
            result.getResponse().getContentAsString(),
            AliquotSpecimenDTO[].class
        ));

        assertThat(dtos, AliquotMatcher.containsAll(List.of(aliquot)));
    }

    @Test
    @WithMockUser(value = "testuser")
    @DisplayName("When source specimen inventory ID is invalid returns 404")
    void getWhenNotPresentIsNotFound() throws Exception {
        var patient = new TestFixtures.PatientFixtureBuilder()
            .numCollectionEvents(1)
            .numSpecimens(1)
            .build(factory);

        var collectionEvent = patient.getCollectionEvents().stream().findFirst().get();
        var specimen = collectionEvent.getOriginalSpecimens().stream().findFirst().get();
        var badInventoryId = specimen.getInventoryId() + "_bad";

        this.mvc.perform(get(new AliquotsEndpoint(badInventoryId).url()))
            .andExpect(status().isNotFound())
            .andDo(MockMvcResultHandlers.print());
    }
}
