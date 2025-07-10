package com.sursindmitry.berkutservice.repository;

import com.sursindmitry.berkutservice.entity.Message;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозитория для работы с сущностью {@link Message}.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
}
