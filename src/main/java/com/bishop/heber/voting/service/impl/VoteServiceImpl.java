package com.bishop.heber.voting.service.impl;

import com.bishop.heber.voting.dto.VoteRequest;
import com.bishop.heber.voting.model.Candidate;
import com.bishop.heber.voting.model.Vote;
import com.bishop.heber.voting.repository.CandidateRepository;
import com.bishop.heber.voting.repository.VoteRepository;
import com.bishop.heber.voting.service.VoteService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final CandidateRepository candidateRepository;

    public VoteServiceImpl(VoteRepository voteRepository, CandidateRepository candidateRepository) {
        this.voteRepository = voteRepository;
        this.candidateRepository = candidateRepository;
    }

    @Override
    public String vote(String username, VoteRequest request) {
        String voterHash = String.valueOf(username.hashCode());
        if (voteRepository.existsByVoterHash(voterHash)) {
            return "You have already voted.";
        }

        Candidate candidate = candidateRepository.findById(UUID.fromString(request.candidateId()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid candidate ID"));
        Vote vote = new Vote();
        vote.setId(UUID.randomUUID());
        vote.setCandidate(candidate);
        vote.setVoterHash(voterHash);
        vote.setTimestamp(LocalDateTime.now());

        voteRepository.save(vote);

        return "Vote submitted successfully.";
    }
}
