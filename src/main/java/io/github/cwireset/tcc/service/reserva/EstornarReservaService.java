package io.github.cwireset.tcc.service.reserva;

import io.github.cwireset.tcc.domain.Reserva;
import io.github.cwireset.tcc.domain.StatusPagamento;
import io.github.cwireset.tcc.exception.IdInvalidoException;
import io.github.cwireset.tcc.exception.StatusPagamentoReservaInvalidoException;
import io.github.cwireset.tcc.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstornarReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public void estornarReserva(Long idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva).orElseThrow(() -> new IdInvalidoException("Reserva", idReserva));
        verificarStatusReserva(reserva);
        reserva.getPagamento().setFormaEscolhida(null);
        reserva.getPagamento().setStatus(StatusPagamento.ESTORNADO);
        reservaRepository.save(reserva);
    }

    private void verificarStatusReserva(Reserva reserva) {
        if((reserva.getPagamento().getStatus().equals(StatusPagamento.PENDENTE)) || (reserva.getPagamento().getStatus().equals(StatusPagamento.ESTORNADO)) || (reserva.getPagamento().getStatus().equals(StatusPagamento.CANCELADO))) {
            throw new StatusPagamentoReservaInvalidoException("estorno", "PAGO");
        }
    }
}
