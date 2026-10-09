package com.votaciones.repository;

import com.votaciones.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    // Indica si ya existe un usuario con ese nombre
    boolean existsByUsername(String username);

    // Busca un usuario por su nombre
    Optional<AppUser> findByUsername(String username);
}