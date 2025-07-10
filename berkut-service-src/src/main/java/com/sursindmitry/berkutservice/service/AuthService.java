package com.sursindmitry.berkutservice.service;

import com.sursindmitry.berkutservice.request.LoginRequest;
import com.sursindmitry.berkutservice.request.RefreshRequest;
import com.sursindmitry.berkutservice.response.TokenResponse;

/**
 * Интерфейс авторизация пользователей.
 */
public interface AuthService {

    /**
     * Аутентификация пользователя.
     *
     * @param request {@link LoginRequest}
     * @return {@link TokenResponse}
     */
    TokenResponse login(LoginRequest request);

    /**
     * Обновление токена аутентификации.
     *
     * @param request {@link RefreshRequest}
     * @return {@link TokenResponse}
     */
    TokenResponse refresh(RefreshRequest request);
}
