package com.votaciones.service;

import com.votaciones.dto.VoterRequest;
import com.votaciones.dto.VoterResponse;
import com.votaciones.entity.Voter;
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
public class VoterService {

    private final VoterRepository voterRepository;
    private final CandidateRepository candidateRepository;

    // Registra un votante; falla si el correo ya existe o si hay un candidato con ese nombre
    @Transactional
    public VoterResponse create(VoterRequest request) {
        if (voterRepository.existsByEmailIgnoreCase(request.email())) {
            throw new ApiException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }
        if (candidateRepository.existsByNameIgnoreCase(request.name())) {
            throw new ApiException(HttpStatus.CONFLICT, "Un candidato no puede ser registrado como votante");
        }
        Voter voter = Voter.builder().name(request.name()).email(request.email()).build();
        return VoterResponse.from(voterRepository.save(voter));
    }

    // Devuelve una página de votantes filtrados por nombre, correo y si ya votaron
    @Transactional(readOnly = true)
    public Page<VoterResponse> list(String name, String email, Boolean hasVoted, Pageable pageable) {
        return voterRepository.findAll(filters(name, email, hasVoted), pageable).map(VoterResponse::from);
    }

    // Devuelve un votante por su ID
    @Transactional(readOnly = true)
    public VoterResponse get(Long id) {
        return VoterResponse.from(find(id));
    }

    // Elimina un votante; falla si ya votó
    @Transactional
    public void delete(Long id) {
        Voter voter = find(id);
        if (voter.isHasVoted()) {
            throw new ApiException(HttpStatus.CONFLICT, "No se puede eliminar un votante que ya votó");
        }
        voterRepository.delete(voter);
    }

    // Busca un votante por ID; falla si no existe
    private Voter find(Long id) {
        return voterRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Votante no encontrado"));
    }

    // Arma los filtros de búsqueda por nombre, correo y estado de voto, ignorando mayúsculas
    private Specification<Voter> filters(String name, String email, Boolean hasVoted) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (email != null && !email.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }
            if (hasVoted != null) {
                predicates.add(cb.equal(root.get("hasVoted"), hasVoted));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}