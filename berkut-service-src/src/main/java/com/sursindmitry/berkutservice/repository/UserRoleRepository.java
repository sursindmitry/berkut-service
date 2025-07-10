package com.sursindmitry.berkutservice.repository;

import com.sursindmitry.berkutservice.entity.UserRole;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозитория для работы с сущностью {@link UserRole}.
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    boolean existsByUserIdAndRoleId(UUID userId, UUID roleId);

    List<UserRole> findAllByUserId(UUID id);
}
