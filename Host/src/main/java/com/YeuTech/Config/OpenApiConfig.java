package com.YeuTech.Config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI marketingOpenAPI() {
                SecurityScheme bearerScheme = new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Provide JWT access token in format: Bearer <token>");

                return new OpenAPI()
                                .info(new Info()
                                                .title("Marketing Backend API")
                                                .version("v1")
                                                .description("REST API documentation for authentication, user management, and password reset flows."))
                                .components(new Components().addSecuritySchemes("bearerAuth", bearerScheme))
                                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement()
                                                .addList("bearerAuth"));
        }

        @Bean
        public GroupedOpenApi publicV1Api() {
                return GroupedOpenApi.builder()
                                .group("v1")
                                .pathsToMatch("/v1/api/**")
                                .build();
        }
}
