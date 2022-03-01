package io.github.cwireset.tcc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.constraints.NotNull;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AnuncioExistenteParaOImovelException extends RuntimeException {
    public AnuncioExistenteParaOImovelException(String message) {
        super(message);
    }
}
