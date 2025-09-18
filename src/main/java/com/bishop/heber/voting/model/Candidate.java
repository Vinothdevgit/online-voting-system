package com.bishop.heber.voting.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "candidates")
public class
Candidate {
    @Id
    private UUID id;
    private String name;
    private String description;
    private String symbol; // 👈 new field (emoji or image URL)
    // One candidate can have many promises
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<CandidatePromise> promises = new ArrayList<>();
    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "photo")              // Postgres maps byte[] + @Lob to bytea
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @Column(name = "photo_filename")
    private String photoFilename;
}
