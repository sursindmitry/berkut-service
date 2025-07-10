package com.sursindmitry.berkutservice.service.impl;

import com.sursindmitry.berkutservice.constant.RoleConstants;
import com.sursindmitry.berkutservice.mapper.KeycloakMapper;
import com.sursindmitry.berkutservice.request.RegisterUserRequest;
import com.sursindmitry.berkutservice.response.TokenResponse;
import com.sursindmitry.berkutservice.service.KeycloakService;
import com.sursindmitry.commonmodels.exception.ConflictException;
import com.sursindmitry.commonmodels.exception.PlatformException;
import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Реализация сервиса для работы с Keycloak.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakServiceImpl implements KeycloakService {

    private final WebClient webClient;

    private final Keycloak keycloak;

    private final KeycloakMapper keycloakMapper;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.roles-client.client-id}")
    private String clientId;

    @Value("${keycloak.roles-client.client-secret}")
    private String clientSecret;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RoleRepresentation> findAllRoles() {
        log.debug("Поиск всех ролей в Keycloak");

        try {
            RealmResource resource = keycloak.realm(realm);
            RolesResource rolesResource = resource.roles();

            List<RoleRepresentation> roles = rolesResource.list();

            log.debug("Получение {} ролей из Keycloak: {}",
                roles.size(),
                roles.stream()
                    .map(RoleRepresentation::getName)
                    .toList()
            );

            return roles;
        } catch (Exception e) {
            log.error("Ошибка при получении ролей из Keycloak: {}", e.getMessage(), e);

            throw new PlatformException("Ошибка при получении ролей из Keycloak: "
                + e.getMessage(),
                e
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID register(RegisterUserRequest request) {
        Objects.requireNonNull(request, "Запрос на создание пользователя не может быть null");

        String userDisplayName = request.firstName() + " " + request.lastName();
        log.debug("Создание пользователя для Keycloak: {} ({})", request.email(), userDisplayName);

        UserRepresentation userRepresentation = keycloakMapper.userRequestToUserRepresentation(
            request
        );

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource userResource = realmResource.users();
        RolesResource rolesResource = realmResource.roles();

        RoleRepresentation roleRepresentation = rolesResource
            .get(RoleConstants.USER)
            .toRepresentation();

        Response response = userResource.create(userRepresentation);

        UUID userId = handleKeycloakResponse(response, request.email(), userDisplayName);

        UserResource createdUserResource = userResource.get(userId.toString());

        createdUserResource.roles()
            .realmLevel()
            .add(Collections.singletonList(roleRepresentation));

        log.debug(
            "Назначена роль {} пользователю {} ({})",
            RoleConstants.USER,
            request.email(),
            userId
        );

        return userId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TokenResponse login(String email, String password) {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", email);
        formData.add("password", password);

        String tokenUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        return webClient.post()
            .uri(tokenUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(formData))
            .retrieve()
            .bodyToMono(TokenResponse.class)
            .block();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TokenResponse refresh(String token) {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("refresh_token", token);
        formData.add("client_secret", clientSecret);
        formData.add("client_id", clientId);


        String tokenUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        return webClient.post()
            .uri(tokenUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(formData))
            .retrieve()
            .bodyToMono(TokenResponse.class)
            .block();
    }


    /**
     * Обрабатывает ответ от Keycloak при создании пользователя.
     *
     * @param response        ответ от keycloak
     * @param email           email пользователя
     * @param userDisplayName отображаемое имя пользователя
     * @return идентификатор пользователя
     */
    private UUID handleKeycloakResponse(Response response, String email, String userDisplayName) {
        Objects.requireNonNull(response, "Ответ от Keycloak не может быть null");
        Objects.requireNonNull(email, "Email пользователя не может быть null");

        int status = response.getStatus();

        if (status == 201) {
            String locationHeader = response.getHeaderString("Location");
            UUID userId = extractUserIdFromLocationHeader(locationHeader);

            log.debug("Пользователь {} ({}) успешно создан в Keycloak с ID: {}",
                email,
                userDisplayName,
                userId
            );

            return userId;
        } else if (status == 409) {
            log.error("Пользователя с email {} уже существует в Keycloak", email);
            throw new ConflictException(
                "Пользователь с email " + email + " уже существует в Keycloak"
            );
        } else {
            String errorMessage = Objects.toString(
                response.getStatusInfo().getReasonPhrase(),
                "Неизвестная ошибка"
            );

            log.error("Ошибка при создании пользователя {} ({}) в Keycloak: {} (код: {})",
                email,
                userDisplayName,
                errorMessage,
                status
            );

            throw new PlatformException(
                "Ошибка при создании пользователя в Keycloak: " + errorMessage
            );
        }
    }

    /**
     * Извлекает идентификатор пользователя из заголовка Location.
     *
     * @param locationHeader заголовок Location из ответа Keycloak
     * @return идентификатор пользователя
     */
    private UUID extractUserIdFromLocationHeader(String locationHeader) {
        if (!StringUtils.hasText(locationHeader)) {
            throw new PlatformException(
                "Не удалось получить идентификатор пользователя, заголовок Location отсутствует");
        }

        int lastSlashIndex = locationHeader.lastIndexOf('/');
        if (lastSlashIndex == -1 || lastSlashIndex == locationHeader.length() - 1) {
            throw new PlatformException(
                "Не удалось извлечь идентификатор пользователя из заголовка Location: "
                    + locationHeader
            );
        }

        String userId = locationHeader.substring(lastSlashIndex + 1);
        return UUID.fromString(userId);
    }
}
