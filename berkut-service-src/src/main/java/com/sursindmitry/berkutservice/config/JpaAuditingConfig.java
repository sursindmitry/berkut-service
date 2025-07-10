package com.sursindmitry.berkutservice.config;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Класс конфигурации JPA.
 */
@Configuration
@EnableJpaAuditing(
    modifyOnCreate = false,
    dateTimeProviderRef = "auditingDateTimeProvider",
    auditorAwareRef = "auditorProvider"
)
public class JpaAuditingConfig {

    /**
     * Создаёт бин отвечающий за автоматическую подставку даты.
     *
     * @return {@link DateTimeProvider}
     */
    @Bean
    public DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(LocalDateTime.now(ZoneOffset.UTC));
    }


    /**
     * Создаёт бин отвечающий за автоматическую подставку идентификатора пользователя
     * при обновлении и создании сущности.
     *
     * @return {@link AuditorAware}
     */
    @Bean
    public AuditorAware<UUID> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
                return Optional.empty();
            }

            Object principal = authentication.getPrincipal();
            if (principal instanceof Jwt jwt) {

                String subject = jwt.getSubject();

                return Optional.ofNullable(subject)
                    .map(UUID::fromString);
            }

            return Optional.empty();
        };
    }
}
