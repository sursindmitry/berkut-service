package com.sursindmitry.berkutservice.service;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс для работы с ролями пользователей.
 */
public interface RoleService {

    /**
     * Синхронизирует роли из Keycloak с базой данных.
     * Получает все роли из Keycloak и сохраняет их в базу данных.
     */
    void syncRoles();

    /**
     * Находит идентификатор роли по имени.
     *
     * @param roleName имя роли
     * @return идентификатор роли
     */
    UUID findIdByName(String roleName);

    /**
     * Находит названия ролей по списку идентификаторов ролей.
     *
     * @param roleIds список идентификаторов ролей
     * @return список названий ролей
     */
    List<String> findIdByNamesByIds(List<UUID> roleIds);
}
