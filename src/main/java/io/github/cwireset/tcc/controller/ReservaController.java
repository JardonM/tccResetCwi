package io.github.cwireset.tcc.controller;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.cwireset.tcc.domain.FormaPagamento;
import io.github.cwireset.tcc.domain.Periodo;
import io.github.cwireset.tcc.domain.Reserva;
import io.github.cwireset.tcc.request.CadastrarReservaRequest;
import io.github.cwireset.tcc.response.InformacaoReservaResponse;
import io.github.cwireset.tcc.service.reserva.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private CadastrarReservaService cadastrarReservaService;
    private ListarReservaService listarReservaService;
    private PagarReservaService pagarReservaService;
    private CancelarReservaService cancelarReservaService;
    private EstornarReservaService estornarReservaService;

    @Autowired
    public ReservaController(CadastrarReservaService cadastrarReservaService, ListarReservaService listarReservaService, PagarReservaService pagarReservaService, CancelarReservaService cancelarReservaService, EstornarReservaService estornarReservaService) {
        this.cadastrarReservaService = cadastrarReservaService;
        this.listarReservaService = listarReservaService;
        this.pagarReservaService = pagarReservaService;
        this.cancelarReservaService = cancelarReservaService;
        this.estornarReservaService = estornarReservaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InformacaoReservaResponse cadastrarReserva(@RequestBody CadastrarReservaRequest cadastrarReservaRequest) {
        return cadastrarReservaService.cadastrarReserva(cadastrarReservaRequest);
    }


    @GetMapping(path = "/solicitantes/{idSolicitante}")
    public Page<Reserva> listarReservaPorSolicitante(@PathVariable Long idSolicitante, @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Periodo periodo, @PageableDefault Pageable pageable) {
        return listarReservaService.listarReservaPorSolicitante(idSolicitante, pageable, periodo);
    }

    @GetMapping(path = "/anuncios/anunciantes/{idAnunciante}")
    public Page<Reserva> listarReservaPorAnunciante(@PathVariable Long idAnunciante, @PageableDefault Pageable pageable) {
        return listarReservaService.listarReservaPorAnunciante(idAnunciante, pageable);
    }

    @PutMapping(path = "/{idReserva}/pagamentos")
    public void pagarReserva(@PathVariable Long idReserva, @RequestBody FormaPagamento formaPagamento) {
        pagarReservaService.pagarReserva(idReserva, formaPagamento);
    }

    @PutMapping(path = "/{idReserva}/pagamentos/cancelar")
    public void cancelarReserva(@PathVariable Long idReserva) {
        cancelarReservaService.cancelarReserva(idReserva);
    }

    @PutMapping(path = "/{idReserva}/pagamentos/estornar")
    public void estornarReserva(@PathVariable Long idReserva) {
        estornarReservaService.estornarReserva(idReserva);
    }
}
