package com.glop.users.domain.service;

import com.glop.users.config.security.JwtUtils;
import com.glop.users.domain.model.AuthenticationModel;
import com.glop.users.domain.model.UserModel;
import com.glop.users.domain.port.out.UserPortOut;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserPortOut userPortOut;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    private AuthService authService;

    private UserModel user;

    @BeforeEach
    void setUp() {
        this.authService = new AuthService(userPortOut, passwordEncoder, jwtUtils);

        this.user = new UserModel();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail(" John.Doe@Example.fr ");
        user.setPassword("AStrongPassword123!");
        user.setDataUsageConsent(true);
    }

    @Test
    public void testRegisterShouldThrowErrorWhenNoConsent() {
        user.setDataUsageConsent(false);

        assertThatThrownBy(() -> authService.register(user))
                .isInstanceOf(BadRequestException.class);

        verify(userPortOut, never()).create(any());
    }

    @Test
    public void testRegisterShouldThrowErrorWhenEmailAlreadyExists() {
        when(userPortOut.findByEmail("john.doe@example.fr")).thenReturn(new UserModel());

        assertThatThrownBy(() -> authService.register(user))
                .isInstanceOf(ResponseStatusException.class);

        verify(userPortOut, never()).create(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    public void testRegisterShouldNormalizeEmailAndReturnToken() throws BadRequestException {
        when(passwordEncoder.encode("AStrongPassword123!")).thenReturn("hashed-password");
        when(userPortOut.create(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtUtils.generateToken(any())).thenReturn("token");

        AuthenticationModel result = authService.register(user);

        assertThat(user.getEmail()).isEqualTo("john.doe@example.fr");
        assertThat(user.getEncodedPassword()).isEqualTo("hashed-password");
        assertThat(result.getToken()).isEqualTo("token");
    }
}
