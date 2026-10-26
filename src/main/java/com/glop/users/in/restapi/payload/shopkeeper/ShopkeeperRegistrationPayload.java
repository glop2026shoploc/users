package com.glop.users.in.restapi.payload.shopkeeper;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(
        description = "Shopkeeper registration request",
        name = "ShopkeeperRegistrationPayload"
)
public class ShopkeeperRegistrationPayload {
    @NotBlank
    @Schema(description = "Raison sociale du commerce",
            example = "MonCommercant",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String companyName;

    @NotBlank
    @Schema(description = "Catégorie du commerce",
            example = "Boulangerie",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String category;

    @NotBlank
    @Schema(description = "Numéro SIRET",
            example = "12345678900013",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String siret;

    @NotBlank
    @Schema(description = "Adresse postale du commerce",
            example = "12 rue de la République, 59000 Lille",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;

    @NotBlank
    @Schema(description = "Email du commerçant",
            example = "moncommercant@example.fr",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank
    @Schema(description = "Mot de passe",
            example = "AStrongPassword",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
