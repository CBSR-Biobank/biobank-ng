package edu.ualberta.med.biobank.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import edu.ualberta.med.biobank.test.Factory;

@TestConfiguration
class TestContextConfig {

    @Bean
    public Factory factory() {
        return new Factory();
    }

}
