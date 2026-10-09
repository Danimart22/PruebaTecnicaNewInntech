package com.votaciones.dto;

import com.votaciones.entity.Vote;

public record VoteResponse(Long id, Long voterId, Long candidateId) {

    // Convierte una entidad Vote en su respuesta para el cliente
    public static VoteResponse from(Vote vote) {
        return new VoteResponse(vote.getId(), vote.getVoter().getId(), vote.getCandidate().getId());
    }
}