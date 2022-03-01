package io.github.cwireset.tcc.repository;

import io.github.cwireset.tcc.domain.Anuncio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnuncioRepository extends JpaRepository<Anuncio, Long> {

    Page<Anuncio> findAllByAnuncianteIdAndEstaAtivoIsTrue(Long idAnunciante, Pageable pageable);

    List<Anuncio> findByImovelIdAndEstaAtivoIsTrue(Long id);

    boolean existsAnuncioByEstaAtivoIsTrueAndImovelId(Long id);

    Page<Anuncio> findAnunciosByEstaAtivoTrue(Pageable pageable);

    Optional<Anuncio> findByIdAndEstaAtivoIsTrue(Long idAnuncio);
}
