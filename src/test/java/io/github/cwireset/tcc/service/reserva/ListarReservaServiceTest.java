package io.github.cwireset.tcc.service.reserva;

import io.github.cwireset.tcc.builds.DomainBuilder;
import io.github.cwireset.tcc.domain.Periodo;
import io.github.cwireset.tcc.domain.Reserva;
import io.github.cwireset.tcc.repository.ReservaRepository;
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

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ListarReservaServiceTest")
public class ListarReservaServiceTest {

    @MockBean
    private ReservaRepository repository;

    @Autowired
    private ListarReservaService service;

    @Test
    @DisplayName("deve listar reserva por solicitante sem periodo")
    public void deveListarReservaPorSolicitanteSemPeriodo() {
        Pageable pageableWithSort = PageRequest.of(0, 5, Sort.by("PeriodoDataHoraFinal").descending());
        Mockito.when(repository.findAllBySolicitanteId(DomainBuilder.buildUsuarioTesteSolicitante().getId(), pageableWithSort)).thenReturn(Page.empty());

        Page<Reserva> reservaPage = service.listarReservaPorSolicitante(DomainBuilder.buildUsuarioTesteSolicitante().getId(), pageableWithSort, new Periodo());

        Mockito.verify(repository).findAllBySolicitanteId(DomainBuilder.buildUsuarioTesteSolicitante().getId(), pageableWithSort);
        Assertions.assertEquals(1, reservaPage.getTotalPages());
    }

    @Test
    @DisplayName("deve listar reserva por solicitante com periodo")
    public void deveListarReservaPorSilicitanteComPeriodo() {
        Pageable pageableWithSort = PageRequest.of(0, 5, Sort.by("PeriodoDataHoraFinal").descending());
        Mockito.when(repository.findBySolicitante_IdEqualsAndPeriodo_DataHoraInicialIsBetweenOrPeriodo_DataHoraFinalIsBetween(ArgumentMatchers.any(Long.class), ArgumentMatchers.any(LocalDateTime.class), ArgumentMatchers.any(LocalDateTime.class), ArgumentMatchers.any(Pageable.class))).thenReturn(Page.empty());

        Page<Reserva> reservaPage = service.listarReservaPorSolicitante(DomainBuilder.buildUsuarioTesteSolicitante().getId(), pageableWithSort, DomainBuilder.periodoTeste());

        Mockito.verify(repository).findBySolicitante_IdEqualsAndPeriodo_DataHoraInicialIsBetweenOrPeriodo_DataHoraFinalIsBetween(ArgumentMatchers.any(Long.class), ArgumentMatchers.any(LocalDateTime.class), ArgumentMatchers.any(LocalDateTime.class), ArgumentMatchers.any(Pageable.class));
        Assertions.assertEquals(1, reservaPage.getTotalPages());
    }

    @Test
    @DisplayName("deve listar reserva por solicitante com periodo null")
    public void deveListarReservaPorSolicitantePeriodoNull() {
        Pageable pageableWithSort = PageRequest.of(0, 5, Sort.by("PeriodoDataHoraFinal").descending());
        Mockito.when(repository.findAllBySolicitanteId(ArgumentMatchers.any(Long.class), ArgumentMatchers.any(Pageable.class))).thenReturn(Page.empty());

        Periodo periodoTeste = DomainBuilder.periodoTeste();
        periodoTeste.setDataHoraFinal(null);

        Page<Reserva> reservaPage = service.listarReservaPorSolicitante(DomainBuilder.buildUsuarioTesteSolicitante().getId(), pageableWithSort, periodoTeste);

        Mockito.verify(repository).findAllBySolicitanteId(ArgumentMatchers.any(Long.class), ArgumentMatchers.any(Pageable.class));
        Assertions.assertEquals(1, reservaPage.getTotalPages());
    }

    @Test
    @DisplayName("deve listar reserva por Anunciante")
    public void deveListarReservaPorAnunciante() {
        Pageable pageableWithSort = PageRequest.of(0, 5, Sort.by("PeriodoDataHoraFinal").descending());
        Mockito.when(repository.findAllByAnuncioAnuncianteId(DomainBuilder.buildAnuncioTeste().getAnunciante().getId(), pageableWithSort)).thenReturn(Page.empty());

        Page<Reserva> reservaPage = service.listarReservaPorAnunciante(DomainBuilder.buildAnuncioTeste().getAnunciante().getId(), pageableWithSort);

        Mockito.verify(repository).findAllByAnuncioAnuncianteId(DomainBuilder.buildAnuncioTeste().getAnunciante().getId(), pageableWithSort);
        Assertions.assertEquals(1, reservaPage.getTotalPages());
    }


}


