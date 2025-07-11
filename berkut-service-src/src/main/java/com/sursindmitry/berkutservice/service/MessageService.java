package com.sursindmitry.berkutservice.service;

import com.sursindmitry.berkutservice.request.MessageRequest;
import com.sursindmitry.berkutservice.response.MessageResponse;
import org.springframework.data.domain.Page;

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

    /**
     * Ищет сообщения пользователя.
     *
     * @param page страница
     * @param size размер одной страницы
     * @param id   идентификатор пользователя
     * @return {@link Page}
     */
    Page<MessageResponse> findMessages(int page, int size, String id);
}
