package io.github.cwireset.tcc.service.anuncio;

import io.github.cwireset.tcc.builds.DomainBuilder;
import io.github.cwireset.tcc.domain.*;
import io.github.cwireset.tcc.exception.AnuncioExistenteParaOImovelException;
import io.github.cwireset.tcc.exception.IdInvalidoException;
import io.github.cwireset.tcc.exception.NaoExistenteException;
import io.github.cwireset.tcc.repository.AnuncioRepository;
import io.github.cwireset.tcc.service.imovel.ImovelService;
import io.github.cwireset.tcc.service.usuario.UsuarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AnuncioServiceTest")
public class AnuncioServiceTest {

    @MockBean
    private AnuncioRepository anuncioRepository;

    @MockBean
    private ImovelService imovelService;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private AnuncioService anuncioService;



    @Test
    @DisplayName("deve cadastrar anuncio")
    public void deveCadastrarAnuncio() {
        Mockito.when(anuncioRepository.existsAnuncioByEstaAtivoIsTrueAndImovelId(DomainBuilder.buildCadastrarAnuncioRequestTeste().getIdImovel())).thenReturn(false);
        Mockito.when(imovelService.imovelExiste(DomainBuilder.buildImovelTeste().getId())).thenReturn(true);
        Mockito.when(usuarioService.usuarioExiste(DomainBuilder.buildUsuarioTeste().getId())).thenReturn(true);
        Mockito.when(usuarioService.listarUsuarioPorId(DomainBuilder.buildUsuarioTeste().getId())).thenReturn(DomainBuilder.buildUsuarioTeste());
        Mockito.when(imovelService.listarImoveisPorId(DomainBuilder.buildImovelTeste().getId())).thenReturn(DomainBuilder.buildImovelTeste());

        anuncioService.cadastrarAnuncio(DomainBuilder.buildCadastrarAnuncioRequestTeste());

        Mockito.verify(anuncioRepository, Mockito.times(1)).existsAnuncioByEstaAtivoIsTrueAndImovelId(DomainBuilder.buildImovelTeste().getId());
        Mockito.verify(anuncioRepository, Mockito.times(1)).save(ArgumentMatchers.any(Anuncio.class));
    }

    @Test
    @DisplayName("nao deve cadastrar pois ja existe anuncio")
    public void naoDeveCadastrarAnuncioJaExistente() {
        Mockito.when(anuncioRepository.existsAnuncioByEstaAtivoIsTrueAndImovelId(DomainBuilder.buildImovelTeste().getId())).thenReturn(true);

        Assertions.assertThrows(AnuncioExistenteParaOImovelException.class, () -> anuncioService.cadastrarAnuncio(DomainBuilder.buildCadastrarAnuncioRequestTeste()));
    }

    @Test
    @DisplayName("nao deve cadastrar pois imovel nao existe")
    public void naoDeveCadastrarImovelNaoExiste() {
        Mockito.when(anuncioRepository.existsAnuncioByEstaAtivoIsTrueAndImovelId(DomainBuilder.buildImovelTeste().getId())).thenReturn(false);
        Mockito.when(imovelService.imovelExiste(DomainBuilder.buildImovelTeste().getId())).thenReturn(false);

        Assertions.assertThrows(NaoExistenteException.class, () -> anuncioService.cadastrarAnuncio(DomainBuilder.buildCadastrarAnuncioRequestTeste()));
    }

    @Test
    @DisplayName("nao deve cadastrar pois usuario nao existe")
    public void naoDeveCadastrarUsuarioNaoExiste() {
        Mockito.when(anuncioRepository.existsAnuncioByEstaAtivoIsTrueAndImovelId(DomainBuilder.buildImovelTeste().getId())).thenReturn(false);
        Mockito.when(imovelService.imovelExiste(DomainBuilder.buildImovelTeste().getId())).thenReturn(true);
        Mockito.when(usuarioService.usuarioExiste(DomainBuilder.buildUsuarioTeste().getId())).thenReturn(false);

        Assertions.assertThrows(NaoExistenteException.class, () -> anuncioService.cadastrarAnuncio(DomainBuilder.buildCadastrarAnuncioRequestTeste()));
    }

    @Test
    @DisplayName("deve listar anuncios")
    public void deveListarAnuncios() {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "valorDiaria");
        Pageable pageableWithSort = PageRequest.of(0, 5, Sort.by(order));

        anuncioService.listarAnuncios(pageableWithSort);

        Mockito.verify(anuncioRepository).findAnunciosByEstaAtivoTrue(pageableWithSort);

    }

    @Test
    @DisplayName("deve listar anuncios por id")
    public void deveListarAnunciosPorId() {
        Mockito.when(anuncioRepository.findByIdAndEstaAtivoIsTrue(DomainBuilder.buildAnuncioTeste().getId())).thenReturn(Optional.ofNullable(DomainBuilder.buildAnuncioTeste()));

        Anuncio anuncioRetornado =  anuncioService.buscarAnuncioPorId(DomainBuilder.buildAnuncioTeste().getId());

        Assertions.assertEquals(anuncioRetornado.getId(), DomainBuilder.buildAnuncioTeste().getId());
    }

    @Test
    @DisplayName("deve retornar IdInvalidoException ao buscar anuncio por id")
    public void deveRetornarIdInvalidoException() {
        Assertions.assertThrows(IdInvalidoException.class, () -> anuncioService.buscarAnuncioPorId(DomainBuilder.buildAnuncioTeste().getId()));
        Mockito.verify(anuncioRepository).findByIdAndEstaAtivoIsTrue(DomainBuilder.buildAnuncioTeste().getId());
    }

    @Test
    @DisplayName("deve listar anuncios por anunciante")
    public void deveListarAnunciosPorAnunciante() {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "valorDiaria");
        Pageable pageableWithSort = PageRequest.of(0, 5, Sort.by(order));
        Mockito.when(anuncioRepository.findAllByAnuncianteIdAndEstaAtivoIsTrue(DomainBuilder.buildAnuncioTeste().getAnunciante().getId(), pageableWithSort))
                .thenReturn(Page.empty());

        Page<Anuncio> anunciosPage = anuncioService.listarAnunciosPorAnunciante(DomainBuilder.buildAnuncioTeste().getAnunciante().getId(), pageableWithSort);

        Mockito.verify(anuncioRepository).findAllByAnuncianteIdAndEstaAtivoIsTrue(DomainBuilder.buildAnuncioTeste().getAnunciante().getId(), pageableWithSort);
        Assertions.assertEquals(0, anunciosPage.getSize());
    }

    @Test
    @DisplayName("deve excluir um anuncio")
    public void deveExcluirUmAnuncio() {
        Mockito.when(anuncioRepository.findByIdAndEstaAtivoIsTrue(DomainBuilder.buildAnuncioTeste().getId())).thenReturn(Optional.of(DomainBuilder.buildAnuncioTeste()));

        anuncioService.deletarAnuncio(DomainBuilder.buildAnuncioTeste().getId());

        Mockito.verify(anuncioRepository).save(ArgumentMatchers.any(Anuncio.class));
        Mockito.verify(anuncioRepository).findByIdAndEstaAtivoIsTrue(DomainBuilder.buildAnuncioTeste().getId());
    }

    @Test
    @DisplayName("deve retornar IdInvalidoException ao deletar anuncio")
    public void naoDeveDeletarAnuncio() {
        Assertions.assertThrows(IdInvalidoException.class, () -> anuncioService.deletarAnuncio(DomainBuilder.buildAnuncioTeste().getId()));
        Mockito.verify(anuncioRepository).findByIdAndEstaAtivoIsTrue(DomainBuilder.buildAnuncioTeste().getId());
    }

}
