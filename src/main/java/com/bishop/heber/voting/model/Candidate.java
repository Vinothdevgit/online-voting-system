package com.bishop.heber.voting.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "candidates")
public class Candidate {
    @Id
    private UUID id;
    private String name;
    private String description;
    private String symbol; // 👈 new field (emoji or image URL)
    // One candidate can have many promises
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<CandidatePromise> promises = new ArrayList<>();
}
