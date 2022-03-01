package io.github.cwireset.tcc.controller;

import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.request.CadastrarAnuncioRequest;
import io.github.cwireset.tcc.service.anuncio.AnuncioService;
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
@RequestMapping("/anuncios")
public class AnuncioController {

    @Autowired
    private AnuncioService anuncioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Anuncio cadastrarAnuncio(@RequestBody @Valid CadastrarAnuncioRequest cadastrarAnuncioRequest){
        return anuncioService.cadastrarAnuncio(cadastrarAnuncioRequest);
    }

    @GetMapping
    public Page<Anuncio> listarAnuncios(@PageableDefault Pageable pageable) {
        return anuncioService.listarAnuncios(pageable);
    }

    @GetMapping(path = "/anunciantes/{idAnunciante}")
    public Page<Anuncio> listarAnunciosPorAnunciante(@PathVariable @Valid @NotNull Long idAnunciante, @PageableDefault Pageable pageable) {
        return anuncioService.listarAnunciosPorAnunciante(idAnunciante, pageable);
    }

    @DeleteMapping(path = "/{idAnuncio}")
    public void deletarAnuncio(@PathVariable @Valid @NotNull Long idAnuncio) {
        anuncioService.deletarAnuncio(idAnuncio);
    }


}
