package com.sursindmitry.berkutservice.api;

import static com.sursindmitry.berkutservice.constant.ApiConstant.MESSAGE_API;
import static com.sursindmitry.berkutservice.constant.RoleConstants.ADMIN;
import static com.sursindmitry.berkutservice.constant.RoleConstants.MANAGER;
import static com.sursindmitry.berkutservice.constant.RoleConstants.MODERATOR;
import static com.sursindmitry.berkutservice.constant.RoleConstants.USER;

import com.sursindmitry.berkutservice.request.MessageRequest;
import com.sursindmitry.berkutservice.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.security.Principal;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер для работы с сообщениями от пользователей.
 */
@RequestMapping(MESSAGE_API)
public interface MessageController {

    /**
     * Принимает сообщение от пользователя, сохраняет в Бд и отправляет в Телеграм.
     *
     * @param messageRequest запрос сообщения
     * @param principal      {@link Principal}
     * @return {@code 200}, если сообщение было отправлено
     */
    @Operation(
        summary = "Отправляет сообщение в телеграм",
        description = "Сохраняет сообщение в БД и так же отправляет сообщение в телеграм"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Сообщение сохранено и отправлено"),
        @ApiResponse(responseCode = "400", description = "Сообщение не может быть пустым"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    @Secured({USER, MANAGER, MODERATOR, ADMIN})
    ResponseEntity<Void> sendMessage(@RequestBody MessageRequest messageRequest,
                                     Principal principal);

    /**
     * Ищет все сообщения пользователя.
     *
     * @param page      страница
     * @param size      размер страницы
     * @param principal {@link Principal}
     * @return {@link Page}
     */
    @Operation(
        summary = "Ищет все сообщения пользователя",
        description = "Находит все сообщения авторизованного пользователя используя пагинацию"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список сообщений получен"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/messages")
    @Secured({USER, MANAGER, MODERATOR, ADMIN})
    Page<MessageResponse> getMessages(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "0") int size,
        Principal principal
    );
}
