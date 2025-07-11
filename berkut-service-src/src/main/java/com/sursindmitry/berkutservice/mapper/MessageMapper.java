package com.sursindmitry.berkutservice.mapper;

import com.sursindmitry.berkutservice.entity.Message;
import com.sursindmitry.berkutservice.response.MessageResponse;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Маппер для сущности {@link Message}.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageMapper {

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "message", source = "message")
    @Mapping(target = "chatId", source = "chatId")
    Message toMessage(UUID userId, String message, String chatId);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "message", source = "message")
    @Mapping(target = "created", source = "created", dateFormat = "dd.MM.yyy")
    MessageResponse toMessageResponse(Message message);
}
