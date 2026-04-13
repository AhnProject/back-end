package com.devahn.auth.controller;

import com.devahn.app.api.ApiResponse;
import com.devahn.auth.dto.AuthResponse;
import com.devahn.auth.dto.LoginRequest;
import com.devahn.auth.dto.SignupRequest;
import com.devahn.auth.service.AuthService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Sign up", description = "Register a user and issue a JWT token.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or duplicate user")
    })
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AuthResponse> signup(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Signup request body",
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
        log.info("Signup request: username={}", request.getUsername());
        AuthResponse response = authService.signup(request);
        return ApiResponse.ok(response, "Signup completed");
    }

    @Operation(summary = "Login", description = "Authenticate a user and issue a JWT token.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login succeeded"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authentication failed")
    })
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Login request body",
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
        log.info("Login request: username={}", request.getUsername());
        AuthResponse response = authService.login(request);
        return ApiResponse.ok(response, "Login succeeded");
    }
}
