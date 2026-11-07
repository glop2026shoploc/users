package com.glop.users.in.restapi.mapper;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.model.ShopkeeperStatus;
import com.glop.users.domain.model.address.AddressModel;
import com.glop.users.domain.model.address.CityModel;
import com.glop.users.in.restapi.payload.address.AddressPayload;
import com.glop.users.in.restapi.payload.auth.UserCreationPayload;
import com.glop.users.in.restapi.payload.shopkeeper.ShopCreationPayload;
import com.glop.users.in.restapi.payload.shopkeeper.ShopkeeperRegistrationPayload;
import com.glop.users.in.restapi.payload.shopkeeper.ShopkeeperResponsePayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ShopkeeperMapperTest {

    private static final UUID CATEGORY_ID = UUID.randomUUID();
    private static final UUID CITY_ID = UUID.randomUUID();

    private ShopkeeperMapper mapper;

    @BeforeEach
    void setUp() {
        ShopkeeperMapperImpl impl = new ShopkeeperMapperImpl();
        ReflectionTestUtils.setField(impl, "addressMapper", new AddressMapperImpl());
        ReflectionTestUtils.setField(impl, "authMapper", new AuthMapperImpl());
        mapper = impl;
    }

    private AddressPayload addressPayload() {
        AddressPayload address = new AddressPayload();
        address.setNumber("12");
        address.setRoad("rue de la Republique");
        address.setComplement("Batiment B");
        address.setCityId(CITY_ID);
        return address;
    }

    private void assertAddress(AddressModel address) {
        assertThat(address.getId()).isNull();
        assertThat(address.getNumber()).isEqualTo("12");
        assertThat(address.getRoad()).isEqualTo("rue de la Republique");
        assertThat(address.getComplement()).isEqualTo("Batiment B");
        assertThat(address.getCityModel().getId()).isEqualTo(CITY_ID);
    }

    @Test
    void testToShopkeeperModelFromRegistrationShouldMapAllFieldsAndOwner() {
        UserCreationPayload account = new UserCreationPayload();
        account.setEmail("moncommercant@example.fr");
        account.setFirstName("John");
        account.setLastName("Doe");
        account.setPassword("AStrongPassword123!");
        account.setDataUsageConsent(true);

        ShopkeeperRegistrationPayload payload = new ShopkeeperRegistrationPayload();
        payload.setAccount(account);
        payload.setCompanyName("MonCommercant");
        payload.setCategoryId(CATEGORY_ID);
        payload.setSiret("12345678900013");
        payload.setAddress(addressPayload());

        ShopkeeperModel model = mapper.toShopkeeperModel(payload);

        assertThat(model.getShopId()).isNull();
        assertThat(model.getCompanyName()).isEqualTo("MonCommercant");
        assertThat(model.getCategoryId()).isEqualTo(CATEGORY_ID);
        assertThat(model.getSiret()).isEqualTo("12345678900013");
        assertAddress(model.getAddress());
        assertThat(model.getStatus()).isNull();

        assertThat(model.getOwner().getUserId()).isNull();
        assertThat(model.getOwner().getEmail()).isEqualTo("moncommercant@example.fr");
        assertThat(model.getOwner().getFirstName()).isEqualTo("John");
        assertThat(model.getOwner().getLastName()).isEqualTo("Doe");
        assertThat(model.getOwner().getPassword()).isEqualTo("AStrongPassword123!");
        assertThat(model.getOwner().isDataUsageConsent()).isTrue();
    }

    @Test
    void testToShopkeeperModelFromCreationShouldMapAllFieldsWithoutOwner() {
        ShopCreationPayload payload = new ShopCreationPayload();
        payload.setCompanyName("MonCommercant");
        payload.setCategoryId(CATEGORY_ID);
        payload.setSiret("12345678900013");
        payload.setAddress(addressPayload());

        ShopkeeperModel model = mapper.toShopkeeperModel(payload);

        assertThat(model.getShopId()).isNull();
        assertThat(model.getOwner()).isNull();
        assertThat(model.getCompanyName()).isEqualTo("MonCommercant");
        assertThat(model.getCategoryId()).isEqualTo(CATEGORY_ID);
        assertThat(model.getSiret()).isEqualTo("12345678900013");
        assertAddress(model.getAddress());
        assertThat(model.getStatus()).isNull();
    }

    @Test
    void testToShopkeeperResponsePayloadShouldMapAllFields() {
        UUID shopId = UUID.randomUUID();
        ShopkeeperModel model = new ShopkeeperModel();
        model.setShopId(shopId);
        model.setCompanyName("MonCommercant");
        model.setCategoryId(CATEGORY_ID);
        model.setSiret("12345678900013");
        model.setAddress(AddressModel.builder()
                .number("12")
                .road("rue de la Republique")
                .complement("Batiment B")
                .cityModel(CityModel.builder().id(CITY_ID).build())
                .build());
        model.setStatus(ShopkeeperStatus.WAITING);

        ShopkeeperResponsePayload response = mapper.toShopkeeperResponsePayload(model);

        assertThat(response.getShopId()).isEqualTo(shopId);
        assertThat(response.getCompanyName()).isEqualTo("MonCommercant");
        assertThat(response.getCategoryId()).isEqualTo(CATEGORY_ID);
        assertThat(response.getSiret()).isEqualTo("12345678900013");
        assertThat(response.getAddress().getNumber()).isEqualTo("12");
        assertThat(response.getAddress().getRoad()).isEqualTo("rue de la Republique");
        assertThat(response.getAddress().getComplement()).isEqualTo("Batiment B");
        assertThat(response.getAddress().getCityId()).isEqualTo(CITY_ID);
        assertThat(response.getStatus()).isEqualTo(ShopkeeperStatus.WAITING);
    }
}