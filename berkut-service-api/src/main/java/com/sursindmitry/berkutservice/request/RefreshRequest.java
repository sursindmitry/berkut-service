package com.sursindmitry.berkutservice.request;

/**
 * Запрос на обновление токена авторизации.
 *
 * @param refreshToken токен обновления
 */
public record RefreshRequest(
    String refreshToken
) {
}
