package io.github.cwireset.tcc.service.reserva;


import io.github.cwireset.tcc.builds.DomainBuilder;
import io.github.cwireset.tcc.domain.FormaPagamento;
import io.github.cwireset.tcc.domain.Reserva;
import io.github.cwireset.tcc.domain.StatusPagamento;
import io.github.cwireset.tcc.exception.FormaPagamentoInvalidoException;
import io.github.cwireset.tcc.exception.IdInvalidoException;
import io.github.cwireset.tcc.exception.StatusPagamentoReservaInvalidoException;
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

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("PagarReservaServiceTest")
public class PagarReservaServiceTest {

    @MockBean
    private ReservaRepository reservaRepository;

    @Autowired
    private PagarReservaService pagarReservaService;


    @Test
    @DisplayName("deve pagar uma reserva")
    public void devePagarReserva() {
        Mockito.when(reservaRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.ofNullable(DomainBuilder.buildReservaTeste()));
        Mockito.when(reservaRepository.save(ArgumentMatchers.any(Reserva.class))).thenReturn(DomainBuilder.buildReservaPagoTeste());

        Reserva reserva = DomainBuilder.buildReservaTeste();
        Assertions.assertEquals(StatusPagamento.PENDENTE, reserva.getPagamento().getStatus());

        reserva = pagarReservaService.pagarReserva(reserva.getId(), FormaPagamento.DINHEIRO);

        Mockito.verify(reservaRepository).save(ArgumentMatchers.any(Reserva.class));
        Assertions.assertEquals(StatusPagamento.PAGO, reserva.getPagamento().getStatus());
        Assertions.assertEquals(FormaPagamento.DINHEIRO, reserva.getPagamento().getFormaEscolhida());
    }

    @Test
    @DisplayName("nao deve pagar reserva / lancar IdInvalidoException")
    public void naoDevePagarIdInvalidoException() {
        Assertions.assertThrows(IdInvalidoException.class, () -> pagarReservaService.pagarReserva(DomainBuilder.buildReservaTeste().getId(), FormaPagamento.DINHEIRO));
    }

    @Test
    @DisplayName("nao deve pagar reserva paga / PagamentoReservaInvalidoException")
    public void naoDevePagarReservaPaga() {
        Mockito.when(reservaRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.ofNullable(DomainBuilder.buildReservaPagoTeste()));

        Assertions.assertThrows(StatusPagamentoReservaInvalidoException.class, () -> pagarReservaService.pagarReserva(DomainBuilder.buildReservaTeste().getId(), FormaPagamento.DINHEIRO));

    }

    @Test
    @DisplayName("nao deve pagar reserva cancelada / PagamentoReservaInvalidoException")
    public void naoDevePagarReservaCancelada() {
        Reserva reserva = DomainBuilder.buildReservaTeste();
        reserva.getPagamento().setStatus(StatusPagamento.CANCELADO);
        Mockito.when(reservaRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(reserva));

        Assertions.assertThrows(StatusPagamentoReservaInvalidoException.class, () -> pagarReservaService.pagarReserva(DomainBuilder.buildReservaTeste().getId(), FormaPagamento.DINHEIRO));

    }


    @Test
    @DisplayName("nao deve pagar reserva estornada / PagamentoReservaInvalidoException")
    public void naoDevePagarReservaEstornada() {
        Reserva reserva = DomainBuilder.buildReservaTeste();
        reserva.getPagamento().setStatus(StatusPagamento.ESTORNADO);
        Mockito.when(reservaRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(reserva));

        Assertions.assertThrows(StatusPagamentoReservaInvalidoException.class, () -> pagarReservaService.pagarReserva(DomainBuilder.buildReservaTeste().getId(), FormaPagamento.DINHEIRO));

    }

    @Test
    @DisplayName("nao deve pagar reserva / FormaPagamentoInvalidoException")
    public void naoDevePagarReservaFormaPagamentoInvalida() {
        Mockito.when(reservaRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.ofNullable(DomainBuilder.buildReservaTeste()));

        Assertions.assertThrows(FormaPagamentoInvalidoException.class, () -> pagarReservaService.pagarReserva(DomainBuilder.buildReservaTeste().getId(), FormaPagamento.PIX));
    }

}
