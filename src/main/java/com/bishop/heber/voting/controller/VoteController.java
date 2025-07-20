package com.bishop.heber.voting.controller;

import com.bishop.heber.voting.dto.VoteRequest;
import com.bishop.heber.voting.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vote")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public ResponseEntity<String> vote(@AuthenticationPrincipal UserDetails user,
                                       @RequestBody VoteRequest request) {
        return ResponseEntity.ok(voteService.vote(user.getUsername(), request));
    }
}
