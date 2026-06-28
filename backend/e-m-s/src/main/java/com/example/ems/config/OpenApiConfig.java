package com.example.ems.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("EMS API")
                .description("API documentation for the EMS application")
                .version("1.0.0")
                .contact(new Contact().name("Muhammed Abdelghany"))

        ).addServersItem(new Server().url("http://localhost:9090"));
    }

}
