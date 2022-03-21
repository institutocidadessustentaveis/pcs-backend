package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.org.cidadessustentaveis.model.institucional.Arquivo;

public interface ArquivoRepository extends JpaRepository<Arquivo, Long>{

}
