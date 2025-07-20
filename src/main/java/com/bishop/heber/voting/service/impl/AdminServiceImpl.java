package com.bishop.heber.voting.service.impl;

import com.bishop.heber.voting.dto.VoteResultDto;
import com.bishop.heber.voting.model.Vote;
import com.bishop.heber.voting.repository.VoteRepository;
import com.bishop.heber.voting.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final VoteRepository voteRepository;

    public AdminServiceImpl(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Override
    public List<VoteResultDto> getVoteSummary() {
        List<Vote> votes = voteRepository.findAll();
        return votes.stream()
            .collect(Collectors.groupingBy(Vote::getCandidateId, Collectors.counting()))
            .entrySet().stream()
            .map(entry -> new VoteResultDto(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }
}
