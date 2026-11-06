package com.glop.users.domain.service;

import com.glop.users.domain.model.AuthenticationModel;
import com.glop.users.domain.model.CredentialsModel;
import com.glop.users.domain.model.UserModel;
import com.glop.users.domain.port.in.AuthPortIn;
import com.glop.users.domain.port.out.TokenPortOut;
import com.glop.users.domain.port.out.UserPortOut;
import com.glop.users.exception.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthPortIn {
    private final UserPortOut userPortOut;
    private final PasswordEncoder passwordEncoder;
    private final TokenPortOut tokenPortOut;

    @Override
    public AuthenticationModel login(CredentialsModel credentials) {
        UserModel user = userPortOut.findWithPasswordByEmail(credentials.getEmail())
                .filter(found -> passwordEncoder.matches(credentials.getPassword(), found.getEncodedPassword()))
                .orElseThrow(InvalidCredentialsException::new);

        user.setEncodedPassword(null);
        return AuthenticationModel.bearer(tokenPortOut.generateToken(user));
    }

    @Override
    public Optional<UserModel> findUserModelByUserEmail(String email) {
        return userPortOut.findByEmail(email);
    }
}
