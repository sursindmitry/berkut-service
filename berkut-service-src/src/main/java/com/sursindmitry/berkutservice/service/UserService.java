package com.sursindmitry.berkutservice.service;

import com.sursindmitry.berkutservice.request.RegisterUserRequest;
import com.sursindmitry.berkutservice.response.UserProfileResponse;
import java.util.UUID;

/**
 * Сервис для работы с пользователями.
 */
public interface UserService {

    /**
     * Находит профиль пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return {@link UserProfileResponse}
     */
    UserProfileResponse findProfileById(UUID id);

    /**
     * Ищет идентификатор пользователя по email если он активен.
     *
     * @param email электронная почта
     * @return идентификатор пользователя
     */
    UUID getUserIdByEmail(String email);

    /**
     * Проверка на существующего пользователя.
     *
     * @param email электронная почта
     * @return true если существует, иначе false
     */
    boolean existsByEmail(String email);

    /**
     * Сохраняет пользователя после регистрации.
     *
     * @param request запрос
     * @param userId идентификатор пользователя
     * @return идентификатор сохранённого пользователя
     */
    UUID saveUserAfterRegistration(RegisterUserRequest request, UUID userId);
}
