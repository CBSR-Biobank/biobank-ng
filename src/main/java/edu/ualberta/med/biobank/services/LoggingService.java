package edu.ualberta.med.biobank.services;

import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import edu.ualberta.med.biobank.applicationevents.PatientReadEvent;
import edu.ualberta.med.biobank.applicationevents.SpecimenReadEvent;
import edu.ualberta.med.biobank.applicationevents.UserLoggedInEvent;
import edu.ualberta.med.biobank.applicationevents.VisitReadEvent;
import edu.ualberta.med.biobank.domain.Log;
import edu.ualberta.med.biobank.dtos.LoggingDTO;
import edu.ualberta.med.biobank.repositories.LoggingRepository;
import jakarta.persistence.Tuple;

@Service
public class LoggingService {

    final Logger logger = LoggerFactory.getLogger(LoggingService.class);

    private LoggingRepository loggingRepository;

    public LoggingService(LoggingRepository loggingRepository) {
        this.loggingRepository = loggingRepository;
    }

    public Page<LoggingDTO> loggingPagination(Integer pageNumber, Integer pageSize, String sort) {
        Pageable pageable = null;
        if (sort != null) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, sort);
        } else {
            pageable = PageRequest.of(pageNumber, pageSize);
        }
        Page<Tuple> data = loggingRepository.findAllWithPagination(pageable);
        return data.map(d -> LoggingDTO.fromTuple(d));
    }

    public List<LoggingDTO> loggingLatest() {
        Collection<Tuple> data = loggingRepository.getLastest();
        return data.stream().map(d -> LoggingDTO.fromTuple(d)).toList();
    }

    @EventListener
    void handleEmployeeEvent(UserLoggedInEvent event) {
        Log logEvent = new Log.LogBuilder()
            .action("login")
            .username(event.getUsername())
            .build();
        loggingRepository.save(logEvent);
    }

    @EventListener
    void handlePatientReadEvent(PatientReadEvent event) {
        Log logEvent = new Log.LogBuilder()
            .action("select")
            .username(event.getUsername())
            .patientNumber(event.getPnumber())
            .type("Patient")
            .build();
        loggingRepository.save(logEvent);
    }

    @EventListener
    void handleVisitReadEvent(VisitReadEvent event) {
        Log logEvent = new Log.LogBuilder()
            .action("select")
            .username(event.getUsername())
            .patientNumber(event.getPnumber())
            .type("CollectionEvent")
            .details("visit: %s".formatted(event.getVnumber()))
            .build();
        loggingRepository.save(logEvent);
    }

    @EventListener
    void handleVisitReadEvent(SpecimenReadEvent event) {
        Log logEvent = new Log.LogBuilder()
            .action("select")
            .username(event.getUsername())
            .patientNumber(event.getPnumber())
            .inventoryId(event.getInventoryId())
            .type("Specimen")
            .build();
        loggingRepository.save(logEvent);
    }
}
