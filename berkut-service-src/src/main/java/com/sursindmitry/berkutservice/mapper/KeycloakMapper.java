package com.sursindmitry.berkutservice.mapper;

import com.sursindmitry.berkutservice.request.RegisterUserRequest;
import java.util.Collections;
import java.util.List;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

/**
 * Маппер для Keycloak.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface KeycloakMapper {

    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "username", source = "request.email")
    @Mapping(target = "credentials", source = "request", qualifiedByName = "createCredentials")
    @Mapping(target = "emailVerified", constant = "true")
    UserRepresentation userRequestToUserRepresentation(RegisterUserRequest request);

    /**
     * Создаёт представление учётных данных для Keycloak.
     *
     * @param request запрос на создание пользователя
     * @return список представлений учётных данных
     */
    @Named("createCredentials")
    default List<CredentialRepresentation> createCredentials(RegisterUserRequest request) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.password());
        credential.setTemporary(false);

        return Collections.singletonList(credential);
    }
}
