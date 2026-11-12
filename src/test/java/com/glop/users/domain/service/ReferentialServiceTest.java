package com.glop.users.domain.service;

import com.glop.users.domain.model.address.CityModel;
import com.glop.users.domain.port.out.ReferentialPortOut;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReferentialServiceTest {
    @Mock
    private ReferentialPortOut referentialPortOut;
    @InjectMocks
    private ReferentialService referentialService;

    @Test
    void testListCitiesShouldReturnCitiesFromPort() {
        List<CityModel> cities = List.of(CityModel.builder().id(UUID.randomUUID()).name("Lille").build());
        when(referentialPortOut.findAllCities()).thenReturn(cities);

        assertThat(referentialService.listCities()).isEqualTo(cities);
    }
}
