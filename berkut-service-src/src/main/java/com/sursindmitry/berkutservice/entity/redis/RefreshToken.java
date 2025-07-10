package com.sursindmitry.berkutservice.entity.redis;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

/**
 * Сущность для хранения токенов обновления в Redis.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "user-refresh-token",  timeToLive = 1800)
public class RefreshToken {
    @Id
    private String id;

    @Indexed
    private String token;

    private Long expiration;
}
