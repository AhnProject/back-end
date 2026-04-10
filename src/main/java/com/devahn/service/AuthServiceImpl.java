package com.devahn.service;

import com.devahn.domain.user.User;
import com.devahn.dto.auth.AuthResponse;
import com.devahn.dto.auth.LoginRequest;
import com.devahn.dto.auth.SignupRequest;
import com.devahn.exception.AuthException;
import com.devahn.repository.UserRepository;
import com.devahn.security.JwtTokenProvider;
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
        // 중복 체크
        if (userRepository.existsByUsername(request.getUsername())) {
            throw AuthException.usernameAlreadyExists(request.getUsername());
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw AuthException.emailAlreadyExists(request.getEmail());
        }

        // 사용자 생성
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role("USER")
                .build();

        Long userId = userRepository.save(user);
        log.info("사용자 생성 완료 - ID: {}, Username: {}", userId, user.getUsername());

        // JWT 토큰 생성
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
            // 인증 수행
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            log.warn("로그인 실패 - 잘못된 인증 정보: {}", request.getUsername());
            throw AuthException.invalidCredentials();
        }

        // 사용자 정보 조회
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> AuthException.userNotFound(request.getUsername()));

        // JWT 토큰 생성
        String token = jwtTokenProvider.generateToken(user.getUsername());
        
        log.info("로그인 성공 - Username: {}", user.getUsername());

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
