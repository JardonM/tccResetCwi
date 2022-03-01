package io.github.cwireset.tcc.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DadosSolicitanteResponse {

    private Long id;

    private String nome;
}
