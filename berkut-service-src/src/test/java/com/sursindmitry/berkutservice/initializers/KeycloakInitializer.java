package com.sursindmitry.berkutservice.initializers;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class KeycloakInitializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String KEYCLOAK_IMAGE_NAME = "quay.io/keycloak/keycloak:26.0.0";

    @Container
    public static final KeycloakContainer container = new KeycloakContainer(KEYCLOAK_IMAGE_NAME)
        .withEnv("KEYCLOAK_ADMIN", "admin")
        .withEnv("KEYCLOAK_ADMIN_PASSWORD", "admin")
        .withRealmImportFile("/keycloak/realm-export.json");

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        container.start();

        String authServerUrl = String.format("http://localhost:%d", container.getHttpPort());

        String realm = "platform";
        String resource = "platform-service";
        String clientSecret = "**********";

        TestPropertyValues.of(
            // Spring Security OAuth2 Resource Server настройки
            "spring.security.oauth2.resourceserver.jwt.issuer-uri=" + authServerUrl + "/realms/" + realm,
            "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=" + authServerUrl + "/realms/" + realm + "/protocol/openid-connect/certs",

            // Spring Security OAuth2 Client настройки
            "spring.security.oauth2.client.provider.keycloak.issuer-uri=" + authServerUrl + "/realms/" + realm,
            "spring.security.oauth2.client.provider.keycloak.authorization-uri=" + authServerUrl + "/realms/" + realm + "/protocol/openid-connect/auth",
            "spring.security.oauth2.client.provider.keycloak.token-uri=" + authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token",
            "spring.security.oauth2.client.provider.keycloak.user-info-uri=" + authServerUrl + "/realms/" + realm + "/protocol/openid-connect/userinfo",
            "spring.security.oauth2.client.provider.keycloak.jwk-set-uri=" + authServerUrl + "/realms/" + realm + "/protocol/openid-connect/certs",

            // Keycloak настройки
            "keycloak.realm=" + realm,
            "keycloak.auth-server-url=" + authServerUrl,
            "keycloak.resource=" + resource,
            "keycloak.credentials.secret=" + clientSecret,
            "keycloak.use-resource-role-mappings=true",
            "keycloak.bearer-only=true",

            // Keycloak roles client настройки
            "keycloak.roles-client.client-id=" + resource,
            "keycloak.roles-client.client-secret=" + clientSecret
        ).applyTo(applicationContext.getEnvironment());
    }
}
