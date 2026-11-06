package com.glop.users.in.restapi.payload.address;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(name = "Address", description = "Adresse postale")
public class AddressPayload {
    @NotBlank
    @Size(max = 255)
    @Schema(description = "Voie", example = "rue de la République", requiredMode = Schema.RequiredMode.REQUIRED)
    private String road;

    @Size(max = 20)
    @Schema(description = "Numéro dans la voie", example = "12 bis")
    private String number;

    @Size(max = 255)
    @Schema(description = "Complément d'adresse", example = "Bâtiment B")
    private String complement;

    @NotNull
    @Schema(description = "Identifiant de la ville (référentiel)", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID cityId;
}
