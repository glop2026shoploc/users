package com.glop.users.in.restapi.payload.shopkeeper;

import com.glop.users.in.restapi.payload.address.AddressPayload;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(name = "ShopCreation", description = "Informations du commerce")
public class ShopCreationPayload {
    @NotBlank
    @Size(max = 150)
    @Schema(description = "Raison sociale du commerce", example = "MonCommercant",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String companyName;

    @Nullable
    @Schema(description = "Identifiant de la catégorie (référentiel)", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID categoryId;

    @NotBlank
    @Pattern(regexp = "\\d{14}", message = "Le SIRET doit contenir 14 chiffres.")
    @Schema(description = "Numéro SIRET", example = "12345678900013", requiredMode = Schema.RequiredMode.REQUIRED)
    private String siret;

    @Valid
    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private AddressPayload address;
}
