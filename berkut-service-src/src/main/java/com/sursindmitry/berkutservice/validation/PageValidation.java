package com.sursindmitry.berkutservice.validation;

import com.sursindmitry.commonmodels.exception.ValidationException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Валидация данных при использовании пагинации.
 */
@Component
@Slf4j
public class PageValidation {

    /**
     * Валидирует страницу и размер.
     *
     * @param page страница
     * @param size размер страницы
     */
    public void validatePageAndSize(int page, int size) {
        Map<String, String> errors = new HashMap<>();

        if (page < 0) {
            errors.put("page", "page не может быть меньше 0");
        }

        if (size <= 0 || size > 100) {
            errors.put("size", "size не может быть меньше 1 или больше 100");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
