package com.votaciones.service;

import com.votaciones.dto.CandidateRequest;
import com.votaciones.dto.CandidateResponse;
import com.votaciones.entity.Candidate;
import com.votaciones.exception.ApiException;
import com.votaciones.repository.CandidateRepository;
import com.votaciones.repository.VoterRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final VoterRepository voterRepository;

    // Registra un candidato; falla si ya existe un votante con ese nombre
    @Transactional
    public CandidateResponse create(CandidateRequest request) {
        if (voterRepository.existsByNameIgnoreCase(request.name())) {
            throw new ApiException(HttpStatus.CONFLICT, "Un votante no puede ser registrado como candidato");
        }
        Candidate candidate = Candidate.builder().name(request.name()).party(request.party()).build();
        return CandidateResponse.from(candidateRepository.save(candidate));
    }

    // Devuelve una página de candidatos filtrados por nombre y partido
    @Transactional(readOnly = true)
    public Page<CandidateResponse> list(String name, String party, Pageable pageable) {
        return candidateRepository.findAll(filters(name, party), pageable).map(CandidateResponse::from);
    }

    // Devuelve un candidato por su ID
    @Transactional(readOnly = true)
    public CandidateResponse get(Long id) {
        return CandidateResponse.from(find(id));
    }

    // Elimina un candidato; falla si ya tiene votos
    @Transactional
    public void delete(Long id) {
        Candidate candidate = find(id);
        if (candidate.getVotes() > 0) {
            throw new ApiException(HttpStatus.CONFLICT, "No se puede eliminar un candidato con votos");
        }
        candidateRepository.delete(candidate);
    }

    // Busca un candidato por ID; falla si no existe
    private Candidate find(Long id) {
        return candidateRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Candidato no encontrado"));
    }

    // Arma los filtros de búsqueda por nombre y partido, ignorando mayúsculas
    private Specification<Candidate> filters(String name, String party) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (party != null && !party.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("party")), "%" + party.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}