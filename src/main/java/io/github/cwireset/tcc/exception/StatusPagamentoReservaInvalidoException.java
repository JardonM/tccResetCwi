package io.github.cwireset.tcc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StatusPagamentoReservaInvalidoException extends RuntimeException{
    public StatusPagamentoReservaInvalidoException(String tipo, String status) {
        super("Não é possível realizar o "+ tipo +" para esta reserva, pois ela não está no status " + status + ".");
    }
}
