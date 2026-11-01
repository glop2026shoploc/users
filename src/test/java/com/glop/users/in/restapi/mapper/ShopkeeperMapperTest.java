package com.glop.users.in.restapi.mapper;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.model.ShopkeeperStatus;
import com.glop.users.in.restapi.payload.shopkeeper.ShopkeeperRegistrationPayload;
import com.glop.users.in.restapi.payload.shopkeeper.ShopkeeperResponsePayload;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ShopkeeperMapperTest {

    private final ShopkeeperMapper mapper = new ShopkeeperMapperImpl();

    @Test
    void testToShopkeeperModelShouldMapAllFieldsAndIgnoreStatus() {
        ShopkeeperRegistrationPayload payload = new ShopkeeperRegistrationPayload();
        payload.setCompanyName("MonCommercant");
        payload.setCategory("ALIMENTATION");
        payload.setSiret("12345678900013");
        payload.setAddress("12 rue de la Republique, 59000 Lille");
        payload.setEmail("moncommercant@example.fr");
        payload.setPassword("AStrongPassword123!");

        ShopkeeperModel model = mapper.toShopkeeperModel(payload);

        assertThat(model.getCompanyName()).isEqualTo("MonCommercant");
        assertThat(model.getCategory()).isEqualTo("ALIMENTATION");
        assertThat(model.getSiret()).isEqualTo("12345678900013");
        assertThat(model.getAddress()).isEqualTo("12 rue de la Republique, 59000 Lille");
        assertThat(model.getEmail()).isEqualTo("moncommercant@example.fr");
        assertThat(model.getPassword()).isEqualTo("AStrongPassword123!");
        assertThat(model.getStatus()).isNull();
    }

    @Test
    void testToShopkeeperResponsePayloadShouldMapAllFields() {
        ShopkeeperModel model = new ShopkeeperModel();
        model.setCompanyName("MonCommercant");
        model.setCategory("ALIMENTATION");
        model.setSiret("12345678900013");
        model.setAddress("12 rue de la Republique, 59000 Lille");
        model.setEmail("moncommercant@example.fr");
        model.setStatus(ShopkeeperStatus.WAITING);

        ShopkeeperResponsePayload response = mapper.toShopkeeperResponsePayload(model);

        assertThat(response.getCompanyName()).isEqualTo("MonCommercant");
        assertThat(response.getCategory()).isEqualTo("ALIMENTATION");
        assertThat(response.getSiret()).isEqualTo("12345678900013");
        assertThat(response.getAddress()).isEqualTo("12 rue de la Republique, 59000 Lille");
        assertThat(response.getEmail()).isEqualTo("moncommercant@example.fr");
        assertThat(response.getStatus()).isEqualTo(ShopkeeperStatus.WAITING);
    }
}
