package com.sursindmitry.berkutservice.controller;

import com.sursindmitry.berkutservice.api.ManagerController;
import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Реализация контроллера менеджера.
 */
@RestController
public class ManagerControllerImpl implements ManagerController {

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> hello(Principal principal) {
        return ResponseEntity.ok("Hello manager " + principal.getName());
    }
}
