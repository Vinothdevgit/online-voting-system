package com.bishop.heber.voting.repository;

import com.bishop.heber.voting.dto.VoteResultDto;
import com.bishop.heber.voting.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, UUID> {
    boolean existsByVoterHash(String hash);

    @Query("SELECT new com.bishop.heber.voting.dto.VoteResultDto(v.candidate.name, COUNT(v)) FROM Vote v GROUP BY v.candidate.name")
    List<VoteResultDto> getVoteSummary();

    @Query("SELECT v.candidate.id, COUNT(v) FROM Vote v GROUP BY v.candidate.id")
    List<Object[]> countVotesPerCandidate();

}
