package com.bishop.heber.voting.service;

import com.bishop.heber.voting.dto.AuthRequest;
import com.bishop.heber.voting.dto.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest request);
}
