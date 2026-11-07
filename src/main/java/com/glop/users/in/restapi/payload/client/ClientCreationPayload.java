package com.glop.users.in.restapi.payload.client;

import com.glop.users.in.restapi.payload.address.AddressPayload;
import com.glop.users.in.restapi.payload.auth.UserCreationPayload;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "ClientCreation", description = "Inscription d'un client : adresse seulement")
public class ClientCreationPayload {
    @Valid
    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private AddressPayload address;
}
