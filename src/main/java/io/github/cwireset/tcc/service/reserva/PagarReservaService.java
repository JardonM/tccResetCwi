package io.github.cwireset.tcc.service.reserva;

import io.github.cwireset.tcc.domain.*;
import io.github.cwireset.tcc.exception.FormaPagamentoInvalidoException;
import io.github.cwireset.tcc.exception.IdInvalidoException;
import io.github.cwireset.tcc.exception.StatusPagamentoReservaInvalidoException;
import io.github.cwireset.tcc.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PagarReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public Reserva pagarReserva(Long idReserva, FormaPagamento formaPagamento) {
        Reserva reserva = reservaRepository.findById(idReserva).orElseThrow(() -> new IdInvalidoException("Reserva", idReserva));
        verificarFormaPagamento(reserva, formaPagamento);
        verificarStatusPagamentoReserva(reserva);
        reserva.getPagamento().setStatus(StatusPagamento.PAGO);
        return reservaRepository.save(reserva);
    }

    private void verificarStatusPagamentoReserva(Reserva reserva) {
        if(reserva.getPagamento().getStatus().equals(StatusPagamento.PAGO) || (reserva.getPagamento().getStatus().equals(StatusPagamento.CANCELADO)) || (reserva.getPagamento().getStatus().equals(StatusPagamento.ESTORNADO))){
            throw new StatusPagamentoReservaInvalidoException("pagamento", "PENDENTE");
        }
    }

    private void verificarFormaPagamento(Reserva reserva, FormaPagamento formaPagamento) {
        Anuncio anuncio = reserva.getAnuncio();
        if(!anuncio.getFormasAceitas().contains(formaPagamento)){
            throw new FormaPagamentoInvalidoException(formaPagamento, anuncio.getFormasAceitas());
        }
    }
}
