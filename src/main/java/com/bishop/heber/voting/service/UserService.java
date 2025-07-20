package com.bishop.heber.voting.service;

import com.bishop.heber.voting.dto.UserRegistrationRequest;

public interface UserService {
    void registerUser(UserRegistrationRequest request);
}
