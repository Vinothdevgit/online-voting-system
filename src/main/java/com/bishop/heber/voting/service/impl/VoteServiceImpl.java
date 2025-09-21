package com.bishop.heber.voting.service.impl;

import com.bishop.heber.voting.dto.CandidateResultDTO;
import com.bishop.heber.voting.dto.VoteRequest;
import com.bishop.heber.voting.model.Candidate;
import com.bishop.heber.voting.model.CandidatePromise;
import com.bishop.heber.voting.model.Vote;
import com.bishop.heber.voting.repository.CandidateRepository;
import com.bishop.heber.voting.repository.VoteRepository;
import com.bishop.heber.voting.service.VoteService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final CandidateRepository candidateRepository;

    public VoteServiceImpl(VoteRepository voteRepository, CandidateRepository candidateRepository) {
        this.voteRepository = voteRepository;
        this.candidateRepository = candidateRepository;
    }

    @Override
    public boolean vote(String username, VoteRequest request) {
        String voterHash = String.valueOf(username.hashCode());
        if (voteRepository.existsByVoterHash(voterHash)) {
            return false;
        }

        Candidate candidate = candidateRepository.findById(UUID.fromString(request.candidateId()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid candidate ID"));
        Vote vote = new Vote();
        vote.setId(UUID.randomUUID());
        vote.setCandidate(candidate);
        vote.setVoterHash(voterHash);
        vote.setTimestamp(LocalDateTime.now());

        voteRepository.save(vote);

        return true;
    }

    public List<CandidateResultDTO> getResults() {
        List<Object[]> rawResults = voteRepository.countVotesPerCandidate();
        Map<UUID, Long> counts = rawResults.stream()
                .collect(Collectors.toMap(
                        r -> (UUID) r[0],
                        r -> (Long) r[1]
                ));

        return candidateRepository.findAll().stream()
                .map(c -> new CandidateResultDTO(
                        c.getId(),
                        c.getName(),
                        c.getSymbol(),
                        counts.getOrDefault(c.getId(), 0L),
                        getCandidatPromises(c.getPromises())
                        ))
                .sorted((a, b) -> Long.compare(b.getVotes(), a.getVotes())) // sort by votes desc
                .toList();
    }

    private List<String> getCandidatPromises(List<CandidatePromise> candidatePromises){
        return candidatePromises.stream()
                .map(CandidatePromise::getPromiseText) // or .map(Object::toString)
                .collect(Collectors.toList());

    }
}
