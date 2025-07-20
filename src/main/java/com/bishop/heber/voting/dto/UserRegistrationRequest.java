package com.bishop.heber.voting.dto;

public record UserRegistrationRequest(
        String username,
        String password,
        String fullName,
        String role // e.g., "USER" or "ADMIN"
) {}
