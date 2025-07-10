package com.sursindmitry.berkutservice.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Базовый класс для сущностей с поддержкой аудита.
 *
 * <p>
 * Расширяет {@link BaseEntity} и добавляет поля для отслеживания
 * времени создания и последнего обновления сущности.
 * Использует функционал JPA Auditing для автоматического заполнения полей.
 * </p>
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditingBaseEntity extends BaseEntity {

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

    /**
     * Идентификатор пользователя при создании сущности.
     */
    @CreatedBy
    @Column(name = "created_by")
    protected UUID createdBy;

    /**
     * Идентификатор пользователя при обновлении сущности.
     */
    @LastModifiedBy
    @Column(name = "updated_by")
    protected UUID updatedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AuditingBaseEntity that = (AuditingBaseEntity) o;
        return Objects.equals(created, that.created)
            && Objects.equals(updated, that.updated)
            && Objects.equals(createdBy, that.createdBy)
            && Objects.equals(updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), created, updated);
    }

    @Override
    public String toString() {
        return "AuditingBaseEntity{"
            + "id=" + getId()
            + ", created=" + created
            + ", updated=" + updated
            + ", createdBy=" + createdBy
            + ", updatedBy=" + updatedBy
            + '}';
    }
}
