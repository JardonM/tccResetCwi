package io.github.cwireset.tcc.builds;

import io.github.cwireset.tcc.domain.*;
import io.github.cwireset.tcc.request.CadastrarAnuncioRequest;
import io.github.cwireset.tcc.request.CadastrarImovelRequest;
import io.github.cwireset.tcc.request.CadastrarReservaRequest;
import io.github.cwireset.tcc.response.DadosAnuncioResponse;
import io.github.cwireset.tcc.response.DadosSolicitanteResponse;
import io.github.cwireset.tcc.response.InformacaoReservaResponse;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Getter
public class DomainBuilder {


    public static Usuario buildUsuarioTeste() {
        return Usuario.builder()
                .id(66L)
                .nome("Usuario Teste")
                .email("usuarioteste@teste.com")
                .senha("5sd5s4d8f85")
                .cpf("55544499977")
                .dataNascimento(LocalDate.of(1980,12,01))
                .endereco(null)
                .imagemAvatar("Imagem Avatar")
                .build();
    }


    public static Usuario buildUsuarioTesteSolicitante() {
        return Usuario.builder()
                .id(88L)
                .nome("Usuario Teste")
                .email("usuariotesteSolicitante@teste.com")
                .senha("5sd5s4d8f85")
                .cpf("55544499966")
                .dataNascimento(LocalDate.of(1980,12,01))
                .endereco(null)
                .imagemAvatar("Imagem Avatar")
                .build();
    }

    public static Usuario buildUsuarioTesteSolicitanteInvalido() {
        return Usuario.builder()
                .id(66L)
                .nome("Usuario Teste")
                .email("usuarioteste@teste.com")
                .senha("5sd5s4d8f85")
                .cpf("55544499977")
                .dataNascimento(LocalDate.of(1980,12,01))
                .endereco(null)
                .imagemAvatar("Imagem Avatar")
                .build();
    }

    public static Endereco buildEnderecoTeste() {
        return Endereco.builder()
                .bairro("Teste")
                .cep("99999-999")
                .cidade("Cidade do Teste")
                .id(100L)
                .estado("Testado")
                .logradouro("Rua dos Testes")
                .numero("88")
                .build();
    }


    public static Imovel buildImovelTeste() {
        return Imovel.builder()
                .proprietario(buildUsuarioTeste())
                .tipoImovel(TipoImovel.HOTEL)
                .endereco(buildEnderecoTeste())
                .identificacao("Casa grande perto da praia")
                .caracteristicas(Collections.emptyList())
                .estaAtivo(true)
                .build();
    }

    public static Imovel buildImovelTestePousada() {
        return Imovel.builder()
                .proprietario(buildUsuarioTeste())
                .tipoImovel(TipoImovel.POUSADA)
                .endereco(buildEnderecoTeste())
                .identificacao("Casa grande perto da praia")
                .caracteristicas(Collections.emptyList())
                .estaAtivo(true)
                .build();
    }


    public static CadastrarImovelRequest buildImovelRequestTeste() {
        return CadastrarImovelRequest.builder()
                .tipoImovel(TipoImovel.POUSADA)
                .endereco(buildEnderecoTeste())
                .identificacao("Casa grande perto da praia")
                .idProprietario(buildUsuarioTeste().getId())
                .caracteristicas(Collections.emptyList())
                .build();
    }


    public static List<FormaPagamento> buildFormasTeste() {
        return new ArrayList<FormaPagamento>(Collections.singleton(FormaPagamento.DINHEIRO));
    }


    public static Anuncio buildAnuncioTeste() {
        return Anuncio.builder()
                .estaAtivo(true)
                .descricao("teste")
                .anunciante(buildUsuarioTeste())
                .imovel(buildImovelTeste())
                .tipoAnuncio(TipoAnuncio.COMPLETO)
                .valorDiaria(BigDecimal.valueOf(50))
                .formasAceitas(buildFormasTeste())
                .build();
    }

    public static Anuncio buildAnuncioPousadaTeste() {
        return Anuncio.builder()
                .estaAtivo(true)
                .descricao("teste")
                .anunciante(buildUsuarioTeste())
                .imovel(buildImovelTestePousada())
                .tipoAnuncio(TipoAnuncio.COMPLETO)
                .valorDiaria(BigDecimal.valueOf(50))
                .formasAceitas(buildFormasTeste())
                .build();
    }


    public static CadastrarAnuncioRequest buildCadastrarAnuncioRequestTeste() {
        return CadastrarAnuncioRequest.builder()
                .descricao("teste")
                .idAnunciante(buildUsuarioTeste().getId())
                .idImovel(buildImovelTeste().getId())
                .tipoAnuncio(TipoAnuncio.COMPLETO)
                .valorDiaria(BigDecimal.valueOf(50))
                .formasAceitas(buildFormasTeste())
                .build();
    }





    public static Periodo periodoTeste() {
        Periodo periodoTeste = new Periodo();
        periodoTeste.setDataHoraInicial(LocalDateTime.of(2021, 11, 11, 14, 00));
        periodoTeste.setDataHoraFinal(LocalDateTime.of(2021, 11, 12, 12, 00));

        return periodoTeste;
    }

    public static CadastrarReservaRequest buildCadastrarReservaRequestTeste() {
        return CadastrarReservaRequest.builder()
                .idAnuncio(buildAnuncioTeste().getId())
                .idSolicitante(buildUsuarioTesteSolicitante().getId())
                .periodo(periodoTeste())
                .quantidadePessoas(2)
                .build();
    }

    public static CadastrarReservaRequest buildCadastrarReservaRequestQuantidadePessoasInvalidoTeste() {
        return CadastrarReservaRequest.builder()
                .idAnuncio(buildAnuncioTeste().getId())
                .idSolicitante(buildUsuarioTesteSolicitante().getId())
                .periodo(periodoTeste())
                .quantidadePessoas(1)
                .build();
    }

    public static CadastrarReservaRequest buildCadastrarReservaRequestSolicitanteInvalidoTeste() {
        return CadastrarReservaRequest.builder()
                .idAnuncio(buildAnuncioTeste().getId())
                .idSolicitante(buildUsuarioTesteSolicitanteInvalido().getId())
                .periodo(periodoTeste())
                .quantidadePessoas(2)
                .build();
    }

    private static Periodo periodoTesteMesmoDia() {
        Periodo periodoTeste = new Periodo();
        periodoTeste.setDataHoraInicial(LocalDateTime.of(2021, 11, 12, 14, 00));
        periodoTeste.setDataHoraFinal(LocalDateTime.of(2021, 11, 12, 12, 00));

        return periodoTeste;
    }

    public static CadastrarReservaRequest buildCadastrarReservaRequestPeriodoReservaMesmoDia() {
        return CadastrarReservaRequest.builder()
                .idAnuncio(buildAnuncioTeste().getId())
                .idSolicitante(buildUsuarioTesteSolicitante().getId())
                .periodo(periodoTesteMesmoDia())
                .quantidadePessoas(2)
                .build();
    }

    public static CadastrarReservaRequest buildCadastrarReservaRequestTipoImovelPousada() {
        return CadastrarReservaRequest.builder()
                .idAnuncio(buildAnuncioPousadaTeste().getId())
                .idSolicitante(buildUsuarioTesteSolicitante().getId())
                .periodo(periodoTeste())
                .quantidadePessoas(2)
                .build();
    }


    public static Pagamento buildPagamentoTeste() {
        return Pagamento.builder()
                .status(StatusPagamento.PENDENTE)
                .valorTotal(BigDecimal.valueOf(100))
                .build();
    }

    public static Pagamento buildPagamentoPagoTeste() {
        return Pagamento.builder()
                .status(StatusPagamento.PAGO)
                .formaEscolhida(FormaPagamento.DINHEIRO)
                .valorTotal(BigDecimal.valueOf(100))
                .build();
    }



    public static Reserva buildReservaTeste() {
        return Reserva.builder()
                .anuncio(buildAnuncioTeste())
                .dataHoraReserva(LocalDateTime.now())
                .id(66L)
                .pagamento(buildPagamentoTeste())
                .periodo(periodoTeste())
                .quantidadePessoas(2)
                .solicitante(buildUsuarioTesteSolicitante())
                .build();
    }

    public static Reserva buildReservaPagoTeste() {
        return Reserva.builder()
                .anuncio(buildAnuncioTeste())
                .dataHoraReserva(LocalDateTime.now())
                .id(66L)
                .pagamento(buildPagamentoPagoTeste())
                .periodo(periodoTeste())
                .quantidadePessoas(2)
                .solicitante(buildUsuarioTesteSolicitante())
                .build();
    }



    private static DadosSolicitanteResponse informacoesSolicitanteTeste() {
        return DadosSolicitanteResponse.builder()
                .id(buildUsuarioTesteSolicitante().getId())
                .nome(buildUsuarioTesteSolicitante().getNome())
                .build();
    }

    private static DadosAnuncioResponse informacoesAnuncioTeste() {
        return DadosAnuncioResponse.builder()
                .id(buildAnuncioTeste().getId())
                .imovel(buildAnuncioTeste().getImovel())
                .anunciante(buildAnuncioTeste().getAnunciante())
                .formasAceitas(buildAnuncioTeste().getFormasAceitas())
                .descricao(buildAnuncioTeste().getDescricao())
                .build();
    }

    public static InformacaoReservaResponse buildInformacaoReservaTeste() {
        return InformacaoReservaResponse.builder()
                .idReserva(buildReservaTeste().getId())
                .solicitante(informacoesSolicitanteTeste())
                .quantidadePessoas(buildCadastrarReservaRequestTeste().getQuantidadePessoas())
                .anuncio(informacoesAnuncioTeste())
                .periodo(periodoTeste())
                .pagamento(buildPagamentoTeste())
                .build();
    }


}
