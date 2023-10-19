package edu.ualberta.med.biobank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import edu.ualberta.med.biobank.config.RsaKeyProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class BiobankApplication {

  public static void main(String... args) {
    SpringApplication.run(BiobankApplication.class, args);
  }
}
