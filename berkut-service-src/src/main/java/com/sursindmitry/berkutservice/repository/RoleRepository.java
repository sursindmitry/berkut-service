package com.sursindmitry.berkutservice.repository;

import com.sursindmitry.berkutservice.entity.Role;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий сущности {@link Role}.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(String roleName);

    @Query("SELECT r.id FROM Role r WHERE r.name = :roleName")
    Optional<UUID> findIdByName(@Param("roleName") String roleName);
}
