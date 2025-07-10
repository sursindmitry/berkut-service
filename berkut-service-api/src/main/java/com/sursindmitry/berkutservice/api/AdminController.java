package com.sursindmitry.berkutservice.api;

import static com.sursindmitry.berkutservice.constant.ApiConstant.ADMIN_API;
import static com.sursindmitry.berkutservice.constant.RoleConstants.ADMIN;

import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер для администратора.
 */
@RequestMapping(ADMIN_API)
public interface AdminController {

    /**
     * Выводит строку приветствия.
     *
     * @param principal аутентифицированный администратор
     * @return строка приветствия
     */
    @GetMapping("/hello")
    @Secured(ADMIN)
    ResponseEntity<String> hello(Principal principal);

}
