package com.glop.users.in.restapi.mapper;

import com.glop.users.domain.model.ClientModel;
import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.in.restapi.payload.client.ClientCreationPayload;
import com.glop.users.in.restapi.payload.client.ClientRegistrationPayload;
import com.glop.users.in.restapi.payload.shopkeeper.ShopCreationPayload;
import com.glop.users.in.restapi.payload.shopkeeper.ShopkeeperRegistrationPayload;
import com.glop.users.in.restapi.payload.shopkeeper.ShopkeeperResponsePayload;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {AuthMapper.class},
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ClientMapper {


    @Mapping(target = "userModel", source = "account")
    @Mapping(target = "clientId", ignore = true)
    @Mapping(target = "cityModel.id", source = "cityId")
    ClientModel toClient(@Valid ClientRegistrationPayload account);

    @Mapping(target = "userModel", ignore = true )
    @Mapping(target = "clientId", ignore = true)
    @Mapping(target = "cityModel.id", source = "cityId")
    ClientModel toClient(@Valid ClientCreationPayload account);
}
