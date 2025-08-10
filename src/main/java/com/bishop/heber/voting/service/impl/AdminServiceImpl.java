package com.bishop.heber.voting.service.impl;

import com.bishop.heber.voting.dto.VoteResultDto;
import com.bishop.heber.voting.model.Candidate;
import com.bishop.heber.voting.model.Vote;
import com.bishop.heber.voting.repository.CandidateRepository;
import com.bishop.heber.voting.repository.VoteRepository;
import com.bishop.heber.voting.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final VoteRepository voteRepository;
    private final CandidateRepository candidateRepository;

    public AdminServiceImpl(VoteRepository voteRepository, CandidateRepository candidateRepository) {
        this.voteRepository = voteRepository;
        this.candidateRepository = candidateRepository;
    }

    @Override
    public List<VoteResultDto> getVoteSummary() {
        List<Candidate> allCandidates = candidateRepository.findAll();

        Map<UUID, Long> voteCounts = voteRepository.findAll().stream()
                .collect(Collectors.groupingBy(v -> v.getCandidate().getId(), Collectors.counting()));

        return allCandidates.stream()
                .map(candidate -> new VoteResultDto(
                        candidate.getId().toString(),
                        candidate.getName(),
                        voteCounts.getOrDefault(candidate.getId(), 0L)
                ))
                .collect(Collectors.toList());
    }


}
