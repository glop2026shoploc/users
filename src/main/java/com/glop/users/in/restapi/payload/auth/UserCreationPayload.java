package com.glop.users.in.restapi.payload.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
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
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9\\s]).+$", message = "Le mot de passe doit contenir une minuscule, une majuscule, un chiffre et un caractère spécial.")
    @Schema(
            description = "User password",
            example = "AStrongPassword",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;

    @AssertTrue(message = "Le consentement est obligatoire pour créer un compte.")
    @Schema(description = "Consent to use personal data for account management", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean dataUsageConsent;
}
