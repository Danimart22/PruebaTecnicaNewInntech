package com.votaciones.dto;

public record CandidateStatistics(Long candidateId, String name, String party, long votes, double percentage) {
}