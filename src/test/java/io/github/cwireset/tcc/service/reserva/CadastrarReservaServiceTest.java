package io.github.cwireset.tcc.service.reserva;

import io.github.cwireset.tcc.builds.DomainBuilder;
import io.github.cwireset.tcc.domain.*;
import io.github.cwireset.tcc.exception.AnuncioJaReservadoException;
import io.github.cwireset.tcc.exception.MinimoReservaException;
import io.github.cwireset.tcc.exception.PeriodoDiariaInvalidaException;
import io.github.cwireset.tcc.exception.SolicitanteInvalidoException;
import io.github.cwireset.tcc.repository.ReservaRepository;
import io.github.cwireset.tcc.response.InformacaoReservaResponse;
import io.github.cwireset.tcc.service.anuncio.AnuncioService;
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



@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("CadastrarReservaServiceTest")
public class CadastrarReservaServiceTest {

    @MockBean
    private ReservaRepository reservaRepository;

    @MockBean
    private AnuncioService anuncioService;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private CadastrarReservaService cadastrarReservaService;

    private DomainBuilder getDomain = new DomainBuilder();


    @Test
    @DisplayName("deve cadastrar reserva")
    public void deveCadastrarReserva() {
        Mockito.when(reservaRepository.existsByAnuncioAndPeriodoAndStatusPagamento(DomainBuilder.buildCadastrarReservaRequestTeste().getIdAnuncio(), DomainBuilder.buildCadastrarReservaRequestTeste().getPeriodo().getDataHoraInicial(), DomainBuilder.buildCadastrarReservaRequestTeste().getPeriodo().getDataHoraFinal(), StatusPagamento.PAGO, StatusPagamento.PENDENTE))
                .thenReturn(false);
        Mockito.when(anuncioService.buscarAnuncioPorId(DomainBuilder.buildCadastrarReservaRequestTeste().getIdAnuncio())).thenReturn(DomainBuilder.buildAnuncioTeste());
        Mockito.when(reservaRepository.save(ArgumentMatchers.any(Reserva.class))).thenReturn(DomainBuilder.buildReservaTeste());


        InformacaoReservaResponse infoRetornada =  cadastrarReservaService.cadastrarReserva(DomainBuilder.buildCadastrarReservaRequestTeste());

        Mockito.verify(reservaRepository).existsByAnuncioAndPeriodoAndStatusPagamento(DomainBuilder.buildCadastrarReservaRequestTeste().getIdAnuncio(), DomainBuilder.buildCadastrarReservaRequestTeste().getPeriodo().getDataHoraInicial(), DomainBuilder.buildCadastrarReservaRequestTeste().getPeriodo().getDataHoraFinal(), StatusPagamento.PAGO, StatusPagamento.PENDENTE);
        Mockito.verify(reservaRepository).save(ArgumentMatchers.any(Reserva.class));
        Assertions.assertEquals(DomainBuilder.buildInformacaoReservaTeste().getIdReserva(), infoRetornada.getIdReserva());
    }

    @Test
    @DisplayName("nao deve cadastrar/ deve lancar SolicitanteInvalidoException")
    public void naoDeveCadastrarSolicitanteInvalido() {
        Mockito.when(anuncioService.buscarAnuncioPorId(DomainBuilder.buildAnuncioTeste().getId())).thenReturn(DomainBuilder.buildAnuncioTeste());
        Assertions.assertThrows(SolicitanteInvalidoException.class, () -> cadastrarReservaService.cadastrarReserva(DomainBuilder.buildCadastrarReservaRequestSolicitanteInvalidoTeste()));
    }

    @Test
    @DisplayName("nao deve cadastrar/periodo reserva mesmo dia ")
    public void naoDeveCadastrarPeriodoReservaMesmoDia() {
        Mockito.when(anuncioService.buscarAnuncioPorId(DomainBuilder.buildAnuncioTeste().getId())).thenReturn(DomainBuilder.buildAnuncioTeste());
        Assertions.assertThrows(PeriodoDiariaInvalidaException.class, () -> cadastrarReservaService.cadastrarReserva(DomainBuilder.buildCadastrarReservaRequestPeriodoReservaMesmoDia()));
    }

    @Test
    @DisplayName("nao deve cadastrar/retorna AnuncioJaReservadoException")
    public void naoDeveCadastrarReservaPorAnuncioInexistente() {
        Mockito.when(anuncioService.buscarAnuncioPorId(DomainBuilder.buildAnuncioTeste().getId())).thenReturn(DomainBuilder.buildAnuncioTeste());
        Mockito.when(reservaRepository.existsByAnuncioAndPeriodoAndStatusPagamento(DomainBuilder.buildCadastrarReservaRequestTeste().getIdAnuncio(), DomainBuilder.buildCadastrarReservaRequestTeste().getPeriodo().getDataHoraInicial(), DomainBuilder.buildCadastrarReservaRequestTeste().getPeriodo().getDataHoraFinal(), StatusPagamento.PAGO, StatusPagamento.PENDENTE))
                .thenReturn(true);
        Assertions.assertThrows(AnuncioJaReservadoException.class, () -> cadastrarReservaService.cadastrarReserva(DomainBuilder.buildCadastrarReservaRequestTeste()));
    }

    @Test
    @DisplayName("nao deve cadastrar / diarias invalidas Pousada")
    public void naoDeveCadastrarQuantiaDiariasInvalida() {
        Mockito.when(anuncioService.buscarAnuncioPorId(DomainBuilder.buildAnuncioPousadaTeste().getId())).thenReturn(DomainBuilder.buildAnuncioPousadaTeste());
        Mockito.when(reservaRepository.existsByAnuncioAndPeriodoAndStatusPagamento(DomainBuilder.buildCadastrarReservaRequestTeste().getIdAnuncio(), DomainBuilder.buildCadastrarReservaRequestTeste().getPeriodo().getDataHoraInicial(), DomainBuilder.buildCadastrarReservaRequestTeste().getPeriodo().getDataHoraFinal(), StatusPagamento.PAGO, StatusPagamento.PENDENTE))
                .thenReturn(false);
        Mockito.when(anuncioService.buscarAnuncioPorId(DomainBuilder.buildCadastrarReservaRequestTipoImovelPousada().getIdAnuncio())).thenReturn(DomainBuilder.buildAnuncioPousadaTeste());

        Assertions.assertThrows(MinimoReservaException.class, () -> cadastrarReservaService.cadastrarReserva(DomainBuilder.buildCadastrarReservaRequestTipoImovelPousada()));
    }

    @Test
    @DisplayName("nao deve cadastrar / quantidade minima pessoas invalido hotel")
    public void naoDeveCadastrarQuantiaPessoasInvalida() {
        Mockito.when(anuncioService.buscarAnuncioPorId(DomainBuilder.buildAnuncioTeste().getId())).thenReturn(DomainBuilder.buildAnuncioTeste());
        Mockito.when(reservaRepository.existsByAnuncioAndPeriodoAndStatusPagamento(DomainBuilder.buildCadastrarReservaRequestTeste().getIdAnuncio(), DomainBuilder.buildCadastrarReservaRequestTeste().getPeriodo().getDataHoraInicial(), DomainBuilder.buildCadastrarReservaRequestTeste().getPeriodo().getDataHoraFinal(), StatusPagamento.PAGO, StatusPagamento.PENDENTE))
                .thenReturn(false);
        Mockito.when(anuncioService.buscarAnuncioPorId(DomainBuilder.buildCadastrarReservaRequestQuantidadePessoasInvalidoTeste().getIdAnuncio())).thenReturn(DomainBuilder.buildAnuncioTeste());

        Assertions.assertThrows(MinimoReservaException.class, () -> cadastrarReservaService.cadastrarReserva(DomainBuilder.buildCadastrarReservaRequestQuantidadePessoasInvalidoTeste()));
    }

}
