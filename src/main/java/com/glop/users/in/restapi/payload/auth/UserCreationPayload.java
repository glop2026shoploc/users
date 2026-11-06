package com.glop.users.in.restapi.payload.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        description = "User creation request",
        name = "UserCreationPayload"
)
public class UserCreationPayload {
    @NotBlank
    @Email
    @Size(max = 255)
    @Schema(
            description = "user email",
            example = "John.doe@gmail.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;
    @NotBlank
    @Schema(
            description = "user last name",
            example = "Doe",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String lastName;
    @NotBlank
    @Schema(
            description = "user first name",
            example = "John",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String firstName;

    @NotBlank
    @Size(min = 12, message = "Le mot de passe doit contenir au moins 12 caractères.")
    @Schema(
            description = "User password",
            example = "AStrongPassword123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;
}
