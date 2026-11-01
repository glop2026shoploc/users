package com.glop.users.in.restapi.controller;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.model.ShopkeeperStatus;
import com.glop.users.domain.port.in.ShopkeeperPortIn;
import com.glop.users.in.restapi.mapper.ShopkeeperMapperImpl;
import com.glop.users.in.restapi.payload.shopkeeper.ShopkeeperRegistrationPayload;
import com.glop.users.in.restapi.payload.shopkeeper.ShopkeeperResponsePayload;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShopkeeperControllerTest {

    @Mock
    private ShopkeeperPortIn shopkeeperPortIn;

    @Test
    void testRegisterShouldReturnRegisteredShopkeeper() throws BadRequestException {
        ShopkeeperController controller = new ShopkeeperController(shopkeeperPortIn, new ShopkeeperMapperImpl());

        ShopkeeperModel registered = new ShopkeeperModel();
        registered.setEmail("contact@boulangerie-dupont.fr");
        registered.setStatus(ShopkeeperStatus.WAITING);
        when(shopkeeperPortIn.register(any())).thenReturn(registered);

        ShopkeeperRegistrationPayload payload = new ShopkeeperRegistrationPayload();
        payload.setEmail("contact@boulangerie-dupont.fr");

        ShopkeeperResponsePayload response = controller.register(payload);

        assertThat(response.getEmail()).isEqualTo("contact@boulangerie-dupont.fr");
        assertThat(response.getStatus()).isEqualTo(ShopkeeperStatus.WAITING);
    }

    @Test
    void testGetWaitingShopkeepersShouldReturnShopkeepersWithId() {
        ShopkeeperController controller = new ShopkeeperController(shopkeeperPortIn, new ShopkeeperMapperImpl());

        ShopkeeperModel waiting = new ShopkeeperModel();
        waiting.setShopkeeperId(1L);
        waiting.setStatus(ShopkeeperStatus.WAITING);
        when(shopkeeperPortIn.getWaitingShopkeepers()).thenReturn(List.of(waiting));

        List<ShopkeeperResponsePayload> response = controller.getWaitingShopkeepers();

        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getShopkeeperId()).isEqualTo(1L);
    }
}
