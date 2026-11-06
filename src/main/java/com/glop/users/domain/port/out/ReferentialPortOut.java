package com.glop.users.domain.port.out;

import com.glop.users.domain.model.CategoryModel;
import com.glop.users.domain.model.address.CityModel;

import java.util.List;
import java.util.UUID;

/** Données de référence (catégories, villes) qui doivent exister avant d'être utilisées. */
public interface ReferentialPortOut {
    boolean categoryExists(UUID categoryId);

    boolean cityExists(UUID cityId);

    /** Villes triées par nom, sans leur pays. */
    List<CityModel> findAllCities();

    /** Catégories triées par nom. */
    List<CategoryModel> findAllCategories();
}
