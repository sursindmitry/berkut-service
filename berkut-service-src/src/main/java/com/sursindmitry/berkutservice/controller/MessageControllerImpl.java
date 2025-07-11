package com.sursindmitry.berkutservice.controller;

import com.sursindmitry.berkutservice.api.MessageController;
import com.sursindmitry.berkutservice.request.MessageRequest;
import com.sursindmitry.berkutservice.response.MessageResponse;
import com.sursindmitry.berkutservice.service.MessageService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Реализация контролера {@link MessageController}.
 */
@RestController
@RequiredArgsConstructor
public class MessageControllerImpl implements MessageController {

    private final MessageService messageService;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Void> sendMessage(MessageRequest messageRequest, Principal principal) {

        messageService.sendMessage(messageRequest, principal.getName());

        return ResponseEntity.ok().build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<MessageResponse> getMessages(int page, int size, Principal principal) {
        return messageService.findMessages(page, size, principal.getName());
    }
}
