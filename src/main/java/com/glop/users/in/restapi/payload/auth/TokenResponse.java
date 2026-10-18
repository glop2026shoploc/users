package com.glop.users.in.restapi.payload.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(name = "TokenResponse", description = "Response from authentication endpoint")
@Data
public class TokenResponse {
    @NotBlank
    @Schema(
            description = "Authorization type",
            example = "Bearer",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String type;
    @NotBlank
    @Schema(
            description = "Token",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String token;
}
