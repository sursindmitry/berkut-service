package com.sursindmitry.berkutservice.controller;

import com.sursindmitry.berkutservice.api.AdminController;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Реализация контроллера админа.
 */
@RestController
@RequiredArgsConstructor
public class AdminControllerImpl implements AdminController {

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> hello(Principal principal) {
        return ResponseEntity.ok("Hello admin " + principal.getName());
    }

}
