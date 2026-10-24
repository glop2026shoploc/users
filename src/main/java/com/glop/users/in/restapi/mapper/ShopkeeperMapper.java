package com.glop.users.in.restapi.mapper;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.in.restapi.payload.shopkeeper.ShopkeeperRegistrationPayload;
import com.glop.users.in.restapi.payload.shopkeeper.ShopkeeperResponsePayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShopkeeperMapper {

    @Mapping(target = "companyName", source = "companyName")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "siret", source = "siret")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "status", ignore = true)
    ShopkeeperModel toShopkeeperModel(ShopkeeperRegistrationPayload ShopkeeperRegistrationPayload);

    @Mapping(target = "companyName", source = "companyName")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "siret", source = "siret")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "status", source = "status")
    ShopkeeperResponsePayload toShopkeeperResponsePayload(ShopkeeperModel ShopkeeperModel);
    
}
