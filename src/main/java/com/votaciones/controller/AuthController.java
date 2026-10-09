package com.votaciones.controller;

import com.votaciones.dto.AuthRequest;
import com.votaciones.dto.TokenResponse;
import com.votaciones.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Registra un usuario nuevo y devuelve su token JWT
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse register(@Valid @RequestBody AuthRequest request) {
        return authService.register(request);
    }

    // Valida las credenciales y devuelve un token JWT
    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody AuthRequest request) {
        return authService.login(request);
    }
}