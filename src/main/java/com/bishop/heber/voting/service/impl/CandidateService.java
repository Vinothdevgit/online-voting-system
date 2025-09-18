package com.bishop.heber.voting.service.impl;

import com.bishop.heber.voting.dto.CandidateRequest;
import com.bishop.heber.voting.model.Candidate;
import com.bishop.heber.voting.model.CandidatePromise;
import com.bishop.heber.voting.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;

    @Autowired
    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public void addCandidate(CandidateRequest request) {
        // Create candidate entity
        Candidate candidate = new Candidate();
        candidate.setId(UUID.randomUUID());
        candidate.setName(request.getName());
        candidate.setDescription(request.getDescription());
        candidate.setSymbol(request.getSymbol());

        // Convert promises from strings into CandidatePromise entities
        var promiseEntities = request.getPromises()
                .stream()
                .map(text -> new CandidatePromise(UUID.randomUUID(),candidate, text))
                .collect(Collectors.toList());
        candidate.setPromises(promiseEntities);
        candidateRepository.save(candidate);

    }

    public List<Candidate> getAllCandidates() {
        // Since Candidate.promises is eagerly fetched in the entity,
        // this will load promises as well.
        return candidateRepository.findAll();
    }

    public Candidate updateCandidate(UUID id, CandidateRequest request) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found: " + id));

        // Basic fields (adapt to your DTO field names)
        candidate.setName(request.getName());
        candidate.setDescription(request.getDescription());
        candidate.setSymbol(request.getSymbol());

        // --- Replace promises ---
        // Ensure your Candidate entity has orphanRemoval=true on promises collection.
        // Clear existing promises; JPA will delete orphans.
        if (candidate.getPromises() != null) {
            candidate.getPromises().clear();
        }

        // Rebuild from request (expecting List<String> promises in DTO)
        if (request.getPromises() != null) {
            List<CandidatePromise> newPromises = request.getPromises().stream()
                    .filter(p -> p != null && !p.isBlank())
                    .map(p -> {
                        CandidatePromise cp = new CandidatePromise();
                        cp.setId(UUID.randomUUID());
                        cp.setCandidate(candidate);
                        cp.setPromiseText(p.trim());
                        return cp;
                    })
                    .toList();

            candidate.getPromises().addAll(newPromises);
        }
        return candidateRepository.save(candidate);
    }

    public void deleteCandidate(UUID id) {
        if (!candidateRepository.existsById(id)) {
            // Optional: throw if not found
            // throw new IllegalArgumentException("Candidate not found: " + id);
            return;
        }
        candidateRepository.deleteById(id);
    }

    public Candidate addCandidateMultipart(String name, String description, List<String> promises, MultipartFile image) {
        Candidate c = new Candidate();
        c.setId(UUID.randomUUID());
        c.setName(name);
        c.setDescription(description);

        if (image != null && !image.isEmpty()) {
            try {
                c.setPhoto(image.getBytes());                  // <-- bytes, not size
                c.setPhotoContentType(image.getContentType());
                c.setPhotoFilename(image.getOriginalFilename());
            } catch (IOException e) {
                throw new RuntimeException("Reading image failed", e);
            }
        }

        // promises
        List<CandidatePromise> list = promises == null ? List.of() :
                promises.stream()
                        .filter(p -> p != null && !p.isBlank())
                        .map(p -> {
                            CandidatePromise cp = new CandidatePromise();
                            cp.setId(UUID.randomUUID());
                            cp.setCandidate(c);
                            cp.setPromiseText(p.trim());
                            return cp;
                        }).toList();
        c.setPromises(new ArrayList<>(list));

        return candidateRepository.save(c);
    }

    public Candidate updateCandidateMultipart(UUID id, String name, String description, List<String> promises, MultipartFile image) {
        Candidate c = candidateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found: " + id));
        c.setName(name);
        c.setDescription(description);

        if (image != null && !image.isEmpty()) {
            try {
                c.setPhoto(image.getBytes());
                c.setPhotoContentType(image.getContentType());
                c.setPhotoFilename(image.getOriginalFilename());
            } catch (IOException e) {
                throw new RuntimeException("Reading image failed", e);
            }
        }

        // replace promises (requires orphanRemoval=true on Candidate.promises)
        c.getPromises().clear();
        if (promises != null) {
            c.getPromises().addAll(
                    promises.stream()
                            .filter(p -> p != null && !p.isBlank())
                            .map(p -> {
                                CandidatePromise cp = new CandidatePromise();
                                cp.setId(UUID.randomUUID());
                                cp.setCandidate(c);
                                cp.setPromiseText(p.trim());
                                return cp;
                            }).toList()
            );
        }

        return candidateRepository.save(c);
    }

    // utility for image controller
    public Candidate getByIdOrThrow(UUID id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found: " + id));
    }

}
