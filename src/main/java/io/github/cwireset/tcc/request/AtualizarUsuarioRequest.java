package io.github.cwireset.tcc.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.cwireset.tcc.domain.Endereco;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Getter
@Setter
@Builder
public class AtualizarUsuarioRequest {
    @NotNull
    @NotBlank
    @NotEmpty
    private String nome;

    @NotBlank
    @NotEmpty
    @NotNull
    @Email
    private String email;

    @NotEmpty
    @NotNull
    @NotBlank
    private String senha;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    private AtualizarEnderecoRequest endereco;
}
