package com.devahn.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Login request")
public class LoginRequest {

    @Schema(description = "Username", example = "john_doe", required = true)
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "Password", example = "password123!", required = true)
    @NotBlank(message = "Password is required")
    private String password;
}
