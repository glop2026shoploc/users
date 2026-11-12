package com.glop.users.in.restapi.controller;

import com.glop.users.config.security.CustomUserDetails;
import com.glop.users.domain.model.AuthenticationModel;
import com.glop.users.domain.model.ClientModel;
import com.glop.users.domain.port.in.ClientPortIn;
import com.glop.users.in.restapi.mapper.AuthMapperImpl;
import com.glop.users.in.restapi.mapper.ClientMapperImpl;
import com.glop.users.in.restapi.payload.auth.TokenResponse;
import com.glop.users.in.restapi.payload.auth.UserCreationPayload;
import com.glop.users.in.restapi.payload.client.ClientCreationPayload;
import com.glop.users.in.restapi.payload.client.ClientRegistrationPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {
    private static final UUID CITY_ID = UUID.randomUUID();
    private static final AuthenticationModel TOKEN = AuthenticationModel.bearer("token");

    @Mock
    private ClientPortIn clientPortIn;
    private ClientController controller;

    @BeforeEach
    void setUp() {
        AuthMapperImpl authMapper = new AuthMapperImpl();
        ClientMapperImpl clientMapper = new ClientMapperImpl();
        ReflectionTestUtils.setField(clientMapper, "authMapper", authMapper);
        controller = new ClientController(clientPortIn, clientMapper, authMapper);
    }

    @Test
    void testRegisterShouldMapPayloadAndReturnToken() {
        when(clientPortIn.register(any())).thenReturn(TOKEN);
        UserCreationPayload account = new UserCreationPayload();
        account.setEmail("client@glop.fr");
        ClientRegistrationPayload payload = new ClientRegistrationPayload();
        payload.setAccount(account);
        payload.setCityId(CITY_ID);

        TokenResponse response = controller.register(payload);

        ArgumentCaptor<ClientModel> captor = ArgumentCaptor.forClass(ClientModel.class);
        verify(clientPortIn).register(captor.capture());
        assertThat(captor.getValue().getUserModel().getEmail()).isEqualTo("client@glop.fr");
        assertThat(captor.getValue().getCityModel().getId()).isEqualTo(CITY_ID);
        assertThat(response.getToken()).isEqualTo("token");
    }

    @Test
    void testCreateShouldUseConnectedUser() {
        UUID userId = UUID.randomUUID();
        when(clientPortIn.create(eq(userId), any())).thenReturn(TOKEN);
        CustomUserDetails me = CustomUserDetails.builder().userId(userId).email("x@glop.fr").authorities(List.of()).build();
        ClientCreationPayload payload = new ClientCreationPayload();
        payload.setCityId(CITY_ID);

        TokenResponse response = controller.create(me, payload);

        assertThat(response.getToken()).isEqualTo("token");
    }
}
