package com.glop.users.out.jpadata.repository;

import com.glop.users.out.jpadata.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/** Les recherches par email passent par lower(email) pour utiliser l'index unique uq_users_email. */
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Query("select u from UserEntity u where lower(u.email) = lower(:email)")
    Optional<UserEntity> findByEmail(@Param("email") String email);

}
