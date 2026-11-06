package com.glop.users.in.restapi.payload.referential;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(name = "Category", description = "Catégorie de commerce")
public class CategoryPayload {
    @Schema(description = "Identifiant de la catégorie")
    private UUID id;

    @Schema(description = "Nom de la catégorie", example = "Boulangerie")
    private String name;
}
