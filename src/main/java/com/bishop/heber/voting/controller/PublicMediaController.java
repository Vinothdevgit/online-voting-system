package com.bishop.heber.voting.controller;

import com.bishop.heber.voting.model.Candidate;
import com.bishop.heber.voting.service.impl.CandidateService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/public") // <— NOT under /admin
public class PublicMediaController {

    private final CandidateService candidateService;

    public PublicMediaController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping("/candidates/{id}/image")
    public ResponseEntity<byte[]> getCandidateImage(@PathVariable UUID id) {
        Candidate c = candidateService.getByIdOrThrow(id);
        if (c.getPhoto() == null || c.getPhoto().length == 0) {
            return ResponseEntity.notFound().build();
        }
        MediaType mt = (c.getPhotoContentType() != null)
                ? MediaType.parseMediaType(c.getPhotoContentType())
                : MediaType.IMAGE_PNG;
        return ResponseEntity.ok()
                .contentType(mt)
                .header("Content-Disposition", "inline; filename=\"" +
                        (c.getPhotoFilename() == null ? (id + ".img") : c.getPhotoFilename()) + "\"")
                .body(c.getPhoto());
    }
}
