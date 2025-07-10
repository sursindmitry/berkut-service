package com.sursindmitry.berkutservice.service.impl;

import com.sursindmitry.berkutservice.constant.RoleConstants;
import com.sursindmitry.berkutservice.entity.User;
import com.sursindmitry.berkutservice.mapper.UserMapper;
import com.sursindmitry.berkutservice.repository.UserRepository;
import com.sursindmitry.berkutservice.request.RegisterUserRequest;
import com.sursindmitry.berkutservice.response.UserProfileResponse;
import com.sursindmitry.berkutservice.service.UserRoleService;
import com.sursindmitry.berkutservice.service.UserService;
import com.sursindmitry.commonmodels.exception.NotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для работы с пользователями.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRoleService userRoleService;

    private final UserMapper userMapper;

    private final UserRepository userRepository;


    /**
     * {@inheritDoc}
     */
    @Override
    public UserProfileResponse findProfileById(UUID id) {
        log.debug("Поиск профиля пользователя по идентификатору: {}", id);

        User user = userRepository.findById(id)
            .orElseThrow(NotFoundException::new);

        List<String> roles = userRoleService.findAllRoles(id);

        return userMapper.toProfileResponse(user, roles);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public UUID getUserIdByEmail(String email) {
        log.debug("Поиск пользователя по email: {}", email);

        return userRepository.findIdByEmail(email)
            .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID saveUserAfterRegistration(RegisterUserRequest request, UUID userId) {
        log.debug("Сохранение пользователя после регистрации {}", userId);

        User user = userMapper.requestToUser(request, userId);

        userRepository.save(user);
        userRoleService.assignRoleToUser(userId, RoleConstants.USER);

        return userId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public User findById(UUID userId) {
        log.debug("Поиск пользователя по идентификатору {}", userId);

        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID userId) {
        return userRepository.existsById(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void save(User user) {
        log.debug("Сохраняет пользователя");

        userRepository.save(user);
    }
}
