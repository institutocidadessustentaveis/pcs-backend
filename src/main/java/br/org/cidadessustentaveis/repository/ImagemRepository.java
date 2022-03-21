package br.org.cidadessustentaveis.repository;

import br.org.cidadessustentaveis.model.boaspraticas.ImagemBoaPratica;
import br.org.cidadessustentaveis.model.noticias.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagemRepository extends JpaRepository<Imagem, Long> {

}