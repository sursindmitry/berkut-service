package com.sursindmitry.berkutservice.service;

/**
 * Интерфейс для отправки сообщения.
 */
public interface ChatService {

    /**
     * Отправляет сообщение в чат по его идентификатору.
     *
     * @param message  сообщение
     * @param name     имя пользователя
     * @param lastName фамилия пользователя
     * @param chatId   идентификатор чата
     */
    void sendMessage(String message, String name, String lastName, String chatId);
}
