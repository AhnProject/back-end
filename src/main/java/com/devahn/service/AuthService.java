package com.devahn.service;

import com.devahn.dto.auth.AuthResponse;
import com.devahn.dto.auth.LoginRequest;
import com.devahn.dto.auth.SignupRequest;

public interface AuthService {
    
    AuthResponse signup(SignupRequest request);
    
    AuthResponse login(LoginRequest request);
}
