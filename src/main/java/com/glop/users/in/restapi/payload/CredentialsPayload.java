package com.glop.users.in.restapi.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(
        name = "Credentials",
        description = "Required for login request"
)
@Data
public class CredentialsPayload {
    @NotBlank
    @Schema(
            description = "user email",
            example = "John.doe@gmail.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;
    @NotBlank
    @Schema(
            description = "User password",
            example = "AStrongPassword",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;
}
