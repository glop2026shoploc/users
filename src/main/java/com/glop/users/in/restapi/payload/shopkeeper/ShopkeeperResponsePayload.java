package com.glop.users.in.restapi.payload.shopkeeper;

import com.glop.users.domain.model.ShopkeeperStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "ShopkeeperResponse", description = "Commercant après inscription")
public class ShopkeeperResponsePayload {
    @Schema(description = "Raison sociale du commerce", example = "MonCommercant")
    private String companyName;
    @Schema(description = "Catégorie du commerce", example = "Boulangerie")
    private String category;
    @Schema(description = "Numéro SIRET", example = "12345678900013")
    private String siret;
    @Schema(description = "Adresse postale du commerce", example = "12 rue de la République, 59000 Lille")
    private String address;
    @Schema(description = "Email du commerçant", example = "moncommercant@example.fr")
    private String email;
    @Schema(description = "Statut d'inscription", example = "WAITING")
    private ShopkeeperStatus status;
}
