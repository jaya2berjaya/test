package com.arism.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SpringStore API")
                        .version("1.0.0")
                        .description("API dokumentasi untuk backend Spring Boot e-commerce.")
                        .contact(new Contact()
                                .name("Developer Backend")
                                .email("dev@example.com")
                                .url("https://github.com/username/springstore"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}