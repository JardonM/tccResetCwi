package io.github.cwireset.tcc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CpfNaoEncontradoException extends RuntimeException{
    public CpfNaoEncontradoException(String cpf) {
        super("Nenhum(a) Usuario com CPF com o valor '" + cpf + "' foi encontrado.");
    }
}
