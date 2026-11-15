package com.glop.users.domain.service;

import com.glop.users.domain.model.AuthenticationModel;
import com.glop.users.domain.model.ClientModel;
import com.glop.users.domain.model.UserModel;
import com.glop.users.domain.model.UserRole;
import com.glop.users.domain.model.address.CityModel;
import com.glop.users.domain.port.out.ClientPortOut;
import com.glop.users.domain.port.out.ReferentialPortOut;
import com.glop.users.domain.port.out.UserPortOut;
import com.glop.users.exception.UnknownReferenceException;
import com.glop.users.exception.UserNotFoundException;
import com.glop.users.exception.credential.ClientAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID CITY_ID = UUID.randomUUID();
    private static final AuthenticationModel TOKEN = AuthenticationModel.bearer("token");

    @Mock
    private AccountService accountService;
    @Mock
    private AuthService authService;
    @Mock
    private ClientPortOut clientPortOut;
    @Mock
    private ReferentialPortOut referentialPortOut;
    @Mock
    private UserPortOut userPortOut;
    @InjectMocks
    private ClientService clientService;

    private ClientModel client;
    private UserModel user;

    @BeforeEach
    void setUp() {
        user = new UserModel();
        user.setUserId(USER_ID);
        client = ClientModel.builder()
                .userModel(new UserModel())
                .cityModel(CityModel.builder().id(CITY_ID).build())
                .build();
    }

    @Test
    void testRegisterShouldCreateAccountAndClientThenReturnToken() {
        when(referentialPortOut.cityExists(CITY_ID)).thenReturn(true);
        when(accountService.createAccount(any())).thenReturn(user);
        when(authService.login(USER_ID, UserRole.CLIENT)).thenReturn(TOKEN);

        assertThat(clientService.register(client)).isEqualTo(TOKEN);
        assertThat(client.getUserModel()).isEqualTo(user);
        verify(clientPortOut).create(client);
    }

    @Test
    void testRegisterShouldThrowWhenCityIsUnknown() {
        when(referentialPortOut.cityExists(CITY_ID)).thenReturn(false);

        assertThatThrownBy(() -> clientService.register(client)).isInstanceOf(UnknownReferenceException.class);
        verifyNoInteractions(accountService, clientPortOut);
    }

    @Test
    void testCreateShouldAttachClientToExistingUser() {
        when(referentialPortOut.cityExists(CITY_ID)).thenReturn(true);
        when(userPortOut.findById(USER_ID)).thenReturn(Optional.of(user));
        when(clientPortOut.existsByUser(USER_ID)).thenReturn(false);
        when(authService.login(USER_ID, UserRole.CLIENT)).thenReturn(TOKEN);

        assertThat(clientService.create(USER_ID, client)).isEqualTo(TOKEN);
        verify(clientPortOut).create(client);
    }

    @Test
    void testCreateShouldThrowWhenUserNotFound() {
        when(referentialPortOut.cityExists(CITY_ID)).thenReturn(true);
        when(userPortOut.findById(USER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.create(USER_ID, client)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void testCreateShouldThrowWhenUserIsAlreadyClient() {
        when(referentialPortOut.cityExists(CITY_ID)).thenReturn(true);
        when(userPortOut.findById(USER_ID)).thenReturn(Optional.of(user));
        when(clientPortOut.existsByUser(USER_ID)).thenReturn(true);

        assertThatThrownBy(() -> clientService.create(USER_ID, client)).isInstanceOf(ClientAlreadyExistsException.class);
        verify(clientPortOut, never()).create(any());
    }
}
