package com.sursindmitry.berkutservice.api;

import static com.sursindmitry.berkutservice.constant.ApiConstant.PUBLIC_API;

import com.sursindmitry.berkutservice.request.LoginRequest;
import com.sursindmitry.berkutservice.request.RefreshRequest;
import com.sursindmitry.berkutservice.request.RegisterUserRequest;
import com.sursindmitry.berkutservice.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Публичный контроллер для всех.
 */
@Tag(name = "Публичные API", description = "API доступные всем")
@RequestMapping(PUBLIC_API)
public interface PublicController {

    /**
     * Публичный API для проверки.
     *
     * @return строку "Публичный вход"
     */
    @GetMapping("/hello")
    ResponseEntity<String> hello();

    /**
     * Регистрирует нового пользователя в Keycloak и сохраняет его в БД.
     *
     * @param request данные для регистрации
     * @return идентификатор нового пользователя
     */
    @Operation(
        summary = "Регистрация нового пользователя",
        description = "Регистрирует нового пользователя в Keycloak"
            + "и сохраняет его в БД с ролью USER"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пользователя успешно зарегистрирован"),
        @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
        @ApiResponse(responseCode = "403",
            description = "Вы уже вошли в систему и не можете это использовать"),
        @ApiResponse(responseCode = "409",
            description = "Пользователь с таким email уже существует"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/register")
    ResponseEntity<UUID> register(@Valid @RequestBody RegisterUserRequest request);

    /**
     * Аутентификация пользователя.
     *
     * @param request {@link LoginRequest}
     * @return {@link TokenResponse}
     */
    @PostMapping("/login")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешная аутентификация"),
        @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request);

    /**
     * Обновление токена аутентификации.
     *
     * @param request {@link RefreshRequest}
     * @return {@link TokenResponse}
     */
    @PostMapping("/refresh")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешное обновление токена"),
        @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
        @ApiResponse(responseCode = "404", description = "Пользователь с таким токеном не найден"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest request);
}
