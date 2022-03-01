package io.github.cwireset.tcc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.function.Supplier;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IdInvalidoException extends RuntimeException {
    public IdInvalidoException(String tipo, Long id) {
        super("Nenhum(a) " + tipo + " com Id com o valor '" + id + "' foi encontrado.");
    }
}
