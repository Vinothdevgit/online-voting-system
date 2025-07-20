package com.bishop.heber.voting.service.impl;

import com.bishop.heber.voting.dto.UserRegistrationRequest;
import com.bishop.heber.voting.model.Role;
import com.bishop.heber.voting.model.User;
import com.bishop.heber.voting.repository.RoleRepository;
import com.bishop.heber.voting.repository.UserRepository;
import com.bishop.heber.voting.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserRegistrationRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(request.username());
        user.setFullName(request.fullName());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEnabled(true);

        userRepository.save(user);

        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setUser(user);
        role.setRoleName(request.role());

        roleRepository.save(role);

        log.info("User {} registered with role {}", request.username(), request.role());
    }
}
