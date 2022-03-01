package io.github.cwireset.tcc.service.anuncio;

import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.exception.AnuncioExistenteParaOImovelException;
import io.github.cwireset.tcc.exception.IdInvalidoException;
import io.github.cwireset.tcc.exception.NaoExistenteException;
import io.github.cwireset.tcc.repository.AnuncioRepository;
import io.github.cwireset.tcc.request.CadastrarAnuncioRequest;
import io.github.cwireset.tcc.service.imovel.ImovelService;
import io.github.cwireset.tcc.service.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnuncioService {

    private AnuncioRepository anuncioRepository;
    private ImovelService imovelService;
    private UsuarioService usuarioService;

    @Autowired
    public AnuncioService(AnuncioRepository anuncioRepository, ImovelService imovelService, UsuarioService usuarioService) {
        this.anuncioRepository = anuncioRepository;
        this.imovelService = imovelService;
        this.usuarioService = usuarioService;
    }

    public Anuncio cadastrarAnuncio(CadastrarAnuncioRequest cadastrarAnuncioRequest) {
        verificarImovelExistente(cadastrarAnuncioRequest);
        verificarUsuarioExistente(cadastrarAnuncioRequest);
        if(verificarAnuncioExistenteParaOImovel(cadastrarAnuncioRequest.getIdImovel())) {
            throw new AnuncioExistenteParaOImovelException("Já existe um recurso do tipo Anuncio com IdImovel com o valor '"+ cadastrarAnuncioRequest.getIdImovel() +"'.");
        }

        Anuncio anuncio = Anuncio.builder()
                .anunciante(usuarioService.listarUsuarioPorId(cadastrarAnuncioRequest.getIdAnunciante()))
                .imovel(imovelService.listarImoveisPorId(cadastrarAnuncioRequest.getIdImovel()))
                .tipoAnuncio(cadastrarAnuncioRequest.getTipoAnuncio())
                .valorDiaria(cadastrarAnuncioRequest.getValorDiaria())
                .formasAceitas(cadastrarAnuncioRequest.getFormasAceitas())
                .descricao(cadastrarAnuncioRequest.getDescricao())
                .estaAtivo(true)
                .build();

        return anuncioRepository.save(anuncio);
    }

    public Page<Anuncio> listarAnuncios(Pageable pageable) {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "valorDiaria");
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(order));
        return anuncioRepository.findAnunciosByEstaAtivoTrue(pageableWithSort);
    }

    public Anuncio buscarAnuncioPorId(Long idAnuncio) {
        return anuncioRepository.findByIdAndEstaAtivoIsTrue(idAnuncio).orElseThrow(() -> new IdInvalidoException("Anuncio", idAnuncio));
    }


    public Page<Anuncio> listarAnunciosPorAnunciante(Long idAnunciante, Pageable pageable) {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "valorDiaria");
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(order));
        return anuncioRepository.findAllByAnuncianteIdAndEstaAtivoIsTrue(idAnunciante, pageableWithSort);
    }

    public void deletarAnuncio(Long idAnuncio) {
        Anuncio anuncio = anuncioRepository.findByIdAndEstaAtivoIsTrue(idAnuncio).orElseThrow(() -> new IdInvalidoException("Anuncio", idAnuncio));
        anuncio.setEstaAtivo(false);
        anuncioRepository.save(anuncio);
    }

    private void verificarUsuarioExistente(CadastrarAnuncioRequest cadastrarAnuncioRequest) {
        if(!usuarioService.usuarioExiste(cadastrarAnuncioRequest.getIdAnunciante())){
            throw new NaoExistenteException("Usuario", cadastrarAnuncioRequest.getIdAnunciante());
        }
    }

    private void verificarImovelExistente(CadastrarAnuncioRequest cadastrarAnuncioRequest){
        if(!imovelService.imovelExiste(cadastrarAnuncioRequest.getIdImovel())) {
            throw new NaoExistenteException("Imovel", cadastrarAnuncioRequest.getIdImovel());
        }
    }

    public boolean verificarAnuncioExistenteParaOImovel(Long idImovel) {
        return anuncioRepository.existsAnuncioByEstaAtivoIsTrueAndImovelId(idImovel);
    }


}
