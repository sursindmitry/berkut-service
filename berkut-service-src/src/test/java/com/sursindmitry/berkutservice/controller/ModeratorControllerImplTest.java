package com.sursindmitry.berkutservice.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.sursindmitry.berkutservice.BaseIntegrationTest;
import jakarta.ws.rs.core.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ModeratorControllerImplTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Должен вернуть приветствие и идентификатор модератора")
    void shouldReturnModeratorId(){

        String message = webTestClient
            .get()
            .uri("/api/v1/moderator/hello")
            .header(HttpHeaders.AUTHORIZATION, authUtil.getAuthorization("moderator@moderator.ru"))
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .returnResult()
            .getResponseBody();

        assertThat(message).isEqualTo("Hello moderator 92bb9523-ce1d-4b4d-9ecd-c53b05e8def2");
    }

    @Test
    @DisplayName("Должен вернуть ошибку 401")
    void shouldReturn401(){
        webTestClient
            .get()
            .uri("api/v1/moderator/hello")
            .exchange()
            .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Должен вернуть ошибку 403 - ADMIN")
    void shouldReturn403Admin(){
        webTestClient
            .get()
            .uri("api/v1/moderator/hello")
            .header(HttpHeaders.AUTHORIZATION,authUtil.getAuthorization("admin@admin.ru"))
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Должен вернуть ошибку 403 - MANAGER")
    void shouldReturn403Manager(){
        webTestClient
            .get()
            .uri("api/v1/moderator/hello")
            .header(HttpHeaders.AUTHORIZATION,authUtil.getAuthorization("manager@manager.ru"))
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Должен вернуть ошибку 403 - USER")
    void shouldReturn403User(){
        webTestClient
            .get()
            .uri("api/v1/moderator/hello")
            .header(HttpHeaders.AUTHORIZATION,authUtil.getAuthorization("user@user.ru"))
            .exchange()
            .expectStatus().isForbidden();
    }
}