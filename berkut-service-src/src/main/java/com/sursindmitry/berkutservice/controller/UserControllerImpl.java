package com.sursindmitry.berkutservice.controller;

import com.sursindmitry.berkutservice.api.UserController;
import com.sursindmitry.berkutservice.response.UserProfileResponse;
import com.sursindmitry.berkutservice.service.UserService;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Реализация контроллера пользователя.
 */
@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> hello(Principal principal) {
        return ResponseEntity.ok("Hello user " + principal.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<UserProfileResponse> findProfileById(UUID id) {
        return ResponseEntity.ok(userService.findProfileById(id));
    }
}
