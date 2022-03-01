package io.github.cwireset.tcc.exception;

import io.github.cwireset.tcc.domain.FormaPagamento;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;
import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FormaPagamentoInvalidoException extends RuntimeException{
    public FormaPagamentoInvalidoException(FormaPagamento formaPagamento, List<FormaPagamento> formasAceitas) {
        super("O anúncio não aceita " + formaPagamento + " como forma de pagamento. As formas aceitas são " + Arrays.toString(formasAceitas.toArray()).replace("[", "").replace("]", "") + ".");
    }
}
