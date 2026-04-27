package com.devahn.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Signup request")
public class SignupRequest {

    @Schema(description = "Username", example = "john_doe", required = true)
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @Schema(description = "Password", example = "password123!", required = true)
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
    private String password;

    @Schema(description = "Email", example = "john@example.com", required = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
}
