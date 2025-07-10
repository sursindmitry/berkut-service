package com.sursindmitry.berkutservice.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.database.rider.core.api.dataset.CompareOperation;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.sursindmitry.berkutservice.BaseIntegrationTest;
import com.sursindmitry.berkutservice.entity.redis.RefreshToken;
import com.sursindmitry.berkutservice.repository.redis.RefreshTokenRepository;
import com.sursindmitry.berkutservice.request.LoginRequest;
import com.sursindmitry.berkutservice.request.RefreshRequest;
import com.sursindmitry.berkutservice.request.RegisterUserRequest;
import jakarta.ws.rs.core.HttpHeaders;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

class PublicControllerImplTest extends BaseIntegrationTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @ParameterizedTest(name = "Должен дать доступ пользователю с email={0} (200)")
    @ValueSource(strings = {
        "user@user.ru",
        "admin@admin.ru",
        "moderator@moderator.ru",
        "manager@manager.ru"
    })
    void shouldReturnAccessForDifferentUsers(String email) {
        String message = webTestClient
            .get()
            .uri("/api/v1/public/hello")
            .header(HttpHeaders.AUTHORIZATION, authUtil.getAuthorization(email))
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .returnResult()
            .getResponseBody();

        assertThat(message).isEqualTo("Публичный вход");
    }

    @Test
    @DisplayName("Должен дать доступ незарегистрированному пользователю (200)")
    void shouldReturn401() {

        String message = webTestClient
            .get()
            .uri("api/v1/public/hello")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .returnResult()
            .getResponseBody();

        assertThat(message).isEqualTo("Публичный вход");
    }

    @Test
    @DisplayName("Должен зарегистрировать пользователя (200)")
    @DataSet(value = "db/api/v1/public/register/200/initial/initial.yml", cleanAfter = true, cleanBefore = true)
    @ExpectedDataSet(value = "db/api/v1/public/register/200/expected/expected.yml", compareOperation = CompareOperation.CONTAINS)
    void shouldRegisterUser() {

        RegisterUserRequest request = jsonParserUtil.getObjectFromJson(
            "json/api/v1/public/register/200/request/request.json",
            RegisterUserRequest.class
        );

        webTestClient
            .post()
            .uri("/api/v1/public/register")
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk()
            .expectBody(UUID.class);
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource({
        "'Должен не пройти валидацию если не заполнено имя (400)', json/api/v1/public/register/400/request/request1.json, user.firstName.invalid, 'Поле \"Имя\" не заполнено'",
        "'Должен не пройти валидацию если имя null (400)', json/api/v1/public/register/400/request/request2.json, user.firstName.invalid, 'Поле \"Имя\" не может быть пустым'",
        "'Должен не пройти валидацию если имя короче 2-х символов (400)', json/api/v1/public/register/400/request/request3.json, user.firstName.invalid, 'Поле \"Имя\" должно содержать не менее 2 символа'",
        "'Должен не пройти валидацию если не заполнено фамилия (400)', json/api/v1/public/register/400/request/request4.json, user.lastName.invalid, 'Поле \"Фамилия\" не заполнено'",
        "'Должен не пройти валидацию если фамилия null (400)', json/api/v1/public/register/400/request/request5.json, user.lastName.invalid, 'Поле \"Фамилия\" не может быть пустым'",
        "'Должен не пройти валидацию если фамилия короче 2-х символов (400)', json/api/v1/public/register/400/request/request6.json, user.lastName.invalid, 'Поле \"Фамилия\" должно содержать не менее 2 символа'",
        "'Должен не пройти валидацию если не заполнена электронная почта (400)', json/api/v1/public/register/400/request/request7.json, user.email.invalid, 'Поле \"Email\" не заполнено'",
        "'Должен не пройти валидацию если электронная почта null (400)', json/api/v1/public/register/400/request/request8.json, user.email.invalid, 'Поле \"Email\" не может быть пустым'",
        "'Должен не пройти валидацию если электронная почта короче 4-х символов (400)', json/api/v1/public/register/400/request/request9.json, user.email.invalid, 'Поле \"Email\" должно содержать не менее 4 символа'",
        "'Должен не пройти валидацию если не заполнен пароль (400)', json/api/v1/public/register/400/request/request13.json, user.password.invalid, 'Поле \"Пароль\" не заполнено'",
        "'Должен не пройти валидацию если пароль null (400)', json/api/v1/public/register/400/request/request14.json, user.password.invalid, 'Поле \"Пароль\" не может быть пустым'",
        "'Должен не пройти валидацию если пароль короче 6 символов (400)', json/api/v1/public/register/400/request/request15.json, user.password.invalid, 'Поле \"Пароль\" должно содержать не менее 6 символов'",
    })
    void shouldFailValidation(
        String displayName,
        String jsonPath,
        String error,
        String expectedMessage
    ) {
        RegisterUserRequest request =
            jsonParserUtil.getObjectFromJson(jsonPath, RegisterUserRequest.class);

        webTestClient
            .post()
            .uri("/api/v1/public/register")
            .bodyValue(request)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.error").isEqualTo("Validation Error")
            .jsonPath("$.errors.['" + error + "']").isEqualTo(expectedMessage)
            .jsonPath("$.timestamp").isNotEmpty()
            .jsonPath("$.status").isEqualTo(400);
    }

    @Test
    @DisplayName("Должен не пройти валидацию если имя длиннее 255 символов (400)")
    void shouldFailValidationIfFirstNameIsGreaterThan255() {
        RegisterUserRequest request = new RegisterUserRequest(
            RandomStringUtils.randomAlphabetic(256),
            "Sursin",
            "sursindmitryi@gmail.com",
            "123456"
        );

        webTestClient
            .post()
            .uri("/api/v1/public/register")
            .bodyValue(request)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.error").isEqualTo("Validation Error")
            .jsonPath("$.errors.['user.firstName.invalid']")
            .isEqualTo("Поле \"Имя\" должно содержать не более 255 символов")
            .jsonPath("$.timestamp").isNotEmpty()
            .jsonPath("$.status").isEqualTo(400);
    }

    @Test
    @DisplayName("Должен не пройти валидацию если электронная почта длиннее 255 символов (400)")
    void shouldFailValidationIfEmailIsGreaterThan255() {
        RegisterUserRequest request = new RegisterUserRequest(
            "Dmitry",
            "Sursin",
            RandomStringUtils.randomAlphabetic(256),
            "123456"
        );

        webTestClient
            .post()
            .uri("/api/v1/public/register")
            .bodyValue(request)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.error").isEqualTo("Validation Error")
            .jsonPath("$.errors.['user.email.invalid']")
            .isEqualTo("Поле \"Email\" должно содержать не более 255 символов")
            .jsonPath("$.timestamp").isNotEmpty()
            .jsonPath("$.status").isEqualTo(400);
    }

    @Test
    @DisplayName("Должен не пройти валидацию если пароль длиннее 255 символов (400)")
    void shouldFailValidationIfPasswordIsGreaterThan255() {
        RegisterUserRequest request = new RegisterUserRequest(
            "Dmitry",
            "Sursin",
            "sursindmitryi@gmail.com",
            RandomStringUtils.randomAlphabetic(256)
        );

        webTestClient
            .post()
            .uri("/api/v1/public/register")
            .bodyValue(request)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.error").isEqualTo("Validation Error")
            .jsonPath("$.errors.['user.password.invalid']")
            .isEqualTo("Поле \"Пароль\" должно содержать не более 255 символов")
            .jsonPath("$.timestamp").isNotEmpty()
            .jsonPath("$.status").isEqualTo(400);
    }


    @Test
    @DisplayName("Должен не пройти валидацию если фамилия длиннее 255 символов (400)")
    void shouldFailValidationIfLastNameIsGreaterThan255() {
        RegisterUserRequest request = new RegisterUserRequest(
            "Dmitry",
            RandomStringUtils.randomAlphabetic(256),
            "sursindmitryi@gmail.com",
            "123456"
        );

        webTestClient
            .post()
            .uri("/api/v1/public/register")
            .bodyValue(request)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.error").isEqualTo("Validation Error")
            .jsonPath("$.errors.['user.lastName.invalid']")
            .isEqualTo("Поле \"Фамилия\" должно содержать не более 255 символов")
            .jsonPath("$.timestamp").isNotEmpty()
            .jsonPath("$.status").isEqualTo(400);
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource({
        "'Должен не дать повторно зарегистрировать одного и того же пользователя с одинаковым email (409)', json/api/v1/public/register/409/request/request1.json",
    })
    @DataSet(value = "db/api/v1/public/register/409/initial/initial.yml", cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet(value = "db/api/v1/public/register/409/expected/expected.yml", compareOperation = CompareOperation.CONTAINS)
    void shouldRejectRepeatedUserRegistration(String displayName, String jsonPath) {
        RegisterUserRequest request = jsonParserUtil.getObjectFromJson(
            jsonPath,
            RegisterUserRequest.class
        );

        webTestClient
            .post()
            .uri("api/v1/public/register")
            .bodyValue(request)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CONFLICT.value())
            .expectBody()
            .jsonPath("$.error").isEqualTo("Conflict Error")
            .jsonPath("$.errors['message']").isEqualTo("Email уже существует")
            .jsonPath("$.timestamp").isNotEmpty()
            .jsonPath("$.status").isEqualTo(409);
    }

    @ParameterizedTest(name = "Должен не дать зарегистрированному {0} повторно зарегистрироваться (403)")
    @ValueSource(strings = {
        "user@user.ru",
        "admin@admin.ru",
        "moderator@moderator.ru",
        "manager@manager.ru"
    })
    void shouldNotAllowDuplicateUserRegistration(String email) {
        RegisterUserRequest request = jsonParserUtil.getObjectFromJson(
            "json/api/v1/public/register/403/request/request.json",
            RegisterUserRequest.class
        );

        webTestClient
            .post()
            .uri("/api/v1/public/register")
            .header(HttpHeaders.AUTHORIZATION, authUtil.getAuthorization(email))
            .bodyValue(request)
            .exchange()
            .expectStatus().isForbidden()
            .expectBody()
            .jsonPath("$.error").isEqualTo("Access Denied Error")
            .jsonPath("$.errors.message")
            .isEqualTo("Вы уже вошли в систему и не можете это использовать")
            .jsonPath("$.status").isEqualTo(403);
    }

    @Test
    @DisplayName("Должен получить ответ аутентификации для пользователя (200)")
    @DataSet(value = "db/api/v1/public/login/200/initial/initial.yml", cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet(value = "db/api/v1/public/login/200/expected/expected.yml", compareOperation = CompareOperation.CONTAINS)
    void shouldGetAuthenticationResponseForUser() {
        LoginRequest request = jsonParserUtil.getObjectFromJson(
            "json/api/v1/public/login/200/request/request.json",
            LoginRequest.class
        );

        byte[] responseBody = webTestClient
            .post()
            .uri("/api/v1/public/login")
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.access_token").isNotEmpty()
            .jsonPath("$.refresh_token").isNotEmpty()
            .jsonPath("$.expires_in").isNotEmpty()
            .jsonPath("$.refresh_expires_in").isNotEmpty()
            .jsonPath("$.token_type").isEqualTo("Bearer")
            .jsonPath("$.scope").isEqualTo("profile email")
            .returnResult()
            .getResponseBody();

        // Проверка Redis
        String refreshToken = jsonParserUtil.extractFieldFromJson(responseBody, "refresh_token");
        String redisKey = "32a3fef2-f97a-47f2-9026-a4f1b793456e";
        RefreshToken redisEntry = refreshTokenRepository.findById(redisKey).orElse(null);

        assertNotNull(redisEntry);
        assertEquals(redisKey, redisEntry.getId());
        assertEquals(refreshToken, redisEntry.getToken());
        assertEquals(1800, redisEntry.getExpiration());
    }

    @Test
    @DisplayName("Должен получить ответ аутентификации для менеджера (200)")
    @DataSet(value = "db/api/v1/public/login/200/initial/initial1.yml", cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet(value = "db/api/v1/public/login/200/expected/expected1.yml", compareOperation = CompareOperation.CONTAINS)
    void shouldGetAuthenticationResponseForManager() {
        LoginRequest request = jsonParserUtil.getObjectFromJson(
            "json/api/v1/public/login/200/request/request2.json",
            LoginRequest.class
        );

        byte[] responseBody = webTestClient
            .post()
            .uri("/api/v1/public/login")
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.access_token").isNotEmpty()
            .jsonPath("$.refresh_token").isNotEmpty()
            .jsonPath("$.expires_in").isNotEmpty()
            .jsonPath("$.refresh_expires_in").isNotEmpty()
            .jsonPath("$.token_type").isEqualTo("Bearer")
            .jsonPath("$.scope").isEqualTo("profile email")
            .returnResult()
            .getResponseBody();

        // Проверка Redis
        String refreshToken = jsonParserUtil.extractFieldFromJson(responseBody, "refresh_token");
        String redisKey = "8a245a5b-9772-45e7-996b-14724aaad2b9";
        RefreshToken redisEntry = refreshTokenRepository.findById(redisKey).orElse(null);

        assertNotNull(redisEntry);
        assertEquals(redisKey, redisEntry.getId());
        assertEquals(refreshToken, redisEntry.getToken());
        assertEquals(1800, redisEntry.getExpiration());
    }

    @Test
    @DisplayName("Должен вернуть ошибку валидации при коротком почтовом адресе (400)")
    void shouldReturnBadRequestWhenEmailIsShort() {
        LoginRequest request = new LoginRequest(
            "use",
            RandomStringUtils.randomAlphabetic(7)
        );

        webTestClient
            .post()
            .uri("/api/v1/public/login")
            .bodyValue(request)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.error").isEqualTo("Validation Error")
            .jsonPath("$.errors['user.email.invalid']")
            .isEqualTo("Поле \"Email\" должно содержать не менее 4 символа")
            .jsonPath("$.status").isEqualTo("400");
    }

    @Test
    @DisplayName("Должен вернуть ошибку валидации при длинном почтовом адресе (400)")
    void shouldReturnBadRequestWhenEmailIsLong() {
        LoginRequest request = new LoginRequest(
            RandomStringUtils.randomAlphabetic(256),
            RandomStringUtils.randomAlphabetic(7)
        );

        webTestClient
            .post()
            .uri("/api/v1/public/login")
            .bodyValue(request)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.error").isEqualTo("Validation Error")
            .jsonPath("$.errors['user.email.invalid']")
            .isEqualTo("Поле \"Email\" должно содержать не более 255 символов")
            .jsonPath("$.status").isEqualTo("400");
    }

    @Test
    @DisplayName("Должен вернуть ошибку валидации при коротком пароле (400)")
    void shouldReturnBadRequestWhenPasswordIsShort() {
        LoginRequest request = new LoginRequest(
            "user@user.ru",
            RandomStringUtils.randomAlphabetic(5)
        );

        webTestClient
            .post()
            .uri("/api/v1/public/login")
            .bodyValue(request)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.error").isEqualTo("Validation Error")
            .jsonPath("$.errors['user.password.invalid']")
            .isEqualTo("Поле \"Пароль\" должно содержать не менее 6 символов")
            .jsonPath("$.status").isEqualTo("400");
    }

    @Test
    @DisplayName("Должен вернуть ошибку валидации при длинном пароле (400)")
    void shouldReturnBadRequestWhenPasswordIsLong() {
        LoginRequest request = new LoginRequest(
            "user@user.ru",
            RandomStringUtils.randomAlphabetic(256)
        );

        webTestClient
            .post()
            .uri("/api/v1/public/login")
            .bodyValue(request)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.error").isEqualTo("Validation Error")
            .jsonPath("$.errors['user.password.invalid']")
            .isEqualTo("Поле \"Пароль\" должно содержать не более 255 символов")
            .jsonPath("$.status").isEqualTo("400");
    }

    @Test
    @DisplayName("Должен вернуть новый токен (200)")
    @DataSet(value = "db/api/v1/public/refresh/200/initial/initial.yml", cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet(value = "db/api/v1/public/refresh/200/expected/expected.yml", compareOperation = CompareOperation.CONTAINS)
    void shouldReturnNewToken() {
        LoginRequest request = jsonParserUtil.getObjectFromJson(
            "json/api/v1/public/refresh/200/request/request.json",
            LoginRequest.class
        );

        // Аутентификация пользователя
        byte[] responseBody = webTestClient
            .post()
            .uri("/api/v1/public/login")
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.access_token").isNotEmpty()
            .jsonPath("$.refresh_token").isNotEmpty()
            .jsonPath("$.expires_in").isNotEmpty()
            .jsonPath("$.refresh_expires_in").isNotEmpty()
            .jsonPath("$.token_type").isEqualTo("Bearer")
            .jsonPath("$.scope").isEqualTo("profile email")
            .returnResult()
            .getResponseBody();

        String refreshToken = jsonParserUtil.extractFieldFromJson(responseBody, "refresh_token");

        RefreshRequest refreshRequest = new RefreshRequest(refreshToken);

        // Обновление токена
        webTestClient
            .post()
            .uri("/api/v1/public/refresh")
            .bodyValue(refreshRequest)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.access_token").isNotEmpty()
            .jsonPath("$.refresh_token").isNotEmpty()
            .jsonPath("$.expires_in").isNotEmpty()
            .jsonPath("$.refresh_expires_in").isNotEmpty()
            .jsonPath("$.token_type").isEqualTo("Bearer")
            .jsonPath("$.scope").isEqualTo("profile email");
    }

    @Test
    @DisplayName("Должен вернуть ошибку Not Found error при поиске токена в Redis (404)")
    void shouldReturnNotFoundWhenTokenNotFound() {
        RefreshRequest request = jsonParserUtil.getObjectFromJson(
            "json/api/v1/public/refresh/404/request/request.json",
            RefreshRequest.class
        );

        webTestClient
            .post()
            .uri("/api/v1/public/refresh")
            .bodyValue(request)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody()
            .jsonPath("$.error").isEqualTo("Not Found Error")
            .jsonPath("$.errors['message']").isEqualTo("Пользователь с таким токеном не найден")
            .jsonPath("$.status").isEqualTo("404");
    }
}