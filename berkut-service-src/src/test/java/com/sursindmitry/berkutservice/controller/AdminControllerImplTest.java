package com.sursindmitry.berkutservice.controller;


import static org.assertj.core.api.Assertions.assertThat;

import com.sursindmitry.berkutservice.BaseIntegrationTest;
import jakarta.ws.rs.core.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AdminControllerImplTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Должен вернуть приветствие и id админа")
    void shouldReturnAdminId() {

        String message = webTestClient
            .get()
            .uri("/api/v1/admin/hello")
            .header(HttpHeaders.AUTHORIZATION, authUtil.getAuthorization("admin@admin.ru"))
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .returnResult()
            .getResponseBody();

        assertThat(message).isEqualTo("Hello admin 779902ed-434e-4747-a96b-eb7984861f52");
    }

    @Test
    @DisplayName("Должен вернуть ошибку 401")
    void shouldReturn401() {
        webTestClient
            .get()
            .uri("api/v1/admin/hello")
            .exchange()
            .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Должен вернуть ошибку 403 - USER")
    void shouldReturn403User() {
        webTestClient
            .get()
            .uri("api/v1/admin/hello")
            .header(HttpHeaders.AUTHORIZATION, authUtil.getAuthorization("user@user.ru"))
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Должен вернуть ошибку 403 - MODERATOR")
    void shouldReturn403Moderator() {
        webTestClient
            .get()
            .uri("api/v1/admin/hello")
            .header(HttpHeaders.AUTHORIZATION, authUtil.getAuthorization("moderator@moderator.ru"))
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Должен вернуть ошибку 403 - MANAGER")
    void shouldReturn403Manager() {
        webTestClient
            .get()
            .uri("api/v1/admin/hello")
            .header(HttpHeaders.AUTHORIZATION, authUtil.getAuthorization("manager@manager.ru"))
            .exchange()
            .expectStatus().isForbidden();
    }

}