package com.bishop.heber.voting.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "votes")
public class Vote {
    @Id
    private UUID id;
    private String voterHash;
    private LocalDateTime timestamp;
    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getVoterHash() { return voterHash; }
    public void setVoterHash(String voterHash) { this.voterHash = voterHash; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }
}
