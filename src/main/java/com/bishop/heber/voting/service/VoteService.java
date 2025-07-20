package com.bishop.heber.voting.service;

import com.bishop.heber.voting.dto.VoteRequest;

public interface VoteService {
    String vote(String username, VoteRequest request);
}
