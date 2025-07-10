package com.sursindmitry.berkutservice.mapper;

import com.sursindmitry.berkutservice.entity.User;
import com.sursindmitry.berkutservice.request.RegisterUserRequest;
import com.sursindmitry.berkutservice.response.UserProfileResponse;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Маппер для сущности {@link User}.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "id", source = "userId")
    @Mapping(target = "name", source = "request.firstName")
    @Mapping(target = "lastName", source = "request.lastName")
    @Mapping(target = "email", source = "request.email")
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "emailVerified", constant = "true")
    @Mapping(target = "chatId", ignore = true)
    User requestToUser(RegisterUserRequest request, UUID userId);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "firstName", source = "user.name")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "roles", source = "roles")
    UserProfileResponse toProfileResponse(User user, List<String> roles);
}
