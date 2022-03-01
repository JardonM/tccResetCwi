package io.github.cwireset.tcc.service.reserva;

import io.github.cwireset.tcc.domain.*;
import io.github.cwireset.tcc.exception.*;
import io.github.cwireset.tcc.repository.ReservaRepository;
import io.github.cwireset.tcc.request.CadastrarReservaRequest;
import io.github.cwireset.tcc.response.DadosAnuncioResponse;
import io.github.cwireset.tcc.response.DadosSolicitanteResponse;
import io.github.cwireset.tcc.response.InformacaoReservaResponse;
import io.github.cwireset.tcc.service.anuncio.AnuncioService;
import io.github.cwireset.tcc.service.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service
public class CadastrarReservaService {

    private ReservaRepository reservaRepository;
    private AnuncioService anuncioService;
    private UsuarioService usuarioService;

    @Autowired
    public CadastrarReservaService(ReservaRepository reservaRepository, AnuncioService anuncioService, UsuarioService usuarioService) {
        this.reservaRepository = reservaRepository;
        this.anuncioService = anuncioService;
        this.usuarioService = usuarioService;
    }

    public InformacaoReservaResponse cadastrarReserva(CadastrarReservaRequest cadastrarReservaRequest) {
        verificarSolicitante(cadastrarReservaRequest);
        verificarHorarioPeriodoReserva(cadastrarReservaRequest);
        verificarReserva(cadastrarReservaRequest);
        verificarTipoDoImovel(cadastrarReservaRequest);

        Pagamento pagamento = Pagamento.builder()
                .valorTotal(calcularValorTotal(cadastrarReservaRequest))
                .status(StatusPagamento.PENDENTE)
                .build();

        Reserva reserva = Reserva.builder()
                .dataHoraReserva(LocalDateTime.now())
                .pagamento(pagamento)
                .solicitante(usuarioService.listarUsuarioPorId(cadastrarReservaRequest.getIdSolicitante()))
                .anuncio(anuncioService.buscarAnuncioPorId(cadastrarReservaRequest.getIdAnuncio()))
                .periodo(cadastrarReservaRequest.getPeriodo())
                .quantidadePessoas(cadastrarReservaRequest.getQuantidadePessoas())
                .build();
        return cadastrarInformacoesReservaResponse(reservaRepository.save(reserva));
    }

    private InformacaoReservaResponse cadastrarInformacoesReservaResponse(Reserva reserva) {
        return InformacaoReservaResponse.builder()
                .idReserva(reserva.getId())
                .solicitante(cadastrarInformacoesSolicitante(reserva.getSolicitante()))
                .quantidadePessoas(reserva.getQuantidadePessoas())
                .anuncio(cadastrarInformacoesAnuncio(reserva.getAnuncio()))
                .periodo(reserva.getPeriodo())
                .pagamento(reserva.getPagamento())
                .build();
    }

    private DadosSolicitanteResponse cadastrarInformacoesSolicitante(Usuario solicitante) {
        return DadosSolicitanteResponse.builder()
                .id(solicitante.getId())
                .nome(solicitante.getNome())
                .build();
    }

    private DadosAnuncioResponse cadastrarInformacoesAnuncio(Anuncio anuncio) {
        return DadosAnuncioResponse.builder()
                .id(anuncio.getId())
                .imovel(anuncio.getImovel())
                .anunciante(anuncio.getAnunciante())
                .formasAceitas(anuncio.getFormasAceitas())
                .descricao(anuncio.getDescricao())
                .build();
    }

    private void verificarSolicitante(CadastrarReservaRequest cadastrarReservaRequest){
        if(Objects.equals(cadastrarReservaRequest.getIdSolicitante(), anuncioService.buscarAnuncioPorId(cadastrarReservaRequest.getIdAnuncio()).getAnunciante().getId())){
            throw new SolicitanteInvalidoException();
        }
    }

    private void verificarReserva(CadastrarReservaRequest cadastrarReservaRequest) {
        if(reservaRepository.existsByAnuncioAndPeriodoAndStatusPagamento(cadastrarReservaRequest.getIdAnuncio(), cadastrarReservaRequest.getPeriodo().getDataHoraInicial(), cadastrarReservaRequest.getPeriodo().getDataHoraFinal(), StatusPagamento.PAGO, StatusPagamento.PENDENTE)){
            throw new AnuncioJaReservadoException();
        }
    }


    private void verificarTipoDoImovel(CadastrarReservaRequest cadastrarReservaRequest) {
        verificarTipoImovelHotel(cadastrarReservaRequest);
        verificarTipoImovelPousada(cadastrarReservaRequest);
    }

    private Long calcularDiarias(CadastrarReservaRequest cadastrarReservaRequest) {
        return ChronoUnit.DAYS.between(cadastrarReservaRequest.getPeriodo().getDataHoraInicial(), cadastrarReservaRequest.getPeriodo().getDataHoraFinal()) + 2;
    }

    private void verificarTipoImovelPousada(CadastrarReservaRequest cadastrarReservaRequest) {
        if((anuncioService.buscarAnuncioPorId(cadastrarReservaRequest.getIdAnuncio()).getImovel().getTipoImovel().equals(TipoImovel.POUSADA)) &&
                (calcularDiarias(cadastrarReservaRequest) < 5)){
                throw new MinimoReservaException(5,"diárias", "Pousada");
        }
    }

    private void verificarTipoImovelHotel(CadastrarReservaRequest cadastrarReservaRequest) {
        if(anuncioService.buscarAnuncioPorId(cadastrarReservaRequest.getIdAnuncio()).getImovel().getTipoImovel().equals(TipoImovel.HOTEL) &&
                cadastrarReservaRequest.getQuantidadePessoas() < 2){
            throw new MinimoReservaException(2, "pessoas", "Hotel");
        }
    }

    private BigDecimal calcularValorTotal(CadastrarReservaRequest cadastrarReservaRequest) {
        Anuncio anuncio = anuncioService.buscarAnuncioPorId(cadastrarReservaRequest.getIdAnuncio());
        BigDecimal diarias = new BigDecimal(Math.toIntExact(calcularDiarias(cadastrarReservaRequest)));
        return diarias.multiply(BigDecimal.valueOf(anuncio.getValorDiaria().doubleValue()));
    }

    private void verificarHorarioPeriodoReserva(CadastrarReservaRequest cadastrarReservaRequest) {
        verificarDiarias(cadastrarReservaRequest);
        verificarReservaComDataInvalida(cadastrarReservaRequest);
        verificarDataHoraInicial(cadastrarReservaRequest);
        verificarDataHoraFinal(cadastrarReservaRequest);
    }

    private void verificarDiarias(CadastrarReservaRequest cadastrarReservaRequest) {
        if(cadastrarReservaRequest.getPeriodo().getDataHoraInicial().getDayOfMonth() == cadastrarReservaRequest.getPeriodo().getDataHoraFinal().getDayOfMonth()) {
            throw new PeriodoDiariaInvalidaException();
        }
    }

    private void verificarReservaComDataInvalida(CadastrarReservaRequest cadastrarReservaRequest) {
        if(cadastrarReservaRequest.getPeriodo().getDataHoraFinal().isBefore(cadastrarReservaRequest.getPeriodo().getDataHoraInicial())) {
            throw new PeriodoInvalidoException();
        }
    }

    private void verificarDataHoraInicial(CadastrarReservaRequest cadastrarReservaRequest) {
        if(cadastrarReservaRequest.getPeriodo().getDataHoraInicial().getHour() != 14){
            cadastrarReservaRequest.getPeriodo().setDataHoraInicial(cadastrarReservaRequest.getPeriodo().getDataHoraInicial().withHour(14).withMinute(0));
        }
    }

    private void verificarDataHoraFinal(CadastrarReservaRequest cadastrarReservaRequest) {
        if(cadastrarReservaRequest.getPeriodo().getDataHoraFinal().getHour() != 12) {
            cadastrarReservaRequest.getPeriodo().setDataHoraFinal(cadastrarReservaRequest.getPeriodo().getDataHoraFinal().withHour(12).withMinute(0));
        }
    }
}
