package com.devahn.auth.service;

import com.devahn.app.security.JwtTokenProvider;
import com.devahn.auth.domain.User;
import com.devahn.auth.dto.AuthResponse;
import com.devahn.auth.dto.LoginRequest;
import com.devahn.auth.dto.SignupRequest;
import com.devahn.auth.repository.UserRepository;
import com.devahn.support.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw AuthException.usernameAlreadyExists(request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw AuthException.emailAlreadyExists(request.getEmail());
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role("USER")
                .build();

        Long userId = userRepository.save(user);
        log.info("User created: id={}, username={}", userId, user.getUsername());

        String token = jwtTokenProvider.generateToken(user.getUsername());

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            log.warn("Login failed: invalid credentials for {}", request.getUsername());
            throw AuthException.invalidCredentials();
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> AuthException.userNotFound(request.getUsername()));

        String token = jwtTokenProvider.generateToken(user.getUsername());

        log.info("Login succeeded: username={}", user.getUsername());

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
