package com.glop.users.domain.port.in;

import com.glop.users.domain.model.AuthenticationModel;
import com.glop.users.domain.model.CredentialsModel;
import com.glop.users.domain.model.UserModel;

import java.util.Optional;

public interface AuthPortIn {
    AuthenticationModel login(CredentialsModel credentialsModel);

    /** Réservé au JwtFilter : utilisateur sans mot de passe. */
    Optional<UserModel> findUserModelByUserEmail(String email);
}
