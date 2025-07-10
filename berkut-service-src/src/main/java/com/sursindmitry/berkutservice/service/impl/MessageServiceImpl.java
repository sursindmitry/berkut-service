package com.sursindmitry.berkutservice.service.impl;

import com.sursindmitry.berkutservice.entity.Message;
import com.sursindmitry.berkutservice.entity.User;
import com.sursindmitry.berkutservice.mapper.MessageMapper;
import com.sursindmitry.berkutservice.repository.MessageRepository;
import com.sursindmitry.berkutservice.request.MessageRequest;
import com.sursindmitry.berkutservice.service.ChatService;
import com.sursindmitry.berkutservice.service.MessageService;
import com.sursindmitry.berkutservice.service.UserService;
import com.sursindmitry.berkutservice.validation.MessageValidation;
import com.sursindmitry.berkutservice.validation.UserValidation;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса для работы с сообщениями пользователей.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final UserValidation userValidation;
    private final MessageValidation messageValidation;

    private final UserService userService;
    private final ChatService chatService;
    private final MessageMapper messageMapper;

    private final MessageRepository messageRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(MessageRequest messageRequest, String userId) {
        UUID id = userValidation.validateId(userId);
        messageValidation.validateMessage(messageRequest);

        User user = userService.findById(id);

        Message message = messageMapper.toMessage(
            id,
            messageRequest.message(),
            user.getChatId()
        );

        messageRepository.save(message);

        chatService.sendMessage(
            message.getMessage(),
            user.getName(),
            user.getLastName(),
            user.getChatId()
        );
    }
}
