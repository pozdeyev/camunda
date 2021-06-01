package ru.gbuac.camunda.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Pozdeyev Dmitry
 * @since 26.05.2021 17:09
 */
@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("CAMUNDA TEST API")
                .description("Camunda Specification")
                .version("v1.0.0"));
  }
}
