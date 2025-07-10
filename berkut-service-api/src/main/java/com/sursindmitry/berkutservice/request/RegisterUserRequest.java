package com.sursindmitry.berkutservice.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Запрос регистрации пользователя.
 *
 * @param firstName Имя
 * @param lastName  Фамилия
 * @param email     Почтовый адрес
 * @param password  Пароль
 */
@Schema(description = "Запрос на регистрацию нового пользователя")
public record RegisterUserRequest(
    @Schema(
        description = "Имя пользователя",
        example = "Иван"
    )
    String firstName,

    @Schema(
        description = "Фамилия пользователя",
        example = "Иванов"
    )
    String lastName,

    @Schema(
        description = "Email пользователя",
        example = "user@example.com"
    )
    String email,

    @Schema(
        description = "Пароль пользователя",
        example = "password123"
    )
    String password) {
}
