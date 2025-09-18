package com.bishop.heber.voting.controller;

import com.bishop.heber.voting.dto.CandidateRequest;
import com.bishop.heber.voting.model.Candidate;
import com.bishop.heber.voting.service.impl.CandidateService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class CandidateController {

    private final CandidateService candidateService;
    // inject ObjectMapper
    private final ObjectMapper objectMapper;

    @Autowired
    public CandidateController(CandidateService candidateService, ObjectMapper objectMapper) {
        this.candidateService = candidateService;
        this.objectMapper = objectMapper;
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

    // --- ADD (multipart) ---
    @PostMapping(value = "/candidate/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Candidate> addCandidateMultipart(
            @RequestPart("name") String name,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart(value = "promises", required = false) String promisesJson,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {

        List<String> promises = promisesJson == null || promisesJson.isBlank()
                ? List.of()
                : objectMapper.readValue(promisesJson, new TypeReference<List<String>>() {});
        Candidate created = candidateService.addCandidateMultipart(name, description, promises, image);
        return ResponseEntity.ok(created);
    }

    // --- UPDATE (multipart) ---
    @PutMapping(value = "/candidate/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Candidate> updateCandidateMultipart(
            @PathVariable UUID id,
            @RequestPart("name") String name,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart(value = "promises", required = false) String promisesJson,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {

        List<String> promises = promisesJson == null || promisesJson.isBlank()
                ? List.of()
                : objectMapper.readValue(promisesJson, new TypeReference<List<String>>() {});
        Candidate updated = candidateService.updateCandidateMultipart(id, name, description, promises, image);
        return ResponseEntity.ok(updated);
    }

    // --- PUBLIC: stream image ---
    @GetMapping(value = "/public/candidates/{id}/image")
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
                .header("Content-Disposition", "inline; filename=\"" + (c.getPhotoFilename() == null ? (id + ".img") : c.getPhotoFilename()) + "\"")
                .body(c.getPhoto());
    }
}
