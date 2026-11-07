package com.glop.users.domain.model;

import com.glop.users.domain.model.address.AddressModel;
import com.glop.users.domain.model.address.CityModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientModel {
    UUID clientId;
    UserModel userModel;
    CityModel cityModel;
}
