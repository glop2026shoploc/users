package com.glop.users.out.catalogue;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.model.ShopkeeperStatus;
import com.glop.users.domain.model.address.AddressModel;
import com.glop.users.domain.model.address.CityModel;
import com.glop.users.exception.CatalogueException;
import com.glop.users.exception.credential.ShopAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class CatalogueShopAdapterOutTest {
    private static final String URL = "http://catalogue/api/catalogue";
    private static final UUID SHOP_ID = UUID.fromString("00000000-0000-0000-0000-000000000401");
    private static final UUID CATEGORY_ID = UUID.fromString("00000000-0000-0000-0000-000000000201");
    private static final UUID CITY_ID = UUID.fromString("00000000-0000-0000-0000-000000000101");
    private static final UUID ADDRESS_ID = UUID.fromString("00000000-0000-0000-0000-000000000301");

    private MockRestServiceServer server;
    private CatalogueShopAdapterOut adapter;
    private ShopkeeperModel shop;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        adapter = new CatalogueShopAdapterOut(builder, URL);

        shop = new ShopkeeperModel();
        shop.setCompanyName("MonCommerce");
        shop.setSiret("12345678900013");
        shop.setCategoryId(CATEGORY_ID);
        shop.setAddress(AddressModel.builder()
                .road("rue de la République").number("12 bis").complement("Bât. B")
                .cityModel(CityModel.builder().id(CITY_ID).build())
                .build());
    }

    @Test
    void testCreateShouldSendShopAndMapResponse() {
        server.expect(requestTo(URL + "/shops"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(jsonPath("$.name").value("MonCommerce"))
                .andExpect(jsonPath("$.siret").value("12345678900013"))
                .andExpect(jsonPath("$.categoryIds[0]").value(CATEGORY_ID.toString()))
                .andExpect(jsonPath("$.address.cityId").value(CITY_ID.toString()))
                .andRespond(withStatus(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body("""
                        {"shopId":"%s","name":"MonCommerce","siret":"12345678900013","categoryIds":["%s"],
                         "address":{"addressId":"%s","road":"rue de la République","number":"12 bis",
                                    "complement":"Bât. B","cityId":"%s"},
                         "status":"VALIDATED"}
                        """.formatted(SHOP_ID, CATEGORY_ID, ADDRESS_ID, CITY_ID)));

        ShopkeeperModel created = adapter.create(shop);

        server.verify();
        assertThat(created.getShopId()).isEqualTo(SHOP_ID);
        assertThat(created.getCompanyName()).isEqualTo("MonCommerce");
        assertThat(created.getSiret()).isEqualTo("12345678900013");
        assertThat(created.getCategoryId()).isEqualTo(CATEGORY_ID);
        assertThat(created.getStatus()).isEqualTo(ShopkeeperStatus.VALIDATED);
        assertThat(created.getAddress().getId()).isEqualTo(ADDRESS_ID);
        assertThat(created.getAddress().getRoad()).isEqualTo("rue de la République");
        assertThat(created.getAddress().getNumber()).isEqualTo("12 bis");
        assertThat(created.getAddress().getComplement()).isEqualTo("Bât. B");
        assertThat(created.getAddress().getCityModel().getId()).isEqualTo(CITY_ID);
    }

    @Test
    void testCreateWithoutCategoryAddressOrStatusShouldUseDefaults() {
        shop.setCategoryId(null);
        server.expect(requestTo(URL + "/shops"))
                .andExpect(jsonPath("$.categoryIds").isEmpty())
                .andRespond(withSuccess("{\"shopId\":\"%s\",\"categoryIds\":[]}".formatted(SHOP_ID),
                        MediaType.APPLICATION_JSON));

        ShopkeeperModel created = adapter.create(shop);

        assertThat(created.getCategoryId()).isNull();
        assertThat(created.getAddress()).isNull();
        assertThat(created.getStatus()).isEqualTo(ShopkeeperStatus.WAITING);
    }

    @Test
    void testCreateWithNullCategoriesShouldReturnNoCategory() {
        server.expect(requestTo(URL + "/shops"))
                .andRespond(withSuccess("{\"shopId\":\"%s\"}".formatted(SHOP_ID), MediaType.APPLICATION_JSON));

        assertThat(adapter.create(shop).getCategoryId()).isNull();
    }

    @Test
    void testCreateShouldThrowShopAlreadyExistsOnConflict() {
        server.expect(requestTo(URL + "/shops")).andRespond(withStatus(HttpStatus.CONFLICT));

        assertThatThrownBy(() -> adapter.create(shop)).isInstanceOf(ShopAlreadyExistsException.class);
    }

    @Test
    void testCreateShouldThrowCatalogueExceptionOnOtherError() {
        server.expect(requestTo(URL + "/shops")).andRespond(withBadRequest());

        assertThatThrownBy(() -> adapter.create(shop))
                .isInstanceOf(CatalogueException.class)
                .hasMessageContaining("400");
    }

    @Test
    void testCreateShouldThrowCatalogueExceptionWhenUnreachable() {
        server.expect(requestTo(URL + "/shops")).andRespond(withException(new java.net.ConnectException("refused")));

        assertThatThrownBy(() -> adapter.create(shop))
                .isInstanceOf(CatalogueException.class)
                .hasCauseInstanceOf(ResourceAccessException.class);
    }

    @Test
    void testCreateShouldThrowCatalogueExceptionOnEmptyBody() {
        server.expect(requestTo(URL + "/shops")).andRespond(withStatus(HttpStatus.CREATED));

        assertThatThrownBy(() -> adapter.create(shop)).isInstanceOf(CatalogueException.class);
    }
}
