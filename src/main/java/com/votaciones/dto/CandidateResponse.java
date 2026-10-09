package com.votaciones.dto;

import com.votaciones.entity.Candidate;

public record CandidateResponse(Long id, String name, String party, long votes) {

    // Convierte una entidad Candidate en su respuesta para el cliente
    public static CandidateResponse from(Candidate candidate) {
        return new CandidateResponse(candidate.getId(), candidate.getName(), candidate.getParty(), candidate.getVotes());
    }
}