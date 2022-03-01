package io.github.cwireset.tcc.repository;

import io.github.cwireset.tcc.domain.Periodo;
import io.github.cwireset.tcc.domain.Reserva;
import io.github.cwireset.tcc.domain.StatusPagamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;


public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    Page<Reserva> findAllBySolicitanteId(Long idSolicitante, Pageable pageable);

    @Query("select r from Reserva r where r.solicitante.id = ?1 and (r.periodo.dataHoraInicial between ?2 and ?3 or r.periodo.dataHoraFinal between ?2 and ?3)")
    Page<Reserva> findBySolicitante_IdEqualsAndPeriodo_DataHoraInicialIsBetweenOrPeriodo_DataHoraFinalIsBetween(Long id, LocalDateTime dataHoraInicialStart, LocalDateTime dataHoraInicialEnd, Pageable pageable);

    Page<Reserva> findAllByAnuncioAnuncianteId(Long idAnunciante, Pageable pageable);

    @Query("select (count(r) > 0) from Reserva r where (r.anuncio.estaAtivo = true and r.anuncio.id = ?1) and (r.pagamento.status = ?4 or r.pagamento.status = ?5) and ((r.periodo.dataHoraInicial between ?2 and ?3 or r.periodo.dataHoraFinal between ?2 and ?3) or (r.periodo.dataHoraInicial < ?2 and r.periodo.dataHoraFinal > ?3))")
    boolean existsByAnuncioAndPeriodoAndStatusPagamento(Long idAnuncio, LocalDateTime dataHoraInicial, LocalDateTime dataHoraFinal, StatusPagamento status, StatusPagamento status1);

}
