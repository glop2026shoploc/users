package com.glop.users.in.restapi.payload.shopkeeper;

import com.glop.users.domain.model.ShopkeeperStatus;
import com.glop.users.in.restapi.payload.address.AddressPayload;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(name = "ShopkeeperResponse", description = "Commerce créé")
public class ShopkeeperResponsePayload {
    private UUID shopId;
    @Schema(description = "Raison sociale du commerce", example = "MonCommercant")
    private String companyName;
    @Schema(description = "Numéro SIRET", example = "12345678900013")
    private String siret;
    private UUID categoryId;
    private AddressPayload address;

    @Schema(description = "Statut d'inscription", example = "WAITING")
    private ShopkeeperStatus status;
}
