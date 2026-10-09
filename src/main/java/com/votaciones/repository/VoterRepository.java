package com.votaciones.repository;

import com.votaciones.entity.Voter;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VoterRepository extends JpaRepository<Voter, Long>, JpaSpecificationExecutor<Voter> {

    // Indica si ya existe un votante con ese correo, sin importar mayúsculas
    boolean existsByEmailIgnoreCase(String email);

    // Indica si ya existe un votante con ese nombre, sin importar mayúsculas
    boolean existsByNameIgnoreCase(String name);

    // Cuenta los votantes que ya votaron o los que no, según el valor recibido
    long countByHasVoted(boolean hasVoted);

    // Busca un votante y lo bloquea hasta terminar la transacción para evitar votos duplicados
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select v from Voter v where v.id = :id")
    Optional<Voter> findByIdForUpdate(@Param("id") Long id);
}