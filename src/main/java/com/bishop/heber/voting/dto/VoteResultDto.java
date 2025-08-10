package com.bishop.heber.voting.dto;

public class VoteResultDto {
    private String candidateId;
    private String candidateName;
    private long totalVotes;

    public VoteResultDto(String candidateId, String candidateName, long totalVotes) {
        this.candidateId = candidateId;
        this.candidateName = candidateName;
        this.totalVotes = totalVotes;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public long getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(long totalVotes) {
        this.totalVotes = totalVotes;
    }

    // Getters & Setters
}
