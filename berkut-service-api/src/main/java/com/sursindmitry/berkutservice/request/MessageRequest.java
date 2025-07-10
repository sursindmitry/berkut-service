package com.sursindmitry.berkutservice.request;

/**
 * Запрос на сохранение сообщения.
 *
 * @param message сообщение
 */
public record MessageRequest(
    String message
) {
}
