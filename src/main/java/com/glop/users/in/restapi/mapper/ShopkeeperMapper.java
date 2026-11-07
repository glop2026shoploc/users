package com.glop.users.in.restapi.mapper;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.in.restapi.payload.shopkeeper.ShopCreationPayload;
import com.glop.users.in.restapi.payload.shopkeeper.ShopkeeperRegistrationPayload;
import com.glop.users.in.restapi.payload.shopkeeper.ShopkeeperResponsePayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, AuthMapper.class},
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ShopkeeperMapper {

    /** Utilisateur connecté : le propriétaire est ajouté par le service. */
    @Mapping(target = "shopId", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "companyName", source = "companyName")
    @Mapping(target = "siret", source = "siret")
    @Mapping(target = "categoryId", source = "categoryId")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "status", ignore = true)
    ShopkeeperModel toShopkeeperModel(ShopCreationPayload creationPayload);

    /** Utilisateur non connecté : le propriétaire vient du compte à créer. */
    @Mapping(target = "shopId", ignore = true)
    @Mapping(target = "owner", source = "account")
    @Mapping(target = "companyName", source = "companyName")
    @Mapping(target = "siret", source = "siret")
    @Mapping(target = "categoryId", source = "categoryId")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "status", ignore = true)
    ShopkeeperModel toShopkeeperModel(ShopkeeperRegistrationPayload registrationPayload);

    @Mapping(target = "shopId", source = "shopId")
    @Mapping(target = "companyName", source = "companyName")
    @Mapping(target = "siret", source = "siret")
    @Mapping(target = "categoryId", source = "categoryId")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "status", source = "status")
    ShopkeeperResponsePayload toShopkeeperResponsePayload(ShopkeeperModel shopkeeperModel);
}
