package io.github.cwireset.tcc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CpfJaCadastradoException extends RuntimeException{
    public CpfJaCadastradoException(String cpf) {
        super("Já existe um recurso do tipo Usuario com CPF com o valor '" + cpf + "'.");
    }
}
