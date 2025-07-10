package com.sursindmitry.berkutservice.mapper;

import com.sursindmitry.berkutservice.entity.Role;
import java.util.UUID;
import org.keycloak.representations.idm.RoleRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

/**
 * Маппинг ролей.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {

    @Mapping(source = "id", target = "id", qualifiedByName = "stringToUuid")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    Role toEntity(RoleRepresentation keycloakRole);

    @Named("stringToUuid")
    default UUID stringToUuid(String id) {
        return UUID.fromString(id);
    }
}
