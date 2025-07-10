package com.sursindmitry.berkutservice.service;

import com.sursindmitry.berkutservice.entity.UserRole;
import java.util.List;
import java.util.UUID;

/**
 * Интерфейс для работы с сущностью {@link UserRole}.
 */
public interface UserRoleService {

    /**
     * Назначает роль пользователю.
     *
     * @param userId   идентификатор пользователя
     * @param roleName название роли
     */
    void assignRoleToUser(UUID userId, String roleName);

    /**
     * Находит названия ролей пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return список ролей конкретного пользователя
     */
    List<String> findAllRoles(UUID id);
}
