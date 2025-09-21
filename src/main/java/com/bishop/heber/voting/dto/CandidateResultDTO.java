package com.bishop.heber.voting.dto;

import java.util.UUID;

public class CandidateResultDTO {
    private UUID id;
    private String name;
    private String symbol;
    private long votes;

    public CandidateResultDTO(UUID id, String name, String symbol, long votes) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.votes = votes;
    }

    // getters & setters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public long getVotes() { return votes; }
}
