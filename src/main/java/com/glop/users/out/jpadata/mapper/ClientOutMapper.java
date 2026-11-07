package com.glop.users.out.jpadata.mapper;


import com.glop.users.domain.model.ClientModel;
import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.out.jpadata.entity.ClientEntity;
import com.glop.users.out.jpadata.entity.ShopEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {AddressOutMapper.class, UserOutMapper.class}, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ClientOutMapper {

    @Mapping(target = "clientId", source = "clientId")
    @Mapping(target = "userModel", source = "user")
    @Mapping(target = "cityModel", source = "city")
    ClientModel toClientModel(ClientEntity clientEntity);

}
