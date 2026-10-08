package com.votaciones.service;

import com.votaciones.dto.CandidateStatistics;
import com.votaciones.dto.VoteRequest;
import com.votaciones.dto.VoteResponse;
import com.votaciones.dto.VoteStatistics;
import com.votaciones.entity.Candidate;
import com.votaciones.entity.Vote;
import com.votaciones.entity.Voter;
import com.votaciones.exception.ApiException;
import com.votaciones.repository.CandidateRepository;
import com.votaciones.repository.VoteRepository;
import com.votaciones.repository.VoterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final VoterRepository voterRepository;
    private final CandidateRepository candidateRepository;

    @Transactional
    public VoteResponse cast(VoteRequest request) {
        Voter voter = voterRepository.findByIdForUpdate(request.voterId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Votante no encontrado"));
        if (voter.isHasVoted()) {
            throw new ApiException(HttpStatus.CONFLICT, "El votante ya emitió su voto");
        }
        Candidate candidate = candidateRepository.findById(request.candidateId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Candidato no encontrado"));
        candidateRepository.incrementVotes(candidate.getId());
        voter.setHasVoted(true);
        Vote vote = voteRepository.save(Vote.builder().voter(voter).candidate(candidate).build());
        return VoteResponse.from(vote);
    }

    @Transactional(readOnly = true)
    public List<VoteResponse> list() {
        return voteRepository.findAll().stream().map(VoteResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public VoteStatistics statistics() {
        long total = voteRepository.count();
        List<CandidateStatistics> candidates = candidateRepository.findAll(Sort.by(Sort.Direction.DESC, "votes")).stream().map(c -> new CandidateStatistics(
                        c.getId(),
                        c.getName(),
                        c.getParty(),
                        c.getVotes(),
                        total == 0 ? 0 : round(c.getVotes() * 100.0 / total))).toList();
        return new VoteStatistics(total, voterRepository.countByHasVoted(true), candidates);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}