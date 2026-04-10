package com.devahn.dto.auth;

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
@Schema(description = "로그인 요청")
public class LoginRequest {

    @Schema(description = "사용자명", example = "john_doe", required = true)
    @NotBlank(message = "사용자명은 필수입니다")
    private String username;

    @Schema(description = "비밀번호", example = "password123!", required = true)
    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;
}
