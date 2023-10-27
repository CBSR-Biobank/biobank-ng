package edu.ualberta.med.biobank.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingUtils {

    private static final Logger logger = LoggerFactory.getLogger(LoggingUtils.class);


    public static <T> String prettyPrintJson(T object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("exception in prettyPrintJson",  e);
        }
        return ">>>> could not convert to JSON";
    }
}
