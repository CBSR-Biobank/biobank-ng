package edu.ualberta.med.biobank.services;

import edu.ualberta.med.biobank.applicationEvents.UserLoggedInEvent;
import edu.ualberta.med.biobank.domain.Log;
import edu.ualberta.med.biobank.repositories.LoggingRepository;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class LoggingService {

    final Logger logger = LoggerFactory.getLogger(LoggingService.class);

    private LoggingRepository loggingRepository;

    public LoggingService(LoggingRepository loggingRepository) {
        this.loggingRepository = loggingRepository;
    }

    @EventListener
    void handleEmployeeEvent(UserLoggedInEvent event) {
        Log logEvent = new Log();
        logEvent.setUsername(event.getName());
        logEvent.setCreatedAt(new Date());
        logEvent.setAction("login");
        logEvent.setCenter("");
        logEvent.setPatientNumber("");
        logEvent.setInventoryId("");
        logEvent.setLocationLabel("");
        logEvent.setDetails("");
        logEvent.setType("");
        loggingRepository.save(logEvent);
    }
}
