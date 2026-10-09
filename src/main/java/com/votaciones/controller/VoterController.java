package com.votaciones.controller;

import com.votaciones.dto.VoterRequest;
import com.votaciones.dto.VoterResponse;
import com.votaciones.service.VoterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voters")
@RequiredArgsConstructor
public class VoterController {

    private final VoterService voterService;

    // Registra un nuevo votante
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VoterResponse create(@Valid @RequestBody VoterRequest request) {
        return voterService.create(request);
    }

    // Lista los votantes con filtros por nombre, correo y si ya votó, y paginación
    @GetMapping
    public Page<VoterResponse> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(name = "has_voted", required = false) Boolean hasVoted,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return voterService.list(name, email, hasVoted, pageable);
    }

    // Devuelve un votante por su ID
    @GetMapping("/{id}")
    public VoterResponse get(@PathVariable Long id) {
        return voterService.get(id);
    }

    // Elimina un votante por su ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        voterService.delete(id);
    }
}