package com.glop.users.out.jpadata.repository;

import com.glop.users.out.jpadata.entity.UserCredentialsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserCredentialsRepository extends JpaRepository<UserCredentialsEntity, UUID> {

    /** Credentials et user chargés en une seule requête : utilisé uniquement pour le login. */
    @Query("select c from UserCredentialsEntity c join fetch c.user u where lower(u.email) = lower(:email)")
    Optional<UserCredentialsEntity> findByEmail(@Param("email") String email);
}
