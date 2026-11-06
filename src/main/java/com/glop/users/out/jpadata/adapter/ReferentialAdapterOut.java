package com.glop.users.out.jpadata.adapter;

import com.glop.users.domain.port.out.ReferentialPortOut;
import com.glop.users.out.jpadata.repository.CategoryRepository;
import com.glop.users.out.jpadata.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReferentialAdapterOut implements ReferentialPortOut {
    private final CategoryRepository categoryRepository;
    private final CityRepository cityRepository;

    @Override
    public boolean categoryExists(UUID categoryId) {
        return categoryId != null && categoryRepository.existsById(categoryId);
    }

    @Override
    public boolean cityExists(UUID cityId) {
        return cityId != null && cityRepository.existsById(cityId);
    }
}
