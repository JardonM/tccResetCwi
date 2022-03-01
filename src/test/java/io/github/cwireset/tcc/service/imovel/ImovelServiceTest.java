package io.github.cwireset.tcc.service.imovel;

import io.github.cwireset.tcc.builds.DomainBuilder;
import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.exception.IdInvalidoException;
import io.github.cwireset.tcc.repository.ImovelRepository;
import io.github.cwireset.tcc.service.usuario.UsuarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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
@ExtendWith(MockitoExtension.class)
@DisplayName("ImovelServiceTest")
public class ImovelServiceTest {

    @MockBean
    private ImovelRepository imovelRepository;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ImovelService imovelService;



    @Test
    @DisplayName("deve cadastrar imovel")
    public void deveCadastrarImovel() {
        Mockito.when(usuarioService.listarUsuarioPorId(DomainBuilder.buildUsuarioTeste().getId())).thenReturn(DomainBuilder.buildUsuarioTeste());

        imovelService.cadastrarImovel(DomainBuilder.buildImovelRequestTeste());

        Mockito.verify(imovelRepository, Mockito.times(1)).save(ArgumentMatchers.any(Imovel.class));
    }

    @Test
    @DisplayName("deve retornar IdInvalidoException")
    public void naoDeveCadastrarImovel() {
        Mockito.when(usuarioService.listarUsuarioPorId(DomainBuilder.buildUsuarioTeste().getId())).thenThrow(IdInvalidoException.class);

        Assertions.assertThrows(IdInvalidoException.class, () -> imovelService.cadastrarImovel(DomainBuilder.buildImovelRequestTeste()));

        Mockito.verify(imovelRepository, Mockito.times(0)).save(ArgumentMatchers.any(Imovel.class));
    }

    @Test
    @DisplayName("deve retornar imoveis")
    public void deveListarImoveis() {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "identificacao").ignoreCase();
        Pageable pageableWithSort = PageRequest.of(0, 5, Sort.by(order));

        imovelService.listarImoveis(Pageable.ofSize(5));

        Mockito.verify(imovelRepository, Mockito.times(1)).findImovelsByEstaAtivoTrue(pageableWithSort);

    }

    @Test
    @DisplayName("deve listar imoveis por id proprietario")
    public void deveListarImoveisPorProprietario() {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "identificacao").ignoreCase();
        Pageable pageableWithSort = PageRequest.of(0, 5, Sort.by(order));

        Mockito.when(imovelRepository.findAllByProprietarioIdAndEstaAtivoTrue(DomainBuilder.buildImovelTeste().getProprietario().getId(), pageableWithSort)).thenReturn(Page.empty());

        imovelService.listarImoveisPorIdProprietario(DomainBuilder.buildImovelTeste().getProprietario().getId(), pageableWithSort);

        Mockito.verify(imovelRepository).findAllByProprietarioIdAndEstaAtivoTrue(DomainBuilder.buildImovelTeste().getProprietario().getId(), pageableWithSort);
    }

    @Test
    @DisplayName("deve listar imovel por id")
    public void deveListarImovelPorId() {
        Mockito.when(imovelRepository.findByIdAndEstaAtivoTrue(DomainBuilder.buildImovelTeste().getId())).thenReturn(Optional.ofNullable(DomainBuilder.buildImovelTeste()));

        Imovel imovelRetornado =  imovelService.listarImoveisPorId(DomainBuilder.buildImovelTeste().getId());

        Assertions.assertEquals(imovelRetornado.getId(), DomainBuilder.buildImovelTeste().getId());
        Mockito.verify(imovelRepository).findByIdAndEstaAtivoTrue(DomainBuilder.buildImovelTeste().getId());

    }

    @Test
    @DisplayName("deve lançar IdInvalidoException")
    public void deveRetornarException() {
        Mockito.when(imovelRepository.findByIdAndEstaAtivoTrue(DomainBuilder.buildImovelTeste().getId())).thenThrow(IdInvalidoException.class);

        Assertions.assertThrows(IdInvalidoException.class, () -> imovelService.listarImoveisPorId(DomainBuilder.buildImovelTeste().getId()));
    }

    @Test
    @DisplayName("deve deletar imovel")
    public void deveDeletarImovel() {
        Mockito.when(imovelRepository.findByIdAndEstaAtivoTrue(DomainBuilder.buildImovelTeste().getId())).thenReturn(Optional.ofNullable(DomainBuilder.buildImovelTeste()));

        imovelService.deletarImovel(DomainBuilder.buildImovelTeste().getId());

        Mockito.verify(imovelRepository, Mockito.times(1)).save(ArgumentMatchers.any(Imovel.class));
    }

    @Test
    @DisplayName("deve retornar exception ao deletar imovel")
    public void naoDeveDeletarImovel() {
        Mockito.when(imovelRepository.findByIdAndEstaAtivoTrue(DomainBuilder.buildImovelTeste().getId())).thenThrow(IdInvalidoException.class);

        Assertions.assertThrows(IdInvalidoException.class, () -> imovelService.deletarImovel(DomainBuilder.buildImovelTeste().getId()));
    }

    @Test
    @DisplayName("deve retornar true")
    public void deveRetornarTrueAImovelExiste() {
        Mockito.when(imovelRepository.existsById(DomainBuilder.buildImovelTeste().getId())).thenReturn(true);

        Assertions.assertTrue(imovelService.imovelExiste(DomainBuilder.buildImovelTeste().getId()));
    }

    @Test
    @DisplayName("deve retornar false")
    public void deveRetornarFalseAImovelExiste() {
        Assertions.assertFalse(imovelService.imovelExiste(DomainBuilder.buildImovelTeste().getId()));
    }

}
