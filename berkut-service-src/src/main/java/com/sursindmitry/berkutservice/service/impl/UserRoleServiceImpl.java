package com.sursindmitry.berkutservice.service.impl;

import com.sursindmitry.berkutservice.entity.UserRole;
import com.sursindmitry.berkutservice.mapper.UserRoleMapper;
import com.sursindmitry.berkutservice.repository.UserRoleRepository;
import com.sursindmitry.berkutservice.service.RoleService;
import com.sursindmitry.berkutservice.service.UserRoleService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Реализация сервиса работающего с сущностью {@link UserRole}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserRoleServiceImpl implements UserRoleService {

    private final RoleService roleService;

    private final UserRoleRepository userRoleRepository;

    private final UserRoleMapper userRoleMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void assignRoleToUser(UUID userId, String roleName) {
        log.debug("Назначение роли {} пользователю с идентификатором: {}", roleName, userId);

        UUID roleId = roleService.findIdByName(roleName);

        if (userRoleRepository.existsByUserIdAndRoleId(userId, roleId)) {
            log.debug(
                "Связь между пользователем {} и ролью {} уже существует",
                userId,
                roleName
            );
            return;
        }

        UserRole userRole = userRoleMapper.toEntity(userId, roleId);

        userRoleRepository.save(userRole);
        log.debug("Роль {} успешно назначена пользователю с идентификатором: {}", roleName, userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> findAllRoles(UUID id) {
        log.debug("Получение всех ролей пользователя с идентификатором: {}", id);

        List<UserRole> userRoles = userRoleRepository.findAllByUserId(id);

        List<UUID> roleIds = userRoles.stream()
            .map(UserRole::getRoleId)
            .toList();

        return roleService.findIdByNamesByIds(roleIds);
    }
}
