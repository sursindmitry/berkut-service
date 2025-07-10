package com.sursindmitry.berkutservice.controller;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.sursindmitry.berkutservice.BaseIntegrationTest;
import jakarta.ws.rs.core.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ManagerControllerImplTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Должен вернуть приветствие и идентификатор менеджера")
    void shouldReturnManagerId(){

        String message = webTestClient
            .get()
            .uri("/api/v1/manager/hello")
            .header(HttpHeaders.AUTHORIZATION, authUtil.getAuthorization("manager@manager.ru"))
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .returnResult()
            .getResponseBody();

        assertThat(message).isEqualTo("Hello manager 8a245a5b-9772-45e7-996b-14724aaad2b9");
    }

    @Test
    @DisplayName("Должен вернуть ошибку 401")
    void shouldReturn401(){
        webTestClient
            .get()
            .uri("api/v1/manager/hello")
            .exchange()
            .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Должен вернуть ошибку 403 - ADMIN")
    void shouldReturn403Admin(){
        webTestClient
            .get()
            .uri("api/v1/manager/hello")
            .header(HttpHeaders.AUTHORIZATION,authUtil.getAuthorization("admin@admin.ru"))
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Должен вернуть ошибку 403 - MODERATOR")
    void shouldReturn403Moderator(){
        webTestClient
            .get()
            .uri("api/v1/manager/hello")
            .header(HttpHeaders.AUTHORIZATION,authUtil.getAuthorization("moderator@moderator.ru"))
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Должен вернуть ошибку 403 - USER")
    void shouldReturn403User(){
        webTestClient
            .get()
            .uri("api/v1/manager/hello")
            .header(HttpHeaders.AUTHORIZATION,authUtil.getAuthorization("user@user.ru"))
            .exchange()
            .expectStatus().isForbidden();
    }
}