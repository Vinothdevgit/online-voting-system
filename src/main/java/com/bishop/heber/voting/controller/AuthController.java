package com.bishop.heber.voting.controller;

import com.bishop.heber.voting.dto.AuthRequest;
import com.bishop.heber.voting.dto.AuthResponse;
import com.bishop.heber.voting.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        log.info("request from client {}",request);
        return ResponseEntity.ok(authService.login(request));
    }
}
