package com.glop.users.domain.service;

import com.glop.users.domain.model.CategoryModel;
import com.glop.users.domain.model.address.CityModel;
import com.glop.users.domain.port.in.ReferentialPortIn;
import com.glop.users.domain.port.out.ReferentialPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReferentialService implements ReferentialPortIn {
    private final ReferentialPortOut referentialPortOut;

    @Override
    public List<CityModel> listCities() {
        return referentialPortOut.findAllCities();
    }

    @Override
    public List<CategoryModel> listCategories() {
        return referentialPortOut.findAllCategories();
    }
}
