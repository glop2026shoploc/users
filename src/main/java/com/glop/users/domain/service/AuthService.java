package com.glop.users.domain.service;

import com.glop.users.domain.model.*;
import com.glop.users.domain.port.in.AuthPortIn;
import com.glop.users.domain.port.out.AuthContextPortOut;
import com.glop.users.domain.port.out.TokenPortOut;
import com.glop.users.domain.port.out.UserPortOut;
import com.glop.users.exception.credential.InvalidCredentialRoleNotAdminException;
import com.glop.users.exception.credential.InvalidCredentialRoleNotClientException;
import com.glop.users.exception.credential.InvalidCredentialRoleNotMerchantException;
import com.glop.users.exception.credential.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthPortIn {
    private final UserPortOut userPortOut;
    private final PasswordEncoder passwordEncoder;
    private final TokenPortOut tokenPortOut;
    private final AuthContextPortOut authContextAdapterOut;

    @Override
    public AuthenticationModel login(CredentialsModel credentials) {
        UserModel user = userPortOut.findWithPasswordByEmail(credentials.getEmail())
                .filter(found -> passwordEncoder.matches(credentials.getPassword(), found.getEncodedPassword()))
                .orElseThrow(InvalidCredentialsException::new);

        user.setEncodedPassword(null);
        AuthContext context = getContextForRole(user.getUserId(),credentials.getRole());
        return AuthenticationModel.bearer(tokenPortOut.generateToken(context));
    }

    public AuthenticationModel login(UUID userId, UserRole role) {
        AuthContext context = getContextForRole(userId,role);
        return AuthenticationModel.bearer(tokenPortOut.generateToken(context));
    }



    private AuthContext getContextForRole(UUID userId, UserRole role) throws InvalidCredentialsException{
        return switch (role){
            case ADMIN -> authContextAdapterOut.getAdminContext(userId).orElseThrow(InvalidCredentialRoleNotAdminException::new);
            case CLIENT -> authContextAdapterOut.getClientContext(userId).orElseThrow(InvalidCredentialRoleNotClientException::new);
            case MERCHANT -> authContextAdapterOut.getMerchantContext(userId).orElseThrow(InvalidCredentialRoleNotMerchantException::new);
        };
    }

    @Override
    public Optional<UserModel> findUserModelByUserEmail(String email) {
        return userPortOut.findByEmail(email);
    }
}
