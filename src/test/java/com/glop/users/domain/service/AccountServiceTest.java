package com.glop.users.domain.service;

import com.glop.users.domain.model.UserModel;
import com.glop.users.domain.port.out.UserPortOut;
import com.glop.users.exception.RequiredUserDataAuthorization;
import com.glop.users.exception.credential.AlreadyRegisterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    public void testCreateAccountShouldThrowErrorWhenNoConsent() {
        user.setDataUsageConsent(false);

        assertThatThrownBy(() -> accountService.createAccount(user))
                .isInstanceOf(RequiredUserDataAuthorization.class);

        verify(userPortOut, never()).create(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    public void testCreateAccountShouldThrowErrorWhenEmailAlreadyExists() {
        when(userPortOut.existsByEmail("john.doe@example.fr")).thenReturn(true);

        assertThatThrownBy(() -> accountService.createAccount(user))
                .isInstanceOf(AlreadyRegisterException.class);

        verify(userPortOut, never()).create(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    public void testCreateAccountShouldNormalizeEmailAndHashPassword() {
        when(userPortOut.existsByEmail("john.doe@example.fr")).thenReturn(false);
        when(passwordEncoder.encode("AStrongPassword123!")).thenReturn("hashed-password");

        ArgumentCaptor<UserModel> captor = ArgumentCaptor.forClass(UserModel.class);
        when(userPortOut.create(captor.capture())).thenAnswer(invocation -> {
            UserModel sent = invocation.getArgument(0);
            // Vérifié au moment de l'appel : le service efface ensuite le hash du résultat
            assertThat(sent.getEncodedPassword()).isEqualTo("hashed-password");
            assertThat(sent.getPassword()).isNull();
            return sent;
        });

        UserModel result = accountService.createAccount(user);

        assertThat(captor.getValue().getEmail()).isEqualTo("john.doe@example.fr");
        assertThat(result.getEmail()).isEqualTo("john.doe@example.fr");
        assertThat(result.getPassword()).isNull();
        assertThat(result.getEncodedPassword()).isNull();
    }
}
