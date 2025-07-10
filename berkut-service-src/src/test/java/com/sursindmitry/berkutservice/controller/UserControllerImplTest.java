package com.sursindmitry.berkutservice.controller;


import static org.assertj.core.api.Assertions.assertThat;

import com.github.database.rider.core.api.dataset.CompareOperation;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.sursindmitry.berkutservice.BaseIntegrationTest;
import jakarta.ws.rs.core.HttpHeaders;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserControllerImplTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Должен вернуть приветствие и идентификатор пользователя")
    void shouldReturnUserId(){

        String message = webTestClient
            .get()
            .uri("/api/v1/user/hello")
            .header(HttpHeaders.AUTHORIZATION, authUtil.getAuthorization("user@user.ru"))
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .returnResult()
            .getResponseBody();

        assertThat(message).isEqualTo("Hello user 32a3fef2-f97a-47f2-9026-a4f1b793456e");
    }

    @Test
    @DisplayName("Должен вернуть ошибку 401")
    void shouldReturn401(){
        webTestClient
            .get()
            .uri("api/v1/user/hello")
            .exchange()
            .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Должен вернуть ошибку 403 - ADMIN")
    void shouldReturn403Admin(){
        webTestClient
            .get()
            .uri("api/v1/user/hello")
            .header(HttpHeaders.AUTHORIZATION,authUtil.getAuthorization("admin@admin.ru"))
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Должен вернуть ошибку 403 - MODERATOR")
    void shouldReturn403Moderator(){
        webTestClient
            .get()
            .uri("api/v1/user/hello")
            .header(HttpHeaders.AUTHORIZATION,authUtil.getAuthorization("moderator@moderator.ru"))
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Должен вернуть ошибку 403 - MANAGER")
    void shouldReturn403Manager(){
        webTestClient
            .get()
            .uri("api/v1/user/hello")
            .header(HttpHeaders.AUTHORIZATION,authUtil.getAuthorization("manager@manager.ru"))
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Должен вернуть аккаунт пользователя с одной ролью")
    @DataSet(value = "db/api/v1/user/200/initial/initial.yml", cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet(value = "db/api/v1/user/200/expected/expected.yml", compareOperation = CompareOperation.CONTAINS)
    void shouldReturnUserProfileWithOneRole() {

        UUID id = UUID.fromString("1bd81f3f-61a1-49d8-ba04-84befea1e943");

        webTestClient
            .get()
            .uri("api/v1/user/" + id)
            .header(HttpHeaders.AUTHORIZATION, authUtil.getAuthorization("user@user.ru"))
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(id.toString())
            .jsonPath("$.firstName").isEqualTo("Dmitry")
            .jsonPath("$.lastName").isEqualTo("Sursin")
            .jsonPath("$.email").isEqualTo("sursindmitryi@gmail.com")
            .jsonPath("$.roles").value(roles ->
                assertThat((List<String>) roles)
                    .containsExactlyInAnyOrder("ROLE_USER")
            );
    }

    @Test
    @DisplayName("Должен вернуть аккаунт пользователя с двумя ролями")
    @DataSet(value = "db/api/v1/user/200/initial/initial1.yml", cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet(value = "db/api/v1/user/200/expected/expected1.yml", compareOperation = CompareOperation.CONTAINS)
    void shouldReturnUserProfileWithTwoRoles() {
        UUID id = UUID.fromString("11f29369-0366-4f20-a725-51e6cb780027");

        webTestClient
            .get()
            .uri("api/v1/user/" + id)
            .header(HttpHeaders.AUTHORIZATION, authUtil.getAuthorization("user@user.ru"))
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(id.toString())
            .jsonPath("$.firstName").isEqualTo("Dmitry")
            .jsonPath("$.lastName").isEqualTo("Sursin")
            .jsonPath("$.email").isEqualTo("sursindmitryi@gmail.com")
            .jsonPath("$.roles").value(roles ->
                assertThat((List<String>) roles)
                    .containsExactlyInAnyOrder("ROLE_USER", "ROLE_MANAGER")
            );
    }

    @Test
    @DisplayName("Должен получить Ошибку поиска (404)")
    void shouldReturnNotFoundWhenUserNotFound() {
        UUID id = UUID.randomUUID();

        webTestClient
            .get()
            .uri("api/v1/user/" + id)
            .header(HttpHeaders.AUTHORIZATION, authUtil.getAuthorization("user@user.ru"))
            .exchange()
            .expectStatus().isNotFound()
            .expectBody()
            .jsonPath("$.error").isEqualTo("Not Found Error")
            .jsonPath("$.errors.message").isEqualTo("Ошибка поиска")
            .jsonPath("$.status").isEqualTo(404);
    }
}