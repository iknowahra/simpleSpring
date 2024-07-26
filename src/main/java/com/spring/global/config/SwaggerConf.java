package com.spring.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!prod")
@Configuration// 운영환경에서는 Swagger 비활성화
public class SwaggerConf {
    private final String securitySchemeName = "Bearer Authentication";

    @Bean
    public OpenAPI OpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(components())
                .info(apiInfo());
    }

    private Components components() {
        return new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));
    }

    private Info apiInfo() {
        return new Info()
                .title("API 명세서")
                .description("SpringDoc을 사용한 Swagger UI")
                .version("1.0.0");
    }

}
