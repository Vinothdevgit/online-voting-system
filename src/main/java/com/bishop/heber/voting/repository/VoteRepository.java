package com.bishop.heber.voting.repository;

import com.bishop.heber.voting.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, UUID> {
    boolean existsByVoterHash(String hash);
}
