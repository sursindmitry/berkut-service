package com.sursindmitry.berkutservice.controller.handler;

import com.sursindmitry.commonmodels.errors.ErrorResponse;
import com.sursindmitry.commonmodels.exception.AccessDeniedException;
import com.sursindmitry.commonmodels.exception.ConflictException;
import com.sursindmitry.commonmodels.exception.NotFoundException;
import com.sursindmitry.commonmodels.exception.PlatformException;
import com.sursindmitry.commonmodels.exception.ValidationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Глобальный обработчик исключений для всего приложения.
 *
 * <p>
 * Перехватывает специфические исключения приложения и преобразует их
 * в структурированные HTTP-ответы с соответствующими статус-кодами.
 * Обеспечивает единообразную обработку ошибок во всем приложении.
 * </p>
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String MESSAGE = "message";

    /**
     * Обрабатывает исключения типа {@link ValidationException}.
     *
     * @param ex исключение
     * @return ответ с картой ошибок и статусом 400 (Bad Request)
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        log.warn("Ошибка валидации: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(
            "Validation Error",
            ex.getErrors(),
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Обрабатывает исключения типа {@link AccessDeniedException}.
     *
     * @param ex исключение
     * @return ответ с картой ошибок и статусом 403 (Access Denied)
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Доступ запрещён: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(
            "Access Denied Error",
            Map.of(MESSAGE, ex.getMessage()),
            LocalDateTime.now(),
            HttpStatus.FORBIDDEN.value()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * Обрабатывает исключения типа {@link NotFoundException}.
     *
     * @param ex исключение
     * @return ответ с картой ошибок и статусом 404 (Not Found)
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        log.warn("Не найдено: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(
            "Not Found Error",
            Map.of(MESSAGE, ex.getMessage()),
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Обрабатывает исключения типа {@link ConflictException}.
     *
     * @param ex исключение
     * @return ответ с картой ошибок и статусом 409 (Conflict)
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
        log.warn("Конфликт: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(
            "Conflict Error",
            Map.of(MESSAGE, ex.getMessage()),
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Обрабатывает исключения типа {@link PlatformException}.
     *
     * @param ex исключение
     * @return ответ с сообщением об ошибке и статусом 500 (Internal Server Error)
     */
    @ExceptionHandler(PlatformException.class)
    public ResponseEntity<Map<String, Object>> handlePlatformException(PlatformException ex) {
        log.error("Ошибка платформы: {}", ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Internal Server Error");
        response.put(MESSAGE, ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
