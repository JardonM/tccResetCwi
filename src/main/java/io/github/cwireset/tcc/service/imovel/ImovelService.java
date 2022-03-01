package io.github.cwireset.tcc.service.imovel;

import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.exception.AnuncioExistenteParaOImovelException;
import io.github.cwireset.tcc.exception.IdInvalidoException;
import io.github.cwireset.tcc.repository.AnuncioRepository;
import io.github.cwireset.tcc.repository.ImovelRepository;
import io.github.cwireset.tcc.request.CadastrarImovelRequest;
import io.github.cwireset.tcc.service.anuncio.AnuncioService;
import io.github.cwireset.tcc.service.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImovelService {

    private ImovelRepository imovelRepository;
    private UsuarioService usuarioService;
    private AnuncioRepository anuncioRepository;

    @Autowired
    public ImovelService(ImovelRepository imovelRepository, UsuarioService usuarioService, AnuncioRepository anuncioRepository) {
        this.imovelRepository = imovelRepository;
        this.usuarioService = usuarioService;
        this.anuncioRepository = anuncioRepository;
    }

    public Imovel cadastrarImovel(CadastrarImovelRequest cadastrarImovelRequest) {

        Imovel imovel = Imovel.builder()
                .proprietario(usuarioService.listarUsuarioPorId(cadastrarImovelRequest.getIdProprietario()))
                .tipoImovel(cadastrarImovelRequest.getTipoImovel())
                .endereco(cadastrarImovelRequest.getEndereco())
                .identificacao(cadastrarImovelRequest.getIdentificacao())
                .caracteristicas(cadastrarImovelRequest.getCaracteristicas())
                .estaAtivo(true)
                .build();

        return imovelRepository.save(imovel);
    }

    public Page<Imovel> listarImoveis(Pageable pageable) {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "identificacao").ignoreCase();
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(order));
        return imovelRepository.findImovelsByEstaAtivoTrue(pageableWithSort);
    }

    public Page<Imovel> listarImoveisPorIdProprietario(Long idProprietario, Pageable pageable) {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "identificacao").ignoreCase();
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(order));
        return imovelRepository.findAllByProprietarioIdAndEstaAtivoTrue(idProprietario, pageableWithSort);
    }

    public Imovel listarImoveisPorId(Long idImovel) {
        return imovelRepository.findByIdAndEstaAtivoTrue(idImovel).orElseThrow(() -> new IdInvalidoException("Imovel", idImovel));
    }

    public void deletarImovel(Long idImovel) {
        Imovel imovel = imovelRepository.findByIdAndEstaAtivoTrue(idImovel).orElseThrow(() -> new IdInvalidoException("Imovel", idImovel));
        if(anuncioRepository.existsAnuncioByEstaAtivoIsTrueAndImovelId(idImovel)) {
            throw new AnuncioExistenteParaOImovelException("Não é possível excluir um imóvel que possua um anúncio.");
        }
        imovel.setEstaAtivo(false);
        imovelRepository.save(imovel);
    }

    public boolean imovelExiste(Long idImovel) {
        return imovelRepository.existsById(idImovel);
    }
}
