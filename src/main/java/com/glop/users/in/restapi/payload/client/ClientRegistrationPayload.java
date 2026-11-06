package com.glop.users.in.restapi.payload.client;

import com.glop.users.in.restapi.payload.address.AddressPayload;
import com.glop.users.in.restapi.payload.auth.UserCreationPayload;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "ClientRegistration", description = "Inscription d'un client : compte + adresse")
public class ClientRegistrationPayload {
    @Valid
    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private UserCreationPayload account;

    @Valid
    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private AddressPayload address;
}
