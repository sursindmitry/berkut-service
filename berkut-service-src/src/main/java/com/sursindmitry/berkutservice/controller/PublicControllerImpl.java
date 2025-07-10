package com.sursindmitry.berkutservice.controller;

import com.sursindmitry.berkutservice.api.PublicController;
import com.sursindmitry.berkutservice.request.LoginRequest;
import com.sursindmitry.berkutservice.request.RefreshRequest;
import com.sursindmitry.berkutservice.request.RegisterUserRequest;
import com.sursindmitry.berkutservice.response.TokenResponse;
import com.sursindmitry.berkutservice.service.AuthService;
import com.sursindmitry.berkutservice.service.RegisterService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Реализация публичного контроллера.
 */
@RestController
@RequiredArgsConstructor
public class PublicControllerImpl implements PublicController {

    private final RegisterService registerService;
    private final AuthService authService;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Публичный вход");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<UUID> register(RegisterUserRequest request) {
        UUID userId = registerService.register(request);

        return ResponseEntity.ok(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<TokenResponse> login(LoginRequest request) {

        TokenResponse token = authService.login(request);

        return ResponseEntity.ok(token);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<TokenResponse> refresh(RefreshRequest request) {

        TokenResponse token = authService.refresh(request);

        return ResponseEntity.ok(token);
    }
}
