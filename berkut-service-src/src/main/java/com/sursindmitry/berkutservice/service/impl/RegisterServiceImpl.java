package com.sursindmitry.berkutservice.service.impl;

import com.sursindmitry.berkutservice.request.RegisterUserRequest;
import com.sursindmitry.berkutservice.service.KeycloakService;
import com.sursindmitry.berkutservice.service.RegisterService;
import com.sursindmitry.berkutservice.service.UserService;
import com.sursindmitry.berkutservice.validation.UserValidation;
import com.sursindmitry.commonmodels.exception.ConflictException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса регистрации.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterServiceImpl implements RegisterService {

    private final UserService userService;
    private final KeycloakService keycloakService;

    private final UserValidation userValidation;

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID register(RegisterUserRequest request) {
        userValidation.validateUser(request);

        if (userService.existsByEmail(request.email())) {
            log.debug(
                "Пользователь с email {} уже существует",
                request.email()
            );
            throw new ConflictException("Email уже существует");
        }

        UUID userId = keycloakService.register(request);

        return userService.saveUserAfterRegistration(request, userId);
    }
}
