package io.github.cwireset.tcc.service.reserva;

import io.github.cwireset.tcc.domain.Reserva;
import io.github.cwireset.tcc.domain.StatusPagamento;
import io.github.cwireset.tcc.exception.IdInvalidoException;
import io.github.cwireset.tcc.exception.StatusPagamentoReservaInvalidoException;
import io.github.cwireset.tcc.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CancelarReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public void cancelarReserva(Long idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva).orElseThrow(() -> new IdInvalidoException("Reserva", idReserva));
        verificarStatusReserva(reserva);
        reserva.getPagamento().setStatus(StatusPagamento.CANCELADO);
        reservaRepository.save(reserva);
    }

    private void verificarStatusReserva(Reserva reserva) {
        if(reserva.getPagamento().getStatus().equals(StatusPagamento.PAGO) || (reserva.getPagamento().getStatus().equals(StatusPagamento.ESTORNADO)) || (reserva.getPagamento().getStatus().equals(StatusPagamento.CANCELADO))) {
            throw new StatusPagamentoReservaInvalidoException("cancelamento", "PENDENTE");
        }
    }
}
