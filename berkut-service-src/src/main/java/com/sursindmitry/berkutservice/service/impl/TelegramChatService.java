package com.sursindmitry.berkutservice.service.impl;

import com.sursindmitry.berkutservice.service.ChatService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Реализация чата для телеграм.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramChatService implements ChatService {

    private final WebClient webClient;

    @Value("${telegram.token}")
    private String botToken;

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(String message, String name, String lastName, String chatId) {
        log.debug("Отправка сообщения в чат с идентификатором {}", chatId);

        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

        String text = name + " " + lastName + ", я получил твоё сообщение:\n" + message;

        webClient.post()
            .uri(url)
            .bodyValue(Map.of(
                "chat_id", chatId,
                "text", text)
            )
            .retrieve()
            .bodyToMono(String.class)
            .block();

        log.debug("Сообщение успешно отправилось в чат с идентификатором {}", chatId);
    }
}
