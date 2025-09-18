package com.bishop.heber.voting.controller;

import com.bishop.heber.voting.dto.CandidateRequest;
import com.bishop.heber.voting.model.Candidate;
import com.bishop.heber.voting.service.impl.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class CandidateController {

    private final CandidateService candidateService;

    @Autowired
    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    // Endpoint to add a new candidate
    @PostMapping("/candidate/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addCandidate(@RequestBody CandidateRequest request) {
        candidateService.addCandidate(request);
        return ResponseEntity.ok("Candidate added successfully....");
    }

    // Endpoint to list all candidates (with promises)
    @GetMapping("/candidates")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Candidate>> getCandidates() {
        return ResponseEntity.ok(candidateService.getAllCandidates());
    }

    @PutMapping("/candidate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Candidate> updateCandidate(
            @PathVariable UUID id,
            @RequestBody CandidateRequest request) {

        Candidate updated = candidateService.updateCandidate(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/candidate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCandidate(@PathVariable UUID id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.noContent().build();
    }
}
