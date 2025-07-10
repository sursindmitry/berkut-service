package com.sursindmitry.berkutservice.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация клиента Keycloak Admin.
 * Создает и настраивает клиент для взаимодействия с API Keycloak.
 */
@Configuration
@RequiredArgsConstructor
public class KeycloakConfig {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.roles-client.client-id}")
    private String rolesClientId;

    @Value("${keycloak.roles-client.client-secret}")
    private String rolesClientSecret;

    /**
     * Создает и настраивает клиент Keycloak Admin.
     * Использует Service Account клиента с ограниченными правами для доступа к ролям.
     *
     * @return настроенный клиент Keycloak Admin
     */
    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
            .serverUrl(authServerUrl)
            .realm(realm)
            .clientId(rolesClientId)
            .clientSecret(rolesClientSecret)
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .build();
    }
}
