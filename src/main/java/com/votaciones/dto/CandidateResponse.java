package com.votaciones.dto;

import com.votaciones.entity.Candidate;

public record CandidateResponse(Long id, String name, String party, long votes) {

    public static CandidateResponse from(Candidate candidate) {
        return new CandidateResponse(candidate.getId(), candidate.getName(), candidate.getParty(), candidate.getVotes());
    }
}