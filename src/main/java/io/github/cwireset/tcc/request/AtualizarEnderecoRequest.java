package io.github.cwireset.tcc.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class AtualizarEnderecoRequest {

    @NotEmpty
    @NotBlank
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "O CEP deve ser informado no formato 99999-999.")
    private String cep;

    @NotEmpty
    @NotBlank
    private String logradouro;

    @NotEmpty
    @NotBlank
    private String numero;

    private String complemento;

    @NotEmpty
    @NotBlank
    private String bairro;

    @NotEmpty
    @NotBlank
    private String cidade;

    @NotEmpty
    @NotBlank
    private String estado;
}
