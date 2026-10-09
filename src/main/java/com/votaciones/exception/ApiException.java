package com.votaciones.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    // Crea una excepción con el código HTTP y el mensaje de error que se enviará al cliente
    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}