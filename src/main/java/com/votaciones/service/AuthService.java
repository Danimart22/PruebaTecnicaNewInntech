package com.votaciones.service;

import com.votaciones.dto.AuthRequest;
import com.votaciones.dto.TokenResponse;
import com.votaciones.entity.AppUser;
import com.votaciones.exception.ApiException;
import com.votaciones.repository.AppUserRepository;
import com.votaciones.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public TokenResponse register(AuthRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ApiException(HttpStatus.CONFLICT, "El usuario ya existe");
        }
        userRepository.save(AppUser.builder().username(request.username()).password(passwordEncoder.encode(request.password())).build());
        return new TokenResponse(jwtService.generate(request.username()));
    }

    public TokenResponse login(AuthRequest request) {
        AppUser user = userRepository.findByUsername(request.username()).filter(u -> passwordEncoder.matches(request.password(), u.getPassword())).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));
        return new TokenResponse(jwtService.generate(user.getUsername()));
    }
}