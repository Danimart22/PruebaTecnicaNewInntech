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

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByNameIgnoreCase(String name);

    long countByHasVoted(boolean hasVoted);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select v from Voter v where v.id = :id")
    Optional<Voter> findByIdForUpdate(@Param("id") Long id);
}