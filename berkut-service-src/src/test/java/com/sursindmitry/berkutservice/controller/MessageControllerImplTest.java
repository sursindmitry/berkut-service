package com.sursindmitry.berkutservice.controller;

import com.github.database.rider.core.api.dataset.CompareOperation;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.sursindmitry.berkutservice.BaseIntegrationTest;
import com.sursindmitry.berkutservice.request.MessageRequest;
import jakarta.ws.rs.core.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MessageControllerImplTest extends BaseIntegrationTest {

    @Test
    @DataSet(value = "db/api/v1/message/200/initial/initial.yml", cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet(value = "db/api/v1/message/200/expected/expected.yml", compareOperation = CompareOperation.CONTAINS)
    @DisplayName("Должен отправить письмо в бота телеграм(200)")
    void shouldSendMessageToTelegram() {
        MessageRequest request = new MessageRequest("Привет мир!");

        webTestClient
            .post()
            .uri("api/v1/message")
            .header(HttpHeaders.AUTHORIZATION, authUtil.getAuthorization("user@user.ru"))
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    @DisplayName("Должен получить ошибку валидации при пустом сообщении (400)")
    void shouldReturnValidationExceptionWhenSendMessage() {
        MessageRequest request = new MessageRequest("");

        webTestClient
            .post()
            .uri("api/v1/message")
            .header(HttpHeaders.AUTHORIZATION, authUtil.getAuthorization("user@user.ru"))
            .bodyValue(request)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.error").isEqualTo("Validation Error")
            .jsonPath("$.errors.['request']").isEqualTo("Запрос не может быть пустым")
            .jsonPath("$.timestamp").isNotEmpty()
            .jsonPath("$.status").isEqualTo(400);
    }

    @Test
    @DataSet(value = "db/api/v1/message/messages/200/initial/initial.yml", cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet(value = "db/api/v1/message/messages/200/expected/expected.yml", compareOperation = CompareOperation.CONTAINS)
    @DisplayName("Должен вернуть 3 сообщения у пользователя")
    void shouldReturnThreeMessages() {
        webTestClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .path("api/v1/message/messages")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .build()
            )
            .header(HttpHeaders.AUTHORIZATION, authUtil.getAuthorization("user@user.ru"))
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.content.length()").isEqualTo(3)
            .jsonPath("$.content.[0].message").isEqualTo("Сообщение №1")
            .jsonPath("$.content.[1].message").isEqualTo("Сообщение №2")
            .jsonPath("$.content.[2].message").isEqualTo("Сообщение №3");
    }

    @Test
    @DisplayName("Должен вернуть ошибку валидации если page и size не валидны (400)")
    void shouldReturnValidationExceptionWhenPageAndSizeInvalid() {
        webTestClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .path("api/v1/message/messages")
                .queryParam("page", -2)
                .queryParam("size", 500)
                .build()
            )
            .header(HttpHeaders.AUTHORIZATION, authUtil.getAuthorization("user@user.ru"))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.error").isEqualTo("Validation Error")
            .jsonPath("$.errors.['page']").isEqualTo("page не может быть меньше 0")
            .jsonPath("$.errors.['size']").isEqualTo("size не может быть меньше 1 или больше 100")
            .jsonPath("$.timestamp").isNotEmpty()
            .jsonPath("$.status").isEqualTo(400);
    }
}