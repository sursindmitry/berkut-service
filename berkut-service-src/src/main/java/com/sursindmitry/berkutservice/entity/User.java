package com.sursindmitry.berkutservice.entity;

import com.sursindmitry.berkutservice.entity.base.AuditingBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Сущность пользователя.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends AuditingBaseEntity {

    @Column(name = "first_name", nullable = false)
    private String name;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(name, user.name)
            && Objects.equals(lastName, user.lastName)
            && Objects.equals(email, user.email)
            && Objects.equals(emailVerified, user.emailVerified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, lastName, email, emailVerified);
    }
}
