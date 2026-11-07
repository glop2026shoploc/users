package com.glop.users.out.jpadata.adapter;

import com.glop.users.domain.model.CategoryModel;
import com.glop.users.domain.model.address.CityModel;
import com.glop.users.domain.port.out.ReferentialPortOut;
import com.glop.users.out.jpadata.mapper.ReferentialOutMapper;
import com.glop.users.out.jpadata.repository.CategoryRepository;
import com.glop.users.out.jpadata.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReferentialAdapterOut implements ReferentialPortOut {
    private static final Sort BY_NAME = Sort.by("name");

    private final CategoryRepository categoryRepository;
    private final CityRepository cityRepository;
    private final ReferentialOutMapper referentialOutMapper;

    @Override
    public boolean categoryExists(UUID categoryId) {
        return categoryId != null && categoryRepository.existsById(categoryId);
    }

    @Override
    public boolean cityExists(UUID cityId) {
        return cityId != null && cityRepository.existsById(cityId);
    }

    @Override
    public List<CityModel> findAllCities() {
        return referentialOutMapper.toCityModels(cityRepository.findAll(BY_NAME));
    }

    @Override
    public List<CategoryModel> findAllCategories() {
        return referentialOutMapper.toCategoryModels(categoryRepository.findAll(BY_NAME));
    }
}
