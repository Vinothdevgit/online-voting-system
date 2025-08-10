package com.bishop.heber.voting.service.impl;

import com.bishop.heber.voting.dto.CandidateRequest;
import com.bishop.heber.voting.model.Candidate;
import com.bishop.heber.voting.model.CandidatePromise;
import com.bishop.heber.voting.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
