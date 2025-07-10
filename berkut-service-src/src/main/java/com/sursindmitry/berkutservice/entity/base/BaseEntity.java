package com.sursindmitry.berkutservice.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * Базовое entity с id.
 */
@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
    @Id
    @Column(name = "id")
    protected UUID id;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "("
            + "id=" + id + ')';
    }
}
