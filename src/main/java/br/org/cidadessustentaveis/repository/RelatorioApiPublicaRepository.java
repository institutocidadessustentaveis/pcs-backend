package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.sistema.RelatorioApiPublica;

@Repository
public interface RelatorioApiPublicaRepository extends JpaRepository<RelatorioApiPublica, Long>{

}
