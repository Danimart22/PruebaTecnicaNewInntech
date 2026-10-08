package com.votaciones.controller;

import com.votaciones.dto.VoteRequest;
import com.votaciones.dto.VoteResponse;
import com.votaciones.dto.VoteStatistics;
import com.votaciones.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VoteResponse cast(@Valid @RequestBody VoteRequest request) {
        return voteService.cast(request);
    }

    @GetMapping
    public List<VoteResponse> list() {
        return voteService.list();
    }

    @GetMapping("/statistics")
    public VoteStatistics statistics() {
        return voteService.statistics();
    }
}