package com.votaciones.controller;

import com.votaciones.dto.CandidateRequest;
import com.votaciones.dto.CandidateResponse;
import com.votaciones.service.CandidateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CandidateResponse create(@Valid @RequestBody CandidateRequest request) {
        return candidateService.create(request);
    }

    @GetMapping
    public Page<CandidateResponse> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String party,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return candidateService.list(name, party, pageable);
    }

    @GetMapping("/{id}")
    public CandidateResponse get(@PathVariable Long id) {
        return candidateService.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        candidateService.delete(id);
    }
}