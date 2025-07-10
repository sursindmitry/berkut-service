package com.sursindmitry.berkutservice.mapper;

import com.sursindmitry.berkutservice.entity.UserRole;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Маппер для сущности {@link UserRole}.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserRoleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roleId", source = "roleId")
    @Mapping(target = "userId", source = "userId")
    UserRole toEntity(UUID userId, UUID roleId);
}
