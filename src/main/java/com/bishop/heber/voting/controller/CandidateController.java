package com.bishop.heber.voting.controller;

import com.bishop.heber.voting.dto.CandidateRequest;
import com.bishop.heber.voting.model.Candidate;
import com.bishop.heber.voting.service.impl.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
