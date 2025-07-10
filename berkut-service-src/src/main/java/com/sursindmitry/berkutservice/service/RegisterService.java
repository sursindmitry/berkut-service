package com.sursindmitry.berkutservice.service;

import com.sursindmitry.berkutservice.request.RegisterUserRequest;
import java.util.UUID;

/**
 * Сервис регистрации.
 */
public interface RegisterService {
    /**
     * Регистрирует нового пользователя.
     *
     * @param request данные для регистрации
     * @return идентификатор пользователя
     */
    UUID register(RegisterUserRequest request);
}
