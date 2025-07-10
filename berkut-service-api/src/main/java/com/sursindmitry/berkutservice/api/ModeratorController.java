package com.sursindmitry.berkutservice.api;

import static com.sursindmitry.berkutservice.constant.ApiConstant.MODERATOR_API;
import static com.sursindmitry.berkutservice.constant.RoleConstants.MODERATOR;

import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер для модератора.
 */
@RequestMapping(MODERATOR_API)
public interface ModeratorController {

    /**
     * Выводит строку приветствия.
     *
     * @param principal аутентифицированный модератор
     * @return строка приветствия
     */
    @GetMapping("/hello")
    @Secured(MODERATOR)
    ResponseEntity<String> hello(Principal principal);
}
