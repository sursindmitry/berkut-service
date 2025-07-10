package com.sursindmitry.berkutservice.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.sursindmitry.berkutservice.BaseIntegrationTest;
import org.junit.jupiter.api.Test;

class TestControllerImplTest extends BaseIntegrationTest {


    @Test
    void shouldReturnIWork() {

        String message = webTestClient
            .get()
            .uri("/api/v1/public/hello")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .returnResult()
            .getResponseBody();

        assertThat(message).isEqualTo("Публичный вход");
    }

}