package com.glop.users.in.restapi.controller;

import com.glop.users.domain.port.in.ReferentialPortIn;
import com.glop.users.in.restapi.mapper.ReferentialMapper;
import com.glop.users.in.restapi.payload.referential.CityLitePayload;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Données de référence publiques, utilisées par les formulaires d'inscription. */
@RestController
@RequiredArgsConstructor
@SecurityRequirements
public class ReferentialController {
    private final ReferentialPortIn referentialPortIn;
    private final ReferentialMapper referentialMapper;

    @GetMapping("/cities")
    public List<CityLitePayload> listCities() {
        return referentialMapper.toCityLitePayloads(referentialPortIn.listCities());
    }
}
