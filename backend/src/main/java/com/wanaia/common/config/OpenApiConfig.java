package com.wanaia.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI wanaiaOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("WANAIA — Global Mobility Intelligence Platform API")
                .description("Versioned REST API v1 providing vehicle intelligence, decision scoring, marketplace listings, provenance tracking, and mobility advisory.")
                .version("v1.0.0")
                .contact(new Contact().name("WANAIA Engineering").email("api@wanaia.com"))
                .license(new License().name("Proprietary").url("https://wanaia.com/terms")))
            .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
            .components(new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME,
                    new SecurityScheme()
                        .name(SECURITY_SCHEME_NAME)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }
}
