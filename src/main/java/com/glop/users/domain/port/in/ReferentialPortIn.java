package com.glop.users.domain.port.in;

import com.glop.users.domain.model.address.CityModel;

import java.util.List;

public interface ReferentialPortIn {
    List<CityModel> listCities();
}
