package com.sursindmitry.berkutservice.request;

/**
 * Запрос на авторизацию пользователя.
 *
 * @param email    электронная почта пользователя
 * @param password пароль пользователя
 */
public record LoginRequest(
    String email,
    String password
) {
}
