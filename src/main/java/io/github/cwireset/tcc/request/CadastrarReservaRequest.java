package io.github.cwireset.tcc.request;

import io.github.cwireset.tcc.domain.Periodo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class CadastrarReservaRequest {

    @NotNull
    private Long idSolicitante;

    @NotNull
    private Long idAnuncio;

    @NotNull
    @Future
    private Periodo periodo;

    @NotNull
    private Integer quantidadePessoas;
}
