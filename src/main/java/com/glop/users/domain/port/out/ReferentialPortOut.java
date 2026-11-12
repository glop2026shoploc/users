package com.glop.users.domain.port.out;

import com.glop.users.domain.model.address.CityModel;

import java.util.List;
import java.util.UUID;

/** Données de référence (villes) qui doivent exister avant d'être utilisées. */
public interface ReferentialPortOut {
    boolean cityExists(UUID cityId);

    /** Villes triées par nom, sans leur pays. */
    List<CityModel> findAllCities();
}
