package com.bishop.heber.voting.service;

import com.bishop.heber.voting.dto.CandidateResultDTO;
import com.bishop.heber.voting.dto.VoteRequest;

import java.util.List;

public interface VoteService {
    boolean vote(String username, VoteRequest request);
    List<CandidateResultDTO> getResults();
}
