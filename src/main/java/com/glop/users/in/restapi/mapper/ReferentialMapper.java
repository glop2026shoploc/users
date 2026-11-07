package com.glop.users.in.restapi.mapper;

import com.glop.users.domain.model.CategoryModel;
import com.glop.users.domain.model.address.CityModel;
import com.glop.users.in.restapi.payload.referential.CategoryPayload;
import com.glop.users.in.restapi.payload.referential.CityLitePayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ReferentialMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CityLitePayload toCityLitePayload(CityModel cityModel);

    List<CityLitePayload> toCityLitePayloads(List<CityModel> cityModels);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryPayload toCategoryPayload(CategoryModel categoryModel);

    List<CategoryPayload> toCategoryPayloads(List<CategoryModel> categoryModels);
}
