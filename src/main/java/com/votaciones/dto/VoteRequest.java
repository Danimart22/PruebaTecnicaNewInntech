package com.votaciones.dto;

import jakarta.validation.constraints.NotNull;

public record VoteRequest(@NotNull Long voterId, @NotNull Long candidateId) {
}