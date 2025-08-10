package com.bishop.heber.voting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;


@Entity
@Data
@Table(name = "candidate_promises")
public class CandidatePromise {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    @JsonBackReference
    private Candidate candidate;

    @Column(name = "promise_text", nullable = false)
    private String promiseText;
    public CandidatePromise() {}
    public CandidatePromise(UUID id , Candidate candidate,String promiseText) {
        this.id = id;
        this.candidate = candidate;
        this.promiseText = promiseText;
    }
}