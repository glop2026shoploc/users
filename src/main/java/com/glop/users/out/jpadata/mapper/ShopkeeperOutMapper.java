package com.glop.users.out.jpadata.mapper;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.out.jpadata.entity.ShopkeeperEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ShopkeeperOutMapper {
    @Mapping(target = "companyName", source = "companyName")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "siret", source = "siret")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "encodedPassword", source = "password")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "status", source = "status")
    public abstract ShopkeeperModel toShopkeeperModel(ShopkeeperEntity shopkeeperEntity);

    @Mapping(target = "shopkeeperId", ignore = true)
    @Mapping(target = "companyName", source = "companyName")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "siret", source = "siret")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "encodedPassword")
    @Mapping(target = "status", source = "status")
    public abstract ShopkeeperEntity toShopkeeperEntity(ShopkeeperModel shopkeeperModel);
}