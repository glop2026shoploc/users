package com.glop.users.out.jpadata.adapter;

import com.glop.users.domain.model.address.CityModel;
import com.glop.users.out.jpadata.entity.CityEntity;
import com.glop.users.out.jpadata.mapper.ReferentialOutMapper;
import com.glop.users.out.jpadata.repository.CityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReferentialAdapterOutTest {
    private static final UUID CITY_ID = UUID.randomUUID();

    @Mock
    private CityRepository cityRepository;
    @Mock
    private ReferentialOutMapper referentialOutMapper;
    @InjectMocks
    private ReferentialAdapterOut adapter;

    @Test
    void testCityExistsShouldAskRepository() {
        when(cityRepository.existsById(CITY_ID)).thenReturn(true);

        assertThat(adapter.cityExists(CITY_ID)).isTrue();
    }

    @Test
    void testCityExistsWithNullIdShouldReturnFalse() {
        assertThat(adapter.cityExists(null)).isFalse();
        verifyNoInteractions(cityRepository);
    }

    @Test
    void testFindAllCitiesShouldReturnCitiesSortedByName() {
        List<CityEntity> entities = List.of(new CityEntity());
        List<CityModel> models = List.of(CityModel.builder().id(CITY_ID).build());
        when(cityRepository.findAll(Sort.by("name"))).thenReturn(entities);
        when(referentialOutMapper.toCityModels(entities)).thenReturn(models);

        assertThat(adapter.findAllCities()).isEqualTo(models);
    }
}
