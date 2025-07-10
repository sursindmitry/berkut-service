package com.sursindmitry.berkutservice.response;

import java.util.List;
import java.util.UUID;

/**
 * Ответ сервера на запрос получения профиля пользователя.
 *
 * @param id        идентификатор пользователя
 * @param firstName имя пользователя
 * @param lastName  фамилия пользователя
 * @param email     email пользователя
 * @param roles     роли пользователя
 */
public record UserProfileResponse(
    UUID id,
    String firstName,
    String lastName,
    String email,
    List<String> roles
) {
}
