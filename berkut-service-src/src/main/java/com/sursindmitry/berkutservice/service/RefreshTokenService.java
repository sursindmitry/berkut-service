package com.sursindmitry.berkutservice.service;

import java.util.UUID;

/**
 * Сервис для работы с токенами обновления.
 */
public interface RefreshTokenService {

    /**
     * Сохраняет токен обновления.
     *
     * @param userId идентификатор пользователя
     * @param refreshToken токен обновления
     * @param refreshTokenExpiresIn время жизни токена обновления
     */
    void save(UUID userId, String refreshToken, Long refreshTokenExpiresIn);

    /**
     * Находит идентификатор пользователя используя токен обновления.
     *
     * @param token токен обновления
     * @return идентификатор пользователя
     */
    UUID findUserIdByToken(String token);
}
