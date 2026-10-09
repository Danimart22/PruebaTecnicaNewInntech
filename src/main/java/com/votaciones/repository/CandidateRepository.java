package com.votaciones.repository;

import com.votaciones.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CandidateRepository extends JpaRepository<Candidate, Long>, JpaSpecificationExecutor<Candidate> {

    // Indica si ya existe un candidato con ese nombre, sin importar mayúsculas
    boolean existsByNameIgnoreCase(String name);

    // Suma un voto al candidato directamente en la base de datos
    @Modifying
    @Query("update Candidate c set c.votes = c.votes + 1 where c.id = :id")
    void incrementVotes(@Param("id") Long id);
}