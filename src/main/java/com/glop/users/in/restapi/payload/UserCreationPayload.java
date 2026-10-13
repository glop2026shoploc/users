package com.glop.users.in.restapi.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(
        description = "User creation request",
        name = "UserCreationPayload"
)
public class UserCreationPayload {
    @NotBlank
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
    @Schema(
            description = "User password",
            example = "AStrongPassword",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;
}
