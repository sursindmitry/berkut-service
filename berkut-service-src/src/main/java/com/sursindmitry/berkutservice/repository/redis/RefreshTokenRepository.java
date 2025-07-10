package com.sursindmitry.berkutservice.repository.redis;

import com.sursindmitry.berkutservice.entity.redis.RefreshToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностью {@link RefreshToken}.
 */
@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByToken(String token);
}
