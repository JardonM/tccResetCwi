package io.github.cwireset.tcc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MinimoReservaException extends RuntimeException {
    public MinimoReservaException(Integer numeroMinimo, String tipoMinimo, String tipoImovel) {super("Não é possivel realizar uma reserva com menos de " + numeroMinimo + " " + tipoMinimo + " para imóveis do tipo " + tipoImovel);
    }
}
