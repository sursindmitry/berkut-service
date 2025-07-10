package com.sursindmitry.berkutservice.validation;

import com.sursindmitry.berkutservice.request.MessageRequest;
import com.sursindmitry.commonmodels.exception.ValidationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Класс для валидации сообщений.
 */
@Component
@Slf4j
public class MessageValidation {

    private static final String REQUEST = "request";
    private static final String REQUEST_CANNOT_BE_EMPTY = "Запрос не может быть пустым";

    /**
     * Валидирует {@link MessageRequest}.
     *
     * @param messageRequest {@link MessageRequest}
     */
    public void validateMessage(MessageRequest messageRequest) {
        log.debug("Валидация сообщения");

        Map<String, String> errors = new HashMap<>();

        if (Objects.isNull(messageRequest)) {
            errors.put(REQUEST, REQUEST_CANNOT_BE_EMPTY);
            throw new ValidationException(errors);
        }

        if (!StringUtils.hasText(messageRequest.message())) {
            errors.put(REQUEST, REQUEST_CANNOT_BE_EMPTY);
        }

        if (messageRequest.message().length() > 500) {
            errors.put(REQUEST, "Сообщение не может быть больше 500 символов");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
