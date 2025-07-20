package com.bishop.heber.voting.controller;

import com.bishop.heber.voting.dto.VoteRequest;
import com.bishop.heber.voting.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vote")
@Slf4j
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public ResponseEntity<String> vote(@AuthenticationPrincipal String user,
                                       @RequestBody VoteRequest request) {
        log.info("User Details from JWT {} ",user);
        return ResponseEntity.ok(voteService.vote(user, request));
    }
}
