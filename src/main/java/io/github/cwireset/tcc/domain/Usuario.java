package io.github.cwireset.tcc.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "O campo nome não pode ser vazio.")
    @NotBlank(message = "O campo nome não pode ficar em branco")
    @NotNull(message = "Favor informar o campo nome")
    private String nome;

    @NotEmpty(message = "O campo email não pode ser vazio.")
    @NotBlank(message = "O campo email não pode ficar em branco")
    @NotNull(message = "Favor informar o campo email")
    @Email
    private String email;


    @NotEmpty(message = "O campo senha não pode ser vazio.")
    @NotBlank(message = "O campo senha não pode ficar em branco")
    @NotNull(message = "Favor informar o campo senha")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;


    @Pattern(regexp = "\\d{11}", message = "O CPF deve ser informado no formato 99999999999.")
    private String cpf;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past
    private LocalDate dataNascimento;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_endereco")
    private Endereco endereco;


    private String imagemAvatar;

}
