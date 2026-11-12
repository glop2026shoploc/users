package com.glop.users.domain.service;

import com.glop.users.domain.model.*;
import com.glop.users.domain.port.out.AuthContextPortOut;
import com.glop.users.domain.port.out.TokenPortOut;
import com.glop.users.domain.port.out.UserPortOut;
import com.glop.users.exception.credential.InvalidCredentialRoleNotAdminException;
import com.glop.users.exception.credential.InvalidCredentialRoleNotClientException;
import com.glop.users.exception.credential.InvalidCredentialRoleNotMerchantException;
import com.glop.users.exception.credential.InvalidCredentialsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    private static final UUID USER_ID = UUID.randomUUID();
    private static final String EMAIL = "test@glop.fr";

    @Mock
    private UserPortOut userPortOut;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenPortOut tokenPortOut;
    @Mock
    private AuthContextPortOut authContextPortOut;
    @InjectMocks
    private AuthService authService;

    private UserModel user;
    private CredentialsModel credentials;
    private final AuthContext context = new AuthContext();

    @BeforeEach
    void setUp() {
        user = new UserModel();
        user.setUserId(USER_ID);
        user.setEncodedPassword("hash");
        credentials = new CredentialsModel();
        credentials.setEmail(EMAIL);
        credentials.setPassword("motdepasse");
        credentials.setRole(UserRole.CLIENT);
    }

    @Test
    void testLoginShouldReturnBearerTokenWhenPasswordMatches() {
        when(userPortOut.findWithPasswordByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("motdepasse", "hash")).thenReturn(true);
        when(authContextPortOut.getClientContext(USER_ID)).thenReturn(Optional.of(context));
        when(tokenPortOut.generateToken(context)).thenReturn("jwt");

        AuthenticationModel result = authService.login(credentials);

        assertThat(result.getType()).isEqualTo("Bearer");
        assertThat(result.getToken()).isEqualTo("jwt");
    }

    @Test
    void testLoginShouldThrowWhenPasswordIsWrong() {
        when(userPortOut.findWithPasswordByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("motdepasse", "hash")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(credentials)).isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void testLoginShouldThrowWhenUserDoesNotExist() {
        when(userPortOut.findWithPasswordByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(credentials)).isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void testLoginWithMerchantRoleShouldUseMerchantContext() {
        when(authContextPortOut.getMerchantContext(USER_ID)).thenReturn(Optional.of(context));
        when(tokenPortOut.generateToken(context)).thenReturn("jwt");

        assertThat(authService.login(USER_ID, UserRole.MERCHANT).getToken()).isEqualTo("jwt");
    }

    @Test
    void testLoginShouldThrowWhenUserDoesNotHaveTheRole() {
        when(authContextPortOut.getAdminContext(USER_ID)).thenReturn(Optional.empty());
        when(authContextPortOut.getClientContext(USER_ID)).thenReturn(Optional.empty());
        when(authContextPortOut.getMerchantContext(USER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(USER_ID, UserRole.ADMIN))
                .isInstanceOf(InvalidCredentialRoleNotAdminException.class);
        assertThatThrownBy(() -> authService.login(USER_ID, UserRole.CLIENT))
                .isInstanceOf(InvalidCredentialRoleNotClientException.class);
        assertThatThrownBy(() -> authService.login(USER_ID, UserRole.MERCHANT))
                .isInstanceOf(InvalidCredentialRoleNotMerchantException.class);
    }

    @Test
    void testFindUserModelByUserEmailShouldDelegateToPort() {
        when(userPortOut.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        assertThat(authService.findUserModelByUserEmail(EMAIL)).contains(user);
    }
}
