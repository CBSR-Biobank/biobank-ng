package edu.ualberta.med.biobank;

import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import edu.ualberta.med.biobank.config.RsaKeyProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.servlet.ServletContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class BiobankApplication {

    public static void main(String... args) {
        SpringApplication.run(BiobankApplication.class, args);
    }

    @Bean
    public OpenAPI openAPI(ServletContext servletContext) {
        Contact contact = new Contact();
        contact.setEmail("tech@biosample.ca");
        contact.setUrl("https://biosample.ca/");

        var mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
            .title("CBSR Biobank NG API")
            .version("1.0")
            .contact(contact)
            .description("This API exposes endpoints to manage Biobank information.")
            .license(mitLicense);

        Server server = new Server().url(servletContext.getContextPath());

        return new OpenAPI()
            .servers(List.of(server))
            .info(info)
            .components(
                new Components()
                    .addSecuritySchemes(
                        "bearer-key",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                    )
            );
    }
}
