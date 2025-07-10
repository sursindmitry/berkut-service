package com.sursindmitry.berkutservice.api;

import static com.sursindmitry.berkutservice.constant.ApiConstant.USER_API;
import static com.sursindmitry.berkutservice.constant.RoleConstants.USER;

import com.sursindmitry.berkutservice.response.UserProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.security.Principal;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер пользователя.
 */
@RequestMapping(USER_API)
public interface UserController {

    /**
     * Выводит строку приветствия.
     *
     * @param principal аутентифицированный пользователь
     * @return строка приветствия
     */
    @GetMapping("/hello")
    @Secured(USER)
    ResponseEntity<String> hello(Principal principal);

    /**
     * Поиск профиля пользователя по id.
     *
     * @param id идентификатор пользователя
     * @return профиль пользователя
     */
    @Operation(
        summary = "Ищет профиль пользователя по id",
        description = "В URI передаётся идентификатор пользователя, который ищется в платформе"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Профиль пользователя найден"),
        @ApiResponse(responseCode = "404", description = "Пользователь с таким идентификатором"
            + "не найден"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/{id}")
    @Secured(USER)
    ResponseEntity<UserProfileResponse> findProfileById(@PathVariable UUID id);
}
