package com.sursindmitry.berkutservice.service;

import com.sursindmitry.berkutservice.request.RegisterUserRequest;
import com.sursindmitry.berkutservice.response.TokenResponse;
import java.util.List;
import java.util.UUID;
import org.keycloak.representations.idm.RoleRepresentation;

/**
 * Интерфейс для работы с Keycloak.
 */
public interface KeycloakService {

    /**
     * Находит все роли в Keycloak.
     *
     * @return список ролей из Keycloak
     */
    List<RoleRepresentation> findAllRoles();

    /**
     * Создаёт нового пользователя в Keycloak.
     *
     * @param request данные пользователя для Keycloak
     * @return идентификатор пользователя
     */
    UUID register(RegisterUserRequest request);

    /**
     * Аутентификация пользователя.
     *
     * @param email    адрес электронной почты
     * @param password пароль
     * @return {@link TokenResponse}
     */
    TokenResponse login(String email, String password);

    /**
     * Обновляет токен аутентификации используя токен обновления.
     *
     * @param token токен обновления
     * @return {@link TokenResponse}
     */
    TokenResponse refresh(String token);
}
