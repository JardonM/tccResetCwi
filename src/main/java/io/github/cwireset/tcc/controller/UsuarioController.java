package io.github.cwireset.tcc.controller;

import io.github.cwireset.tcc.request.AtualizarUsuarioRequest;
import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.service.usuario.ImagemAvatarUsuarioService;
import io.github.cwireset.tcc.service.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario cadastrarUsuario(@RequestBody @Valid Usuario usuario) {
        return usuarioService.cadastrarUsuario(usuario);
    }

    @GetMapping
    public Page<Usuario> listarUsuarios (@PageableDefault Pageable pageable) {
        return usuarioService.listarUsuarios(pageable);
    }

    @GetMapping(path = "/{idUsuario}")
    public Usuario listarUsuarioPorId(@PathVariable @Valid @NotNull Long idUsuario) {
        return usuarioService.listarUsuarioPorId(idUsuario);
    }

    @GetMapping(path = "/cpf/{cpf}")
    public Usuario listarUsuarioPorCpf(@PathVariable @Valid @NotNull @NotEmpty @NotBlank String cpf) {
        return usuarioService.listarUsuariosPorCpf(cpf);
    }

    @PutMapping(path = "/{id}")
    public Usuario atualizarUsuario(@PathVariable @Valid @NotNull Long id, @RequestBody @Valid AtualizarUsuarioRequest atualizarUsuarioRequest) {
        return usuarioService.atualizarUsuario(id, atualizarUsuarioRequest);
    }
}
