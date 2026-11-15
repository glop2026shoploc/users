package com.glop.users.in.restapi.controller;

import com.glop.users.domain.model.address.CityModel;
import com.glop.users.domain.port.in.ReferentialPortIn;
import com.glop.users.in.restapi.mapper.ReferentialMapperImpl;
import com.glop.users.in.restapi.payload.referential.CityLitePayload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReferentialControllerTest {
    @Mock
    private ReferentialPortIn referentialPortIn;

    @Test
    void testListCitiesShouldReturnIdAndName() {
        UUID cityId = UUID.randomUUID();
        when(referentialPortIn.listCities()).thenReturn(List.of(CityModel.builder().id(cityId).name("Lille").build()));
        ReferentialController controller = new ReferentialController(referentialPortIn, new ReferentialMapperImpl());

        List<CityLitePayload> cities = controller.listCities();

        assertThat(cities).hasSize(1);
        assertThat(cities.getFirst().getId()).isEqualTo(cityId);
        assertThat(cities.getFirst().getName()).isEqualTo("Lille");
    }
}
