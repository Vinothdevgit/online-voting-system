package com.bishop.heber.voting.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "votes")
public class Vote {
    @Id
    private UUID id;
    private String candidateId;
    private String voterHash;
    private LocalDateTime timestamp;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getCandidateId() { return candidateId; }
    public void setCandidateId(String candidateId) { this.candidateId = candidateId; }

    public String getVoterHash() { return voterHash; }
    public void setVoterHash(String voterHash) { this.voterHash = voterHash; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
