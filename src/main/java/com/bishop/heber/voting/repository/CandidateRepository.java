package com.bishop.heber.voting.repository;

import com.bishop.heber.voting.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CandidateRepository extends JpaRepository<Candidate, UUID> {}
