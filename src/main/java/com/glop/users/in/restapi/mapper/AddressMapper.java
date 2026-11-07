package com.glop.users.in.restapi.mapper;

import com.glop.users.domain.model.address.AddressModel;
import com.glop.users.domain.model.address.CityModel;
import com.glop.users.in.restapi.payload.address.AddressPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "road", source = "road")
    @Mapping(target = "number", source = "number")
    @Mapping(target = "complement", source = "complement")
    @Mapping(target = "cityModel", source = "cityId")
    AddressModel toAddressModel(AddressPayload addressPayload);

    @Mapping(target = "road", source = "road")
    @Mapping(target = "number", source = "number")
    @Mapping(target = "complement", source = "complement")
    @Mapping(target = "cityId", source = "cityModel.id")
    AddressPayload toAddressPayload(AddressModel addressModel);

    /** Le client n'envoie que l'id de la ville : le reste vient du référentiel. */
    default CityModel toCityModel(UUID cityId) {
        if (cityId == null) {
            return null;
        }
        return CityModel.builder().id(cityId).build();
    }
}
