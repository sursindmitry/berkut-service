package com.sursindmitry.berkutservice.service.impl;

import com.sursindmitry.berkutservice.entity.redis.RefreshToken;
import com.sursindmitry.berkutservice.repository.redis.RefreshTokenRepository;
import com.sursindmitry.berkutservice.service.RefreshTokenService;
import com.sursindmitry.commonmodels.exception.NotFoundException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса для работы с токенами обновления.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(UUID userId, String refreshToken, Long refreshTokenExpiresIn) {
        log.debug("Сохранение токена обновления для пользователя: {}", userId);

        RefreshToken refreshTokenEntity = new RefreshToken(
            userId.toString(),
            refreshToken,
            refreshTokenExpiresIn
        );

        refreshTokenRepository.save(refreshTokenEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID findUserIdByToken(String token) {
        log.debug("Поиск идентификатор пользователя используя токен обновления: {}", token);

        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByToken(token);
        log.debug("Найденный токен: {}", refreshTokenOptional);

        return refreshTokenOptional
            .map(refreshToken -> UUID.fromString(refreshToken.getId()))
            .orElseThrow(() -> new NotFoundException("Пользователь с таким токеном не найден"));
    }
}
