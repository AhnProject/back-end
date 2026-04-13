package com.devahn.auth.service;

import com.devahn.auth.dto.AuthResponse;
import com.devahn.auth.dto.LoginRequest;
import com.devahn.auth.dto.SignupRequest;

public interface AuthService {

    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);
}
