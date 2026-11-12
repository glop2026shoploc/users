package com.glop.users.out.catalogue;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.model.ShopkeeperStatus;
import com.glop.users.domain.model.address.AddressModel;
import com.glop.users.domain.model.address.CityModel;
import com.glop.users.domain.port.out.ShopPortOut;
import com.glop.users.exception.CatalogueException;
import com.glop.users.exception.credential.ShopAlreadyExistsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

/** Création des magasins via l'API REST du service catalogue. */
@Component
public class CatalogueShopAdapterOut implements ShopPortOut {
    private final RestClient restClient;

    public CatalogueShopAdapterOut(RestClient.Builder builder, @Value("${app.catalogue-url}") String catalogueUrl) {
        this.restClient = builder.baseUrl(catalogueUrl).build();
    }

    @Override
    public ShopkeeperModel create(ShopkeeperModel shop) {
        CatalogueShopResponse response;
        try {
            response = restClient.post()
                    .uri("/shops")
                    .body(toRequest(shop))
                    .retrieve()
                    .onStatus(status -> status.isSameCodeAs(HttpStatus.CONFLICT), (request, res) -> {
                        throw new ShopAlreadyExistsException("Un commerce existe déjà avec ce SIRET.");
                    })
                    .onStatus(HttpStatusCode::isError, (request, res) -> {
                        throw new CatalogueException("Le service catalogue a refusé le commerce (HTTP " + res.getStatusCode().value() + ").");
                    })
                    .body(CatalogueShopResponse.class);
        } catch (RestClientException e) {
            throw new CatalogueException("Service catalogue injoignable.", e);
        }
        if (response == null) {
            throw new CatalogueException("Réponse vide du service catalogue.");
        }
        return toModel(response);
    }

    private static CatalogueShopRequest toRequest(ShopkeeperModel shop) {
        AddressModel address = shop.getAddress();
        return new CatalogueShopRequest(
                shop.getCompanyName(),
                shop.getSiret(),
                shop.getCategoryId() == null ? List.of() : List.of(shop.getCategoryId()),
                new CatalogueShopRequest.Address(address.getRoad(), address.getNumber(), address.getComplement(),
                        address.getCityModel().getId()));
    }

    private static ShopkeeperModel toModel(CatalogueShopResponse response) {
        ShopkeeperModel model = new ShopkeeperModel();
        model.setShopId(response.shopId());
        model.setCompanyName(response.name());
        model.setSiret(response.siret());
        model.setCategoryId(response.categoryIds() == null || response.categoryIds().isEmpty()
                ? null : response.categoryIds().getFirst());
        if (response.address() != null) {
            CatalogueShopResponse.Address address = response.address();
            model.setAddress(AddressModel.builder()
                    .id(address.addressId())
                    .road(address.road())
                    .number(address.number())
                    .complement(address.complement())
                    .cityModel(CityModel.builder().id(address.cityId()).build())
                    .build());
        }
        model.setStatus(response.status() == null ? ShopkeeperStatus.WAITING : ShopkeeperStatus.valueOf(response.status()));
        return model;
    }
}
