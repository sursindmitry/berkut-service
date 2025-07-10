package com.sursindmitry.berkutservice.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Ответ сервера от Keycloak при успешной авторизации.
 *
 * @param accessToken      токен аутентификации
 * @param refreshToken     токен для получения нового токена аутентификации
 * @param expiresIn        время жизни токена аутентификации
 * @param refreshExpiresIn время жизни refresh токена
 * @param tokenType        тип токена
 * @param scope            область действия токена
 */
public record TokenResponse(
    @JsonProperty("access_token")
    String accessToken,

    @JsonProperty("refresh_token")
    String refreshToken,

    @JsonProperty("expires_in")
    Long expiresIn,

    @JsonProperty("refresh_expires_in")
    Long refreshExpiresIn,

    @JsonProperty("token_type")
    String tokenType,

    @JsonProperty("scope")
    String scope
) {
}
