package com.sursindmitry.berkutservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Сущность для хранения сообщений пользователей.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Сообщение.
     */
    @Column(name = "message", nullable = false)
    private String message;

    /**
     * Идентификатор пользователя.
     */
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /**
     * Идентификатор чата.
     */
    @Column(name = "chat_id", nullable = false)
    private String chatId;

    /**
     * Дата и время создания сущности.
     */
    @CreatedDate
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    /**
     * Дата и время последнего обновления сущности.
     */
    @LastModifiedDate
    @Column(name = "updated")
    protected LocalDateTime updated;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message1 = (Message) o;
        return Objects.equals(id, message1.id)
            && Objects.equals(message, message1.message)
            && Objects.equals(userId, message1.userId)
            && Objects.equals(created, message1.created)
            && Objects.equals(updated, message1.updated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, userId, created, updated);
    }
}
