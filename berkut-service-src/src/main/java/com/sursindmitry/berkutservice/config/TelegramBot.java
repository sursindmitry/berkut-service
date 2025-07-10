package com.sursindmitry.berkutservice.config;

import com.sursindmitry.berkutservice.entity.User;
import com.sursindmitry.berkutservice.service.UserService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Настройка телеграм бота.
 */
@Component
public class TelegramBot extends TelegramLongPollingBot implements ApplicationRunner {

    private final UserService userService;


    public TelegramBot(@Value("${telegram.token}") String botToken,
                       UserService userService) {
        super(botToken);
        this.userService = userService;
    }

    @Override
    public String getBotUsername() {
        return "sursinberkuttest_bot";
    }

    /**
     * Принимает сообщение от бота, проверяет существование пользователя и привязывает чат к нему.
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String token = update.getMessage().getText().trim();

            UUID userId = UUID.fromString(token);

            boolean existsUser = userService.existsById(userId);

            if (existsUser) {

                User user = userService.findById(userId);

                user.setChatId(chatId);

                userService.save(user);

                SendMessage message = new SendMessage(chatId, "Телеграм успешно привязан");
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else {
                SendMessage message = new SendMessage(chatId, "Токен не найден");
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(this);
    }
}
