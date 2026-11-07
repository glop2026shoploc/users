package com.glop.users.out.jpadata.mapper;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.out.jpadata.entity.ShopEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = AddressOutMapper.class, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ShopkeeperOutMapper {

    @Mapping(target = "shopId", source = "shopId")
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "companyName", source = "name")
    @Mapping(target = "siret", source = "siretNumber")
    @Mapping(target = "categoryId", source = "category.categoryId")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "status", source = "status")
    ShopkeeperModel toShopkeeperModel(ShopEntity shopEntity);

    /** La catégorie et l'adresse sont rattachées par l'adapter. */
    @Mapping(target = "shopId", ignore = true)
    @Mapping(target = "name", source = "companyName")
    @Mapping(target = "siretNumber", source = "siret")
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "status", source = "status")
    ShopEntity toShopEntity(ShopkeeperModel shopkeeperModel);
}
