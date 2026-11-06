package com.glop.users.domain.service;

import com.glop.users.domain.model.UserModel;
import com.glop.users.domain.port.out.UserPortOut;
import com.glop.users.exception.AlreadyRegisterException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Création d'un compte (user + credentials), commune aux inscriptions client et commerçant.
 * C'est le seul endroit où un mot de passe est haché.
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserPortOut userPortOut;
    private final PasswordEncoder passwordEncoder;

    public UserModel createAccount(UserModel account) {
        account.setEmail(normalizeEmail(account.getEmail()));
        if (userPortOut.existsByEmail(account.getEmail())) {
            throw new AlreadyRegisterException();
        }

        account.setEncodedPassword(passwordEncoder.encode(account.getPassword()));
        account.setPassword(null);

        UserModel created = userPortOut.create(account);
        created.setEncodedPassword(null);
        return created;
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
