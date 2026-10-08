package com.votaciones.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VoterRequest(
        @NotBlank String name,
        @NotBlank @Email String email
) {
}