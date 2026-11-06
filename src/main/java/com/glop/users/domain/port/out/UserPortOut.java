package com.glop.users.domain.port.out;

import com.glop.users.domain.model.UserModel;

import java.util.Optional;
import java.util.UUID;

public interface UserPortOut {
    boolean existsByEmail(String email);

    Optional<UserModel> findById(UUID userId);

    /** Utilisateur sans son mot de passe. */
    Optional<UserModel> findByEmail(String email);

    /** Utilisateur avec son hash de mot de passe : réservé au login. */
    Optional<UserModel> findWithPasswordByEmail(String email);

    /** Crée l'utilisateur et ses credentials à partir de {@code encodedPassword}. */
    UserModel create(UserModel userModel);
}
