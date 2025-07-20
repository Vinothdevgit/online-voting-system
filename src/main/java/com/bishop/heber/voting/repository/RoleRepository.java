package com.bishop.heber.voting.repository;

import com.bishop.heber.voting.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {}
