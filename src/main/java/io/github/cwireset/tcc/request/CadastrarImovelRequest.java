package io.github.cwireset.tcc.request;

import io.github.cwireset.tcc.domain.CaracteristicaImovel;
import io.github.cwireset.tcc.domain.Endereco;
import io.github.cwireset.tcc.domain.TipoImovel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
public class CadastrarImovelRequest {

    @NotNull
    private TipoImovel tipoImovel;

    @NotNull
    @Valid
    private Endereco endereco;

    @NotBlank
    @NotNull
    @NotEmpty
    private String identificacao;

    @NotNull
    private Long idProprietario;

    @NotNull
    @NotEmpty
    private List<CaracteristicaImovel> caracteristicas;
}
