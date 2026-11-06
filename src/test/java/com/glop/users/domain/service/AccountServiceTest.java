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

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private UserPortOut userPortOut;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AccountService accountService;

    private UserModel user;

    @BeforeEach
    void setUp() {
        this.accountService = new AccountService(userPortOut, passwordEncoder);

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

        assertThatThrownBy(() -> accountService.createAccount(user))
                .isInstanceOf(BadRequestException.class);

        verify(userPortOut, never()).create(any());
    }

    @Test
    public void testRegisterShouldThrowErrorWhenEmailAlreadyExists() {
        when(userPortOut.findByEmail("john.doe@example.fr")).thenReturn(Optional.of(new UserModel()));

        assertThatThrownBy(() -> accountService.createAccount(user))
                .isInstanceOf(ResponseStatusException.class);

        verify(userPortOut, never()).create(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    public void testRegisterShouldNormalizeEmail() throws BadRequestException {
        when(passwordEncoder.encode("AStrongPassword123!")).thenReturn("hashed-password");
        when(userPortOut.create(any())).thenAnswer(invocation -> invocation.getArgument(0));

        UserModel result = accountService.createAccount(user);

        assertThat(result.getEmail()).isEqualTo("john.doe@example.fr");
        assertThat(result.getEncodedPassword()).isEqualTo("hashed-password");
    }
}
