package io.github.cwireset.tcc.service.reserva;


import io.github.cwireset.tcc.domain.Periodo;
import io.github.cwireset.tcc.domain.Reserva;
import io.github.cwireset.tcc.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ListarReservaService {

    @Autowired
    private ReservaRepository reservaRepository;


      public Page<Reserva> listarReservaPorSolicitante(Long idSolicitante, Pageable pageable,Periodo periodo) {
          Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("periodo.dataHoraFinal").descending());
            if(periodo.getDataHoraFinal() == null || periodo.getDataHoraInicial() == null) {
                return reservaRepository.findAllBySolicitanteId(idSolicitante, pageableWithSort);
            } else {
                return reservaRepository.findBySolicitante_IdEqualsAndPeriodo_DataHoraInicialIsBetweenOrPeriodo_DataHoraFinalIsBetween(idSolicitante, periodo.getDataHoraInicial(), periodo.getDataHoraFinal(), pageableWithSort);
            }
    }


    public Page<Reserva> listarReservaPorAnunciante(Long idAnunciante, Pageable pageable) {
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("PeriodoDataHoraFinal").descending());
        return reservaRepository.findAllByAnuncioAnuncianteId(idAnunciante, pageableWithSort);
    }

}
