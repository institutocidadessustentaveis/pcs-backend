package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.noticias.InformacaoLivre;

@Repository
public interface InformacaoLivreRepository extends JpaRepository<InformacaoLivre, Long>{

}
