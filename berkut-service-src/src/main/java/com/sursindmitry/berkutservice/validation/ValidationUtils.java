package com.sursindmitry.berkutservice.validation;

import java.util.Map;
import java.util.Objects;
import org.springframework.util.StringUtils;

/**
 * Утилитарный класс с общими методами валидации.
 */
public final class ValidationUtils {

    private static final int MIN_TEXT_LENGTH = 2;
    private static final int MAX_TEXT_LENGTH = 255;

    private ValidationUtils() {

    }


    /**
     * Валидирует длину текстового поля с дефолтной длиной.
     *
     * @param text       проверяемый текст
     * @param fieldName  название поля для ключа ошибки
     * @param fieldLabel человекочитаемое название поля
     * @param errors     карта ошибок
     */
    public static void validateTextLength(
        String text,
        String fieldName,
        String fieldLabel,
        Map<String, String> errors
    ) {
        validateText(text, fieldName, fieldLabel, errors, MIN_TEXT_LENGTH, MAX_TEXT_LENGTH);
    }


    /**
     * Валидирует длину текстового поля с пользовательской длиной.
     *
     * @param text       проверяемый текст
     * @param fieldName  название поля для ключа ошибки
     * @param fieldLabel человекочитаемое название поля
     * @param errors     карта ошибок
     */
    public static void validateTextLength(
        String text,
        String fieldName,
        String fieldLabel,
        int minLength,
        int maxLength,
        Map<String, String> errors
    ) {
        validateText(text, fieldName, fieldLabel, errors, minLength, maxLength);
    }

    private static void validateText(String text,
                                     String fieldName,
                                     String fieldLabel,
                                     Map<String, String> errors,
                                     int minTextLength,
                                     int maxTextLength
    ) {
        if (Objects.isNull(text) || !StringUtils.hasText(text)) {
            errors.put(
                fieldName,
                String.format("Поле \"%s\" не заполнено", fieldLabel)
            );
        } else if (text.length() < minTextLength) {
            errors.put(
                fieldName,
                String.format(
                    "Поле \"%s\" должно содержать не менее %d %s",
                    fieldLabel,
                    minTextLength,
                    pluralizeSymbol(minTextLength)
                )
            );
        } else if (text.length() > maxTextLength) {
            errors.put(
                fieldName,
                String.format(
                    "Поле \"%s\" должно содержать не более %d %s",
                    fieldLabel,
                    maxTextLength,
                    pluralizeSymbol(maxTextLength)
                )
            );
        }
    }

    /**
     * Возвращает корректную форму слова "символ" в зависимости от числительного.
     *
     * @param count количество символов
     * @return слово "символ" в нужной форме
     */
    private static String pluralizeSymbol(int count) {
        int mod10 = count % 10;
        int mod100 = count % 100;

        if (mod10 == 1 && mod100 != 11) {
            return "символ";
        } else if (mod10 >= 2 && mod10 <= 4 && (mod100 < 10 || mod100 >= 20)) {
            return "символа";
        } else {
            return "символов";
        }
    }
}
