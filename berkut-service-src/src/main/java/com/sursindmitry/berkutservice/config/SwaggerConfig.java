package com.sursindmitry.berkutservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger configuration.
 *
 * <p>
 * Класс конфигурации свагера
 * </p>
 */
@Configuration
public class SwaggerConfig {
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    /**
     * Настройка OpenAPI.
     *
     * @return {@link OpenAPI}
     */
    @Bean
    public OpenAPI apiInfo() {
        final String securitySchemeName = "oauth2";

        Info apiInfo = new Info()
            .title("User Service REST API")
            .description(
                "API for user-service."
                    + "Сервис для управления пользователями, аутентификация, авторизация.")
            .version("0.0.1");

        Server localServer = new Server()
            .description("Локальное окружение")
            .url("http://localhost:9191");

        return new OpenAPI()
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(new Components()
                .addSecuritySchemes(securitySchemeName,
                    new SecurityScheme()
                        .type(SecurityScheme.Type.OAUTH2)
                        .flows(new OAuthFlows()
                            .authorizationCode(new OAuthFlow()
                                .authorizationUrl(issuerUri + "/protocol/openid-connect/auth")
                                .tokenUrl(issuerUri + "/protocol/openid-connect/token")
                                .refreshUrl(issuerUri + "/protocol/openid-connect/token")))))
            .info(apiInfo)
            .servers(List.of(localServer));
    }
}
