package com.glop.users.in.restapi.controller;

import com.glop.users.config.security.CustomUserDetails;
import com.glop.users.domain.model.AuthenticationModel;
import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.model.ShopkeeperStatus;
import com.glop.users.domain.port.in.ShopkeeperPortIn;
import com.glop.users.in.restapi.mapper.AddressMapperImpl;
import com.glop.users.in.restapi.mapper.AuthMapperImpl;
import com.glop.users.in.restapi.mapper.ShopkeeperMapperImpl;
import com.glop.users.in.restapi.payload.auth.TokenResponse;
import com.glop.users.in.restapi.payload.auth.UserCreationPayload;
import com.glop.users.in.restapi.payload.shopkeeper.ShopCreationPayload;
import com.glop.users.in.restapi.payload.shopkeeper.ShopkeeperRegistrationPayload;
import com.glop.users.in.restapi.payload.shopkeeper.ShopkeeperResponsePayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShopkeeperControllerTest {

    @Mock
    private ShopkeeperPortIn shopkeeperPortIn;

    private ShopkeeperController controller;

    @BeforeEach
    void setUp() {
        AuthMapperImpl authMapper = new AuthMapperImpl();
        ShopkeeperMapperImpl shopkeeperMapper = new ShopkeeperMapperImpl();
        ReflectionTestUtils.setField(shopkeeperMapper, "addressMapper", new AddressMapperImpl());
        ReflectionTestUtils.setField(shopkeeperMapper, "authMapper", authMapper);
        controller = new ShopkeeperController(shopkeeperPortIn, shopkeeperMapper, authMapper);
    }

    @Test
    void testRegisterShouldReturnToken() {
        AuthenticationModel authentication = AuthenticationModel.builder().type("Bearer").token("token").build();
        when(shopkeeperPortIn.register(any())).thenReturn(authentication);

        UserCreationPayload account = new UserCreationPayload();
        account.setEmail("contact@boulangerie-dupont.fr");
        ShopkeeperRegistrationPayload payload = new ShopkeeperRegistrationPayload();
        payload.setAccount(account);
        payload.setCompanyName("Boulangerie Dupont");

        TokenResponse response = controller.register(payload);

        assertThat(response.getToken()).isEqualTo("token");
        assertThat(response.getType()).isEqualTo("Bearer");

        ArgumentCaptor<ShopkeeperModel> captor = ArgumentCaptor.forClass(ShopkeeperModel.class);
        verify(shopkeeperPortIn).register(captor.capture());
        assertThat(captor.getValue().getOwner().getEmail()).isEqualTo("contact@boulangerie-dupont.fr");
        assertThat(captor.getValue().getCompanyName()).isEqualTo("Boulangerie Dupont");
    }

    @Test
    void testCreateShouldReturnCreatedShop() {
        UUID userId = UUID.randomUUID();
        UUID shopId = UUID.randomUUID();
        CustomUserDetails principal = CustomUserDetails.builder()
                .userId(userId)
                .email("contact@boulangerie-dupont.fr")
                .authorities(List.of())
                .build();

        ShopkeeperModel created = new ShopkeeperModel();
        created.setShopId(shopId);
        created.setCompanyName("Boulangerie Dupont");
        created.setStatus(ShopkeeperStatus.WAITING);
        when(shopkeeperPortIn.create(eq(userId), any())).thenReturn(created);

        ShopCreationPayload payload = new ShopCreationPayload();
        payload.setCompanyName("Boulangerie Dupont");

        ShopkeeperResponsePayload response = controller.create(principal, payload);

        assertThat(response.getShopId()).isEqualTo(shopId);
        assertThat(response.getCompanyName()).isEqualTo("Boulangerie Dupont");
        assertThat(response.getStatus()).isEqualTo(ShopkeeperStatus.WAITING);
    }
}