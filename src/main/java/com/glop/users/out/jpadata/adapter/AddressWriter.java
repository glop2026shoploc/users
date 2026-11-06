package com.glop.users.out.jpadata.adapter;

import com.glop.users.domain.model.address.AddressModel;
import com.glop.users.out.jpadata.entity.AddressEntity;
import com.glop.users.out.jpadata.mapper.AddressOutMapper;
import com.glop.users.out.jpadata.repository.AddressRepository;
import com.glop.users.out.jpadata.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Enregistrement d'une adresse, partagé par les adapters client et commerçant. */
@Component
@RequiredArgsConstructor
class AddressWriter {
    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;
    private final AddressOutMapper addressOutMapper;

    AddressEntity save(AddressModel address) {
        AddressEntity entity = addressOutMapper.toAddressEntity(address);
        entity.setCity(cityRepository.getReferenceById(address.getCityModel().getId()));
        return addressRepository.save(entity);
    }
}
