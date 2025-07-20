package com.bishop.heber.voting.controller;

import com.bishop.heber.voting.dto.VoteResultDto;
import com.bishop.heber.voting.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/votes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VoteResultDto>> getVotes() {
        return ResponseEntity.ok(adminService.getVoteSummary());
    }
}
