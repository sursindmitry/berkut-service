package com.sursindmitry.berkutservice.service.impl;

import com.sursindmitry.berkutservice.constant.RoleConstants;
import com.sursindmitry.berkutservice.entity.Role;
import com.sursindmitry.berkutservice.mapper.RoleMapper;
import com.sursindmitry.berkutservice.repository.RoleRepository;
import com.sursindmitry.berkutservice.service.KeycloakService;
import com.sursindmitry.berkutservice.service.RoleService;
import com.sursindmitry.commonmodels.exception.PlatformException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для работы с ролями.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final KeycloakService keycloakService;

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void syncRoles() {
        log.debug("Начало синхронизации ролей Keycloak");

        try {
            List<RoleRepresentation> keycloakRoles = keycloakService.findAllRoles();

            List<RoleRepresentation> filterRoles = filterRoles(keycloakRoles);

            saveOrUpdateRoles(filterRoles);
        } catch (Exception e) {
            log.error("Ошибка синхронизации ролей из Keycloak", e);
            throw new PlatformException("Ошибка синхронизации ролей из Keycloak", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UUID findIdByName(String roleName) {
        return roleRepository.findIdByName(roleName)
            .orElseThrow(() -> new PlatformException("Роль с именем" + roleName + "не найдена"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<String> findIdByNamesByIds(List<UUID> roleIds) {
        return roleRepository.findAllById(roleIds).stream()
            .map(Role::getName)
            .toList();
    }

    /**
     * Фильтрует роли из Keycloak, оставляя только те, которые нужны.
     *
     * @param keycloakRoles список всех ролей из Keycloak
     * @return отфильтрованный список
     */
    private List<RoleRepresentation> filterRoles(List<RoleRepresentation> keycloakRoles) {
        List<RoleRepresentation> filterRoles = new ArrayList<>();

        for (RoleRepresentation role : keycloakRoles) {
            String roleName = role.getName();

            boolean isAdmin = roleName.contains(RoleConstants.ADMIN);
            boolean isModerator = roleName.contains(RoleConstants.MODERATOR);
            boolean isManager = roleName.contains(RoleConstants.MANAGER);
            boolean isUser = roleName.contains(RoleConstants.USER);

            if (isAdmin || isModerator || isManager || isUser) {
                filterRoles.add(role);
                log.debug("Роль {} прошла фильтрацию", roleName);
            } else {
                log.debug("Пропуск роли {}", roleName);
            }
        }

        log.debug("Добавление {} ролей", filterRoles.size());

        return filterRoles;
    }

    /**
     * Сохраняет или обновляет роли в БД.
     *
     * @param filterRoles список ролей для сохранения
     */
    private void saveOrUpdateRoles(List<RoleRepresentation> filterRoles) {

        for (RoleRepresentation keycloakRole : filterRoles) {
            String roleName = keycloakRole.getName();

            log.debug("Обработка роли: id={}, name={}",
                keycloakRole.getId(),
                roleName
            );

            Optional<Role> existingRole = roleRepository.findByName(roleName);

            if (existingRole.isPresent()) {
                Role role = existingRole.get();

                role.setId(UUID.fromString(keycloakRole.getId()));
                role.setName(roleName);
                role.setDescription(keycloakRole.getDescription());

                roleRepository.save(role);
                log.debug("Обновлена существующая роль: {}", role);
            } else {
                Role newRole = roleMapper.toEntity(keycloakRole);

                roleRepository.save(newRole);
                log.debug("Создана новая роль: {}", newRole);
            }
        }
    }
}
