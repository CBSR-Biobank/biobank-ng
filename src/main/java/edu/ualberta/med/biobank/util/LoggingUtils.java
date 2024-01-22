package edu.ualberta.med.biobank.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingUtils {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(LoggingUtils.class);

    private LoggingUtils() {
        throw new AssertionError();
    }


    public static <T> String prettyPrintJson(T object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
            mapper.setSerializationInclusion(Include.NON_NULL);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("exception in prettyPrintJson",  e);
        }
        return "!!! ERROR: could not convert to JSON";
    }
}
