package com.glop.users.out.jpadata.mapper;

import com.glop.users.domain.model.CategoryModel;
import com.glop.users.domain.model.address.CityModel;
import com.glop.users.out.jpadata.entity.CategoryEntity;
import com.glop.users.out.jpadata.entity.CityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ReferentialOutMapper {

    /** Version allégée : le pays (LAZY) n'est pas chargé. */
    @Mapping(target = "id", source = "cityId")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "country", ignore = true)
    CityModel toCityModel(CityEntity cityEntity);

    List<CityModel> toCityModels(List<CityEntity> cityEntities);

    @Mapping(target = "id", source = "categoryId")
    @Mapping(target = "name", source = "name")
    CategoryModel toCategoryModel(CategoryEntity categoryEntity);

    List<CategoryModel> toCategoryModels(List<CategoryEntity> categoryEntities);
}
