package io.github.cwireset.tcc.service.usuario;

import io.github.cwireset.tcc.builds.DomainBuilder;
import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.CpfJaCadastradoException;
import io.github.cwireset.tcc.exception.CpfNaoEncontradoException;
import io.github.cwireset.tcc.exception.EmailJaCadastradoException;
import io.github.cwireset.tcc.exception.IdInvalidoException;
import io.github.cwireset.tcc.repository.UsuarioRepository;
import io.github.cwireset.tcc.request.AtualizarUsuarioRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.time.LocalDate;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("UsuarioServiceTest")
public class UsuarioServiceTest {

    @MockBean
    private UsuarioRepository usuarioRepository;


    @Autowired
    private UsuarioService usuarioService;

    @Test
    @DisplayName("deve cadastrar um usuario")
    public void deveCadastrarUsuario() {
        Mockito.when(usuarioRepository.save(ArgumentMatchers.any(Usuario.class))).thenReturn(DomainBuilder.buildUsuarioTeste());

        Usuario usuarioRetornado = usuarioService.cadastrarUsuario(DomainBuilder.buildUsuarioTeste());

        Mockito.verify(usuarioRepository, Mockito.times(1)).save(ArgumentMatchers.any(Usuario.class));
        Assertions.assertEquals(DomainBuilder.buildUsuarioTeste().getCpf(), usuarioRetornado.getCpf());
    }

    @Test
    @DisplayName("deve retornar CpfJaCadastradoException")
    public void naoDeveCadastrarUsuarioPorCpfRepetido() {

        Mockito.when(usuarioRepository.existsByCpfIgnoreCase(DomainBuilder.buildUsuarioTeste().getCpf())).thenReturn(true);

        Assertions.assertThrows(CpfJaCadastradoException.class, () -> usuarioService.cadastrarUsuario(DomainBuilder.buildUsuarioTeste()));

    }

    @Test
    @DisplayName("deve retornar EmailJaCadastradoException")
    public void naoDeveCadastrarUsuarioPorEmailRepetido() {

        Mockito.when(usuarioRepository.existsByEmailIgnoreCase(DomainBuilder.buildUsuarioTeste().getEmail())).thenReturn(true);

        Assertions.assertThrows(EmailJaCadastradoException.class, () -> usuarioService.cadastrarUsuario(DomainBuilder.buildUsuarioTeste()));
    }

    @Test
    @DisplayName("deve listar usuarios")
    public void deveListarUsuarios() {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "nome").ignoreCase();
        Pageable pageableWithSort = PageRequest.of(0, 5, Sort.by(order));

        usuarioService.listarUsuarios(pageableWithSort);

        Mockito.verify(usuarioRepository).findAll(pageableWithSort);
    }

    @Test
    @DisplayName("deve listar usuario teste por id")
    public void deveListarUsuarioPorId() {
        Mockito.when(usuarioRepository.findById(DomainBuilder.buildUsuarioTeste().getId())).thenReturn(java.util.Optional.ofNullable(DomainBuilder.buildUsuarioTeste()));

        usuarioService.listarUsuarioPorId(DomainBuilder.buildUsuarioTeste().getId());

        Mockito.verify(usuarioRepository).findById(DomainBuilder.buildUsuarioTeste().getId());
    }

    @Test
    @DisplayName("deve retornar IdInvalidoException")
    public void naoDeveListarUsuarioPorId() {

        Mockito.when(usuarioRepository.findById(DomainBuilder.buildUsuarioTeste().getId())).thenThrow(IdInvalidoException.class);

        Assertions.assertThrows(IdInvalidoException.class, () -> usuarioService.listarUsuarioPorId(DomainBuilder.buildUsuarioTeste().getId()));
    }

    @Test
    @DisplayName("deve retornar usuario por cpf")
    public void deveRetornarUsuarioPorCpf() {
        Mockito.when(usuarioRepository.existsByCpfIgnoreCase(DomainBuilder.buildUsuarioTeste().getCpf())).thenReturn(true);
        Mockito.when(usuarioRepository.findByCpf(DomainBuilder.buildUsuarioTeste().getCpf())).thenReturn(DomainBuilder.buildUsuarioTeste());

        Usuario usuarioRetornado = usuarioService.listarUsuariosPorCpf(DomainBuilder.buildUsuarioTeste().getCpf());

        Mockito.verify(usuarioRepository, Mockito.times(1)).existsByCpfIgnoreCase(DomainBuilder.buildUsuarioTeste().getCpf());
        Mockito.verify(usuarioRepository, Mockito.times(1)).findByCpf(DomainBuilder.buildUsuarioTeste().getCpf());
        Assertions.assertEquals(usuarioRetornado.getCpf(), DomainBuilder.buildUsuarioTeste().getCpf());
    }

    @Test
    @DisplayName("deve retornar CpfNaoEncontradoException")
    public void naoDeveRetornarUsuarioPorCpf() {
        Mockito.when(usuarioRepository.existsByCpfIgnoreCase(DomainBuilder.buildUsuarioTeste().getCpf())).thenReturn(false);

        Assertions.assertThrows(CpfNaoEncontradoException.class, () -> usuarioService.listarUsuariosPorCpf(DomainBuilder.buildUsuarioTeste().getCpf()));
        Mockito.verify(usuarioRepository, Mockito.times(1)).existsByCpfIgnoreCase(DomainBuilder.buildUsuarioTeste().getCpf());
    }

    @Test
    @DisplayName("deve atualizar usuario teste")
    public void deveAtualizarUsuarioTeste() {
        AtualizarUsuarioRequest usuarioAtualizadoRequest = AtualizarUsuarioRequest.builder()
                .nome("Usuario Teste Atualizado")
                .email("usuarioteste@teste.com")
                .senha("5sd5s4d8f85")
                .dataNascimento(LocalDate.of(1980,12,01))
                .endereco(null)
                .build();

        Mockito.when(usuarioRepository.findById(DomainBuilder.buildUsuarioTeste().getId())).thenReturn(java.util.Optional.ofNullable(DomainBuilder.buildUsuarioTeste()));

        Usuario usuarioAtualizadoRetornado =  usuarioService.atualizarUsuario(DomainBuilder.buildUsuarioTeste().getId(), usuarioAtualizadoRequest);

        Mockito.verify(usuarioRepository).save(ArgumentMatchers.any(Usuario.class));

    }

    @Test
    @DisplayName("deve retornar IdInvalidoException")
    public void naoDeveAtualizarUsuario() {
        AtualizarUsuarioRequest usuarioAtualizadoRequest = AtualizarUsuarioRequest.builder()
                .nome("Usuario Teste Atualizado")
                .email("usuarioteste@teste.com")
                .senha("5sd5s4d8f85")
                .dataNascimento(LocalDate.of(1980,12,01))
                .endereco(null)
                .build();

        Mockito.when(usuarioRepository.findById(DomainBuilder.buildUsuarioTeste().getId())).thenThrow(IdInvalidoException.class);

        Assertions.assertThrows(IdInvalidoException.class, () -> usuarioService.atualizarUsuario(DomainBuilder.buildUsuarioTeste().getId(), usuarioAtualizadoRequest));

    }

    @Test
    @DisplayName("deve retornar true")
    public void deveRetornarTrueParaExisteUsuario() {
        Mockito.when(usuarioRepository.existsById(DomainBuilder.buildUsuarioTeste().getId())).thenReturn(true);

        Assertions.assertTrue(usuarioService.usuarioExiste(DomainBuilder.buildUsuarioTeste().getId()));

    }

    @Test
    @DisplayName("deve retornar false")
    public void deveRetornarFalseParaExisteUsuario() {
        Assertions.assertFalse(usuarioService.usuarioExiste(DomainBuilder.buildUsuarioTeste().getId()));
    }

}
