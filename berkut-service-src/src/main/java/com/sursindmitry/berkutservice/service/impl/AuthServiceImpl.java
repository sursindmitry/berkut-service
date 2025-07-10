package com.sursindmitry.berkutservice.service.impl;

import com.sursindmitry.berkutservice.request.LoginRequest;
import com.sursindmitry.berkutservice.request.RefreshRequest;
import com.sursindmitry.berkutservice.response.TokenResponse;
import com.sursindmitry.berkutservice.service.AuthService;
import com.sursindmitry.berkutservice.service.KeycloakService;
import com.sursindmitry.berkutservice.service.RefreshTokenService;
import com.sursindmitry.berkutservice.service.UserService;
import com.sursindmitry.berkutservice.validation.UserValidation;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса авторизации пользователей.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final UserValidation userValidation;
    private final KeycloakService keycloakService;
    private final RefreshTokenService refreshTokenService;

    /**
     * {@inheritDoc}
     */
    @Override
    public TokenResponse login(LoginRequest request) {
        userValidation.validateLogin(request);
        UUID userId = userService.getUserIdByEmail(request.email());

        TokenResponse tokenResponse = keycloakService.login(request.email(), request.password());

        refreshTokenService.save(
            userId,
            tokenResponse.refreshToken(),
            tokenResponse.refreshExpiresIn()
        );


        return tokenResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TokenResponse refresh(RefreshRequest request) {
        userValidation.validateRefresh(request);

        UUID userId = refreshTokenService.findUserIdByToken(request.refreshToken());

        TokenResponse tokenResponse = keycloakService.refresh(request.refreshToken());

        refreshTokenService.save(
            userId,
            tokenResponse.refreshToken(),
            tokenResponse.refreshExpiresIn()
        );

        return tokenResponse;
    }
}
