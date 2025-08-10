package com.bishop.heber.voting.repository;

import com.bishop.heber.voting.model.CandidatePromise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CandidatePromiseRepository extends JpaRepository<CandidatePromise, UUID> {
}