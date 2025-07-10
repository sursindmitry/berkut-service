package com.sursindmitry.berkutservice.validation;

import com.sursindmitry.berkutservice.constant.ErrorCode;
import com.sursindmitry.berkutservice.request.LoginRequest;
import com.sursindmitry.berkutservice.request.RefreshRequest;
import com.sursindmitry.berkutservice.request.RegisterUserRequest;
import com.sursindmitry.commonmodels.exception.ValidationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Класс для валидации пользователей.
 * Содержит методы для проверки существования пользователей
 * и валидации основных данных пользователя.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserValidation {

    private static final String REQUEST = "request";
    private static final String REQUEST_CANNOT_BE_EMPTY = "Запрос не может быть пустым";

    /**
     * Проверяет основные данные пользователя на валидность.
     *
     * @param request запрос на регистрацию нового пользователя
     * @throws ValidationException если данные не проходят валидацию
     */
    public void validateUser(RegisterUserRequest request) {
        Map<String, String> errors = new HashMap<>();

        if (Objects.isNull(request)) {
            errors.put(REQUEST, REQUEST_CANNOT_BE_EMPTY);
            throw new ValidationException(errors);
        }

        validateUserData(request, errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    /**
     * Валидирует основные данные пользователя.
     *
     * @param request данные пользователя
     * @param errors  карта ошибок
     */
    private void validateUserData(RegisterUserRequest request, Map<String, String> errors) {
        validateFirstName(request.firstName(), errors);

        validateLastName(request.lastName(), errors);

        validateEmail(request.email(), errors);

        validatePassword(request.password(), errors);
    }


    /**
     * Валидирует имя.
     *
     * @param firstName проверяемое имя
     * @param errors    карта ошибок
     */
    private void validateFirstName(String firstName, Map<String, String> errors) {
        if (Objects.isNull(firstName)) {
            errors.put(ErrorCode.User.FIRST_NAME_INVALID, "Поле \"Имя\" не может быть пустым");
        } else {
            ValidationUtils.validateTextLength(
                firstName,
                ErrorCode.User.FIRST_NAME_INVALID,
                "Имя",
                errors
            );
        }
    }

    /**
     * Валидирует фамилию.
     *
     * @param lastName проверяемое имя
     * @param errors   карта ошибок
     */
    private void validateLastName(String lastName, Map<String, String> errors) {
        if (Objects.isNull(lastName)) {
            errors.put(ErrorCode.User.LAST_NAME_INVALID, "Поле \"Фамилия\" не может быть пустым");
        } else {
            ValidationUtils.validateTextLength(
                lastName,
                ErrorCode.User.LAST_NAME_INVALID,
                "Фамилия",
                errors
            );
        }
    }

    /**
     * Валидирует почтовый адрес.
     *
     * @param email  проверяемое имя
     * @param errors карта ошибок
     */
    private void validateEmail(String email, Map<String, String> errors) {
        if (Objects.isNull(email)) {
            errors.put(ErrorCode.User.EMAIL_NAME_INVALID,
                "Поле \"Email\" не может быть пустым");
        } else {
            ValidationUtils.validateTextLength(
                email,
                ErrorCode.User.EMAIL_NAME_INVALID,
                "Email",
                4,
                255,
                errors
            );
        }
    }

    /**
     * Валидирует пароль.
     *
     * @param password проверяемое имя
     * @param errors   карта ошибок
     */
    private void validatePassword(String password, Map<String, String> errors) {
        if (Objects.isNull(password)) {
            errors.put(
                ErrorCode.User.PASSWORD_NAME_INVALID,
                "Поле \"Пароль\" не может быть пустым"
            );
        } else {
            ValidationUtils.validateTextLength(
                password,
                ErrorCode.User.PASSWORD_NAME_INVALID,
                "Пароль",
                6,
                255,
                errors
            );
        }
    }

    /**
     * Проверяет {@link LoginRequest} на валидность.
     *
     * @param request {@link LoginRequest}
     */
    public void validateLogin(LoginRequest request) {
        Map<String, String> errors = new HashMap<>();

        if (Objects.isNull(request)) {
            errors.put(REQUEST, REQUEST_CANNOT_BE_EMPTY);
            throw new ValidationException(errors);
        }

        log.debug("Валидация аутентификации пользователя с email: {}", request.email());

        validateEmail(request.email(), errors);
        validatePassword(request.password(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    /**
     * Проверяет {@link RefreshRequest} на валидность.
     *
     * @param request {@link RefreshRequest}
     */
    public void validateRefresh(RefreshRequest request) {
        log.debug("Валидация токена обновления");

        Map<String, String> errors = new HashMap<>();

        if (!StringUtils.hasText(request.refreshToken())) {
            errors.put(REQUEST, REQUEST_CANNOT_BE_EMPTY);
            throw new ValidationException(errors);
        }
    }
}
