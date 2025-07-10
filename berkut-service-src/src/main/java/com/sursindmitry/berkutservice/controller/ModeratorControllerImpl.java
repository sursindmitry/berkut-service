package com.sursindmitry.berkutservice.controller;

import com.sursindmitry.berkutservice.api.ModeratorController;
import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Реализация контроллера модератора.
 */
@RestController
public class ModeratorControllerImpl implements ModeratorController {

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> hello(Principal principal) {
        return ResponseEntity.ok("Hello moderator " + principal.getName());
    }
}
