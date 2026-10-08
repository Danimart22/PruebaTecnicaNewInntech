package com.votaciones.dto;

import java.util.List;

public record VoteStatistics(long totalVotes, long votersWhoVoted, List<CandidateStatistics> candidates) {
}