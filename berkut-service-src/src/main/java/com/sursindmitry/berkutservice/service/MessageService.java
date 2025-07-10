package com.sursindmitry.berkutservice.service;

import com.sursindmitry.berkutservice.request.MessageRequest;

/**
 * Интерфейс для работы с сообщениями пользователей.
 */
public interface MessageService {

    /**
     * Отправляет сообщение в телеграм.
     *
     * @param messageRequest {@link MessageRequest}
     * @param userId         идентификатор пользователя
     */
    void sendMessage(MessageRequest messageRequest, String userId);
}
