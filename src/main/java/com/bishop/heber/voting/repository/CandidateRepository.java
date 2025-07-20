package com.bishop.heber.voting.repository;

import com.bishop.heber.voting.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, String> {}
