package com.bishop.heber.voting.controller;

import com.bishop.heber.voting.dto.CandidateResultDTO;
import com.bishop.heber.voting.dto.VoteRequest;
import com.bishop.heber.voting.model.Candidate;
import com.bishop.heber.voting.repository.CandidateRepository;
import com.bishop.heber.voting.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class VoteController {

    private final VoteService voteService;
    private final CandidateRepository candidateRepository;
    public VoteController(VoteService voteService, CandidateRepository candidateRepository) {
        this.voteService = voteService;
        this.candidateRepository = candidateRepository;
    }

    @PostMapping("/vote")
    public ResponseEntity<String> vote(@AuthenticationPrincipal String user,
                                       @RequestBody VoteRequest request) {
        log.info("User Details from JWT {} ",user);
        if(voteService.vote(user, request))
            return ResponseEntity.ok("Vote submitted successfully.");
        else
            return ResponseEntity.badRequest().body("Duplicate vote detected!!!");
    }

    @GetMapping("/candidates")
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    @GetMapping("/vote/result")
    public ResponseEntity<List<CandidateResultDTO>> getResults() {
        List<CandidateResultDTO> results = voteService.getResults();
        return ResponseEntity.ok(results);
    }
}
