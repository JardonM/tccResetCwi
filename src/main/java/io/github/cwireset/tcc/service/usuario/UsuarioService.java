package io.github.cwireset.tcc.service.usuario;

import io.github.cwireset.tcc.domain.Endereco;
import io.github.cwireset.tcc.request.AtualizarEnderecoRequest;
import io.github.cwireset.tcc.request.AtualizarUsuarioRequest;
import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.CpfJaCadastradoException;
import io.github.cwireset.tcc.exception.CpfNaoEncontradoException;
import io.github.cwireset.tcc.exception.EmailJaCadastradoException;
import io.github.cwireset.tcc.exception.IdInvalidoException;
import io.github.cwireset.tcc.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;
    private ImagemAvatarUsuarioService imagemAvatarUsuarioService;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, ImagemAvatarUsuarioService imagemAvatarUsuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.imagemAvatarUsuarioService = imagemAvatarUsuarioService;
    }

    public Usuario cadastrarUsuario(Usuario usuario) {
        verificarEmail(usuario.getEmail());
        verificarCpf(usuario.getCpf());
        usuario.setImagemAvatar(imagemAvatarUsuarioService.getImagemAvatar());
        return usuarioRepository.save(usuario);
    }

    public Page<Usuario> listarUsuarios(Pageable pageable) {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "nome").ignoreCase();
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(order));
        return usuarioRepository.findAll(pageableWithSort);
    }

    public Usuario listarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new IdInvalidoException("Usuario",id));
    }

    public Usuario listarUsuariosPorCpf(String cpf) {
        if(!usuarioRepository.existsByCpfIgnoreCase(cpf)){
            throw new CpfNaoEncontradoException(cpf);
        }
        return usuarioRepository.findByCpf(cpf);
    }

    private void verificarEmail(String email) {
        if(usuarioRepository.existsByEmailIgnoreCase(email)){
            throw new EmailJaCadastradoException(email);
        }
    }

    private void verificarCpf(String cpf) {
        if(usuarioRepository.existsByCpfIgnoreCase(cpf)){
            throw new CpfJaCadastradoException(cpf);
        }
    }

    public Usuario atualizarUsuario(Long id, AtualizarUsuarioRequest atualizarUsuarioRequest) {
        Usuario usuarioEncontrado = usuarioRepository.findById(id).orElseThrow(() -> new IdInvalidoException("Usuario",id));
        if(!usuarioEncontrado.getEmail().equalsIgnoreCase(atualizarUsuarioRequest.getEmail())){
            verificarEmail(atualizarUsuarioRequest.getEmail());
        }
        usuarioEncontrado.setNome(atualizarUsuarioRequest.getNome());
        usuarioEncontrado.setEmail(atualizarUsuarioRequest.getEmail());
        usuarioEncontrado.setSenha(atualizarUsuarioRequest.getSenha());
        usuarioEncontrado.setDataNascimento(atualizarUsuarioRequest.getDataNascimento());

        if(atualizarUsuarioRequest.getEndereco() != null) {
            usuarioEncontrado.setEndereco(atualizarEnderecoUsuario(usuarioEncontrado, atualizarUsuarioRequest.getEndereco()));
        }

        return usuarioRepository.save(usuarioEncontrado);
    }

    private Endereco atualizarEnderecoUsuario(Usuario usuario, AtualizarEnderecoRequest atualizarEnderecoRequest) {
        usuario.getEndereco().setBairro(atualizarEnderecoRequest.getBairro());
        usuario.getEndereco().setCep(atualizarEnderecoRequest.getCep());
        usuario.getEndereco().setCidade(atualizarEnderecoRequest.getCidade());
        usuario.getEndereco().setComplemento(atualizarEnderecoRequest.getComplemento());
        usuario.getEndereco().setEstado(atualizarEnderecoRequest.getEstado());
        usuario.getEndereco().setLogradouro(atualizarEnderecoRequest.getLogradouro());
        usuario.getEndereco().setNumero(atualizarEnderecoRequest.getNumero());
        return usuario.getEndereco();
    }

    public boolean usuarioExiste(Long idUsuario) {
        return usuarioRepository.existsById(idUsuario);
    }
}
