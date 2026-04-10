package com.devahn.dto.auth;

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
@Schema(description = "회원가입 요청")
public class SignupRequest {

    @Schema(description = "사용자명", example = "john_doe", required = true)
    @NotBlank(message = "사용자명은 필수입니다")
    @Size(min = 3, max = 20, message = "사용자명은 3자 이상 20자 이하여야 합니다")
    private String username;

    @Schema(description = "비밀번호", example = "password123!", required = true)
    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 6, max = 40, message = "비밀번호는 6자 이상 40자 이하여야 합니다")
    private String password;

    @Schema(description = "이메일", example = "john@example.com", required = true)
    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "유효한 이메일 형식이어야 합니다")
    private String email;
}
