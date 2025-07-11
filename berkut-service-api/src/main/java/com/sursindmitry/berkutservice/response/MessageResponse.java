package com.sursindmitry.berkutservice.response;

import java.util.UUID;

/**
 * Ответ сервера на запрос для получения сообщения.
 *
 * @param id      идентификатор сообщения
 * @param message текст сообщения
 * @param created дата создания в формате {@code dd.MM.yyyy}
 */
public record MessageResponse(

    UUID id,

    String message,

    String created) {
}
