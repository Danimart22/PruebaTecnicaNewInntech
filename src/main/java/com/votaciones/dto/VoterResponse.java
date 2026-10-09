package com.votaciones.dto;

import com.votaciones.entity.Voter;

public record VoterResponse(Long id, String name, String email, boolean hasVoted) {

    // Convierte una entidad Voter en su respuesta para el cliente
    public static VoterResponse from(Voter voter) {
        return new VoterResponse(voter.getId(), voter.getName(), voter.getEmail(), voter.isHasVoted());
    }
}