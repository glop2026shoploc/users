package com.glop.users.in.restapi.controller;

import com.glop.users.domain.model.AuthenticationModel;
import com.glop.users.domain.model.CredentialsModel;
import com.glop.users.domain.model.UserRole;
import com.glop.users.domain.port.in.AuthPortIn;
import com.glop.users.in.restapi.mapper.AuthMapperImpl;
import com.glop.users.in.restapi.payload.auth.CredentialsPayload;
import com.glop.users.in.restapi.payload.auth.TokenResponse;
import com.glop.users.in.restapi.payload.user.UserRoleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthPortIn authPortIn;

    @Test
    void testLoginShouldReturnToken() {
        AuthController controller = new AuthController(authPortIn, new AuthMapperImpl());

        AuthenticationModel authentication = AuthenticationModel.builder().type("Bearer").token("token").build();
        when(authPortIn.login(any())).thenReturn(authentication);

        CredentialsPayload payload = new CredentialsPayload();
        payload.setEmail("john.doe@example.fr");
        payload.setPassword("AStrongPassword123!");
        payload.setRole(UserRoleEnum.MERCHANT);

        TokenResponse response = controller.login(payload);

        assertThat(response.getToken()).isEqualTo("token");
        assertThat(response.getType()).isEqualTo("Bearer");

        ArgumentCaptor<CredentialsModel> captor = ArgumentCaptor.forClass(CredentialsModel.class);
        verify(authPortIn).login(captor.capture());
        assertThat(captor.getValue().getEmail()).isEqualTo("john.doe@example.fr");
        assertThat(captor.getValue().getPassword()).isEqualTo("AStrongPassword123!");
        assertThat(captor.getValue().getRole()).isEqualTo(UserRole.MERCHANT);
    }
}