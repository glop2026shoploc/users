package com.glop.users.out.jpadata.repository;

import com.glop.users.out.jpadata.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.nio.channels.FileChannel;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<ClientEntity, UUID> {
    Optional<ClientEntity> findByUser_userId(UUID userId);

    Optional<ClientEntity> findOptionalByUser_UserId(UUID userId);
}
