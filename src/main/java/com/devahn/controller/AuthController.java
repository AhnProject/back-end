package com.devahn.controller;

import com.devahn.common.ApiResponse;
import com.devahn.dto.auth.AuthResponse;
import com.devahn.dto.auth.LoginRequest;
import com.devahn.dto.auth.SignupRequest;
import com.devahn.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "회원가입 및 로그인 API")
public class AuthController {

    private final AuthService authService;

    @Operation(
        summary = "회원가입",
        description = "새로운 사용자를 등록합니다. 사용자명과 이메일은 중복될 수 없습니다."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "회원가입 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "data": {
                        "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                        "tokenType": "Bearer",
                        "username": "john_doe",
                        "email": "john@example.com",
                        "role": "USER"
                      },
                      "message": "회원가입이 완료되었습니다",
                      "errorCode": null,
                      "timestamp": 1234567890
                    }
                    """)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "잘못된 입력 또는 중복된 사용자명/이메일",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AuthResponse> signup(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "회원가입 정보",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = SignupRequest.class),
                    examples = @ExampleObject(value = """
                        {
                          "username": "john_doe",
                          "password": "password123!",
                          "email": "john@example.com"
                        }
                        """)
                )
            )
            @Valid @RequestBody SignupRequest request) {
        log.info("회원가입 요청 - username: {}", request.getUsername());
        AuthResponse response = authService.signup(request);
        return ApiResponse.ok(response, "회원가입이 완료되었습니다");
    }

    @Operation(
        summary = "로그인",
        description = "사용자명과 비밀번호로 로그인하여 JWT 토큰을 발급받습니다."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "data": {
                        "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                        "tokenType": "Bearer",
                        "username": "john_doe",
                        "email": "john@example.com",
                        "role": "USER"
                      },
                      "message": "로그인 성공",
                      "errorCode": null,
                      "timestamp": 1234567890
                    }
                    """)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "잘못된 사용자명 또는 비밀번호",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "로그인 정보",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = LoginRequest.class),
                    examples = @ExampleObject(value = """
                        {
                          "username": "john_doe",
                          "password": "password123!"
                        }
                        """)
                )
            )
            @Valid @RequestBody LoginRequest request) {
        log.info("로그인 요청 - username: {}", request.getUsername());
        AuthResponse response = authService.login(request);
        return ApiResponse.ok(response, "로그인 성공");
    }
}
