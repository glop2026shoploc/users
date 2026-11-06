package com.glop.users.out.jpadata.mapper;

import com.glop.users.domain.model.address.AddressModel;
import com.glop.users.domain.model.address.CityModel;
import com.glop.users.out.jpadata.entity.AddressEntity;
import com.glop.users.out.jpadata.entity.CityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AddressOutMapper {

    @Mapping(target = "id", source = "addressId")
    @Mapping(target = "road", source = "road")
    @Mapping(target = "number", source = "number")
    @Mapping(target = "complement", source = "complement")
    @Mapping(target = "cityModel", source = "city")
    AddressModel toAddressModel(AddressEntity addressEntity);

    /** Seul l'id est repris : la ville est un proxy LAZY, lire son nom déclencherait une requête. */
    @Mapping(target = "id", source = "cityId")
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "country", ignore = true)
    CityModel toCityModel(CityEntity cityEntity);

    /** La ville est rattachée par AddressWriter à partir de son id. */
    @Mapping(target = "addressId", ignore = true)
    @Mapping(target = "road", source = "road")
    @Mapping(target = "number", source = "number")
    @Mapping(target = "complement", source = "complement")
    @Mapping(target = "city", ignore = true)
    AddressEntity toAddressEntity(AddressModel addressModel);
}
