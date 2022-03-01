package io.github.cwireset.tcc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.constraints.NotNull;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NaoExistenteException extends RuntimeException {
    public NaoExistenteException(String tipo, @NotNull Long idImovel) {
        super("Nenhum(a) " + tipo + " com Id com o valor '"+ idImovel + "' foi encontrado.");
    }
}
