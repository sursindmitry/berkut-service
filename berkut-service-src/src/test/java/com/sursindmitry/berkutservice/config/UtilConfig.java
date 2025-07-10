package com.sursindmitry.berkutservice.config;

import com.sursindmitry.berkutservice.initializers.KeycloakInitializer;
import com.sursindmitry.berkutservice.util.AuthUtil;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class UtilConfig {
    private static final String REALM = "platform";
    private static final String CLIENT_ID = "platform-service";
    private static final String CLIENT_SECRET = "**********";
    private static final String DEFAULT_PASSWORD = "password";

    @Bean
    public AuthUtil authUtil() {
        String keycloakUrl = KeycloakInitializer.container.getAuthServerUrl();
        return new AuthUtil(keycloakUrl, REALM, CLIENT_ID, CLIENT_SECRET, DEFAULT_PASSWORD);
    }
}
