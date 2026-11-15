package com.glop.users.out.jpadata.mapper;

import com.glop.users.domain.model.ClientModel;
import com.glop.users.domain.model.address.CityModel;
import com.glop.users.out.jpadata.entity.CityEntity;
import com.glop.users.out.jpadata.entity.ClientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = UserOutMapper.class, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ClientOutMapper {

    @Mapping(target = "clientId", source = "clientId")
    @Mapping(target = "userModel", source = "user")
    @Mapping(target = "cityModel", source = "city")
    ClientModel toClientModel(ClientEntity clientEntity);

    @Mapping(target = "id", source = "cityId")
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "country", ignore = true)
    CityModel toCityModel(CityEntity cityEntity);
}
