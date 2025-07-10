package com.sursindmitry.berkutservice.api;

import static com.sursindmitry.berkutservice.constant.ApiConstant.MANAGER_API;
import static com.sursindmitry.berkutservice.constant.RoleConstants.MANAGER;

import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер для модератора.
 */
@RequestMapping(MANAGER_API)
public interface ManagerController {

    /**
     * Выводит строку приветствия.
     *
     * @param principal аутентифицированный менеджер
     * @return строка приветствия
     */
    @GetMapping("/hello")
    @Secured(MANAGER)
    ResponseEntity<String> hello(Principal principal);
}
