package com.glop.users.in.restapi.payload.referential;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(name = "CityLite", description = "Ville au format allégé")
public class CityLitePayload {
    @Schema(description = "Identifiant de la ville")
    private UUID id;

    @Schema(description = "Nom de la ville", example = "Lille")
    private String name;
}
