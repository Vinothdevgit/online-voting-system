package com.bishop.heber.voting.dto;

import java.util.List;
import java.util.UUID;

public class CandidateResultDTO {
    private UUID id;
    private String name;
    private String symbol;
    private long votes;
    private List<String> promises;

    public CandidateResultDTO(UUID id, String name, String symbol, long votes, List<String> promises) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.votes = votes;
        this.promises = promises;
    }

    // getters & setters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public long getVotes() { return votes; }
    public List<String> getPromises() { return promises; }
}
