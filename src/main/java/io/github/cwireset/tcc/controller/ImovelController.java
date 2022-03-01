package io.github.cwireset.tcc.controller;

import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.request.CadastrarImovelRequest;
import io.github.cwireset.tcc.service.imovel.ImovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/imoveis")
public class ImovelController {

    @Autowired
    ImovelService imovelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Imovel cadastrarImovel(@RequestBody @Valid CadastrarImovelRequest cadastrarImovelRequest) {
        return imovelService.cadastrarImovel(cadastrarImovelRequest);
    }

    @GetMapping
    public Page<Imovel> listarImoveis(@PageableDefault Pageable pageable) {
        return imovelService.listarImoveis(pageable);
    }

    @GetMapping(path = "/proprietarios/{idProprietario}")
    public Page<Imovel> listarPorIdProprietario(@PathVariable @Valid @NotNull Long idProprietario, @PageableDefault Pageable pageable) {
        return imovelService.listarImoveisPorIdProprietario(idProprietario, pageable);
    }

    @GetMapping(path = "/{idImovel}")
    public Imovel listarImovelPorId(@PathVariable Long idImovel) {
        return imovelService.listarImoveisPorId(idImovel);
    }

    @DeleteMapping(path = "/{idImovel}")
    public void deletarImovel(@PathVariable Long idImovel) {
        imovelService.deletarImovel(idImovel);
    }
}
