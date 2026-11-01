package com.glop.users.in.restapi.controller;

import com.glop.users.domain.model.AuthenticationModel;
import com.glop.users.domain.port.in.AuthPortIn;
import com.glop.users.in.restapi.mapper.AuthMapperImpl;
import com.glop.users.in.restapi.payload.auth.TokenResponse;
import com.glop.users.in.restapi.payload.auth.UserCreationPayload;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthPortIn authPortIn;

    @Test
    void testRegisterShouldReturnToken() throws BadRequestException {
        AuthController controller = new AuthController(authPortIn, new AuthMapperImpl());

        AuthenticationModel authentication = AuthenticationModel.builder().type("Bearer").token("token").build();
        when(authPortIn.register(any())).thenReturn(authentication);

        UserCreationPayload payload = new UserCreationPayload();
        payload.setEmail("john.doe@example.fr");
        payload.setDataUsageConsent(true);

        TokenResponse response = controller.register(payload);

        assertThat(response.getToken()).isEqualTo("token");
        assertThat(response.getType()).isEqualTo("Bearer");
    }
}
