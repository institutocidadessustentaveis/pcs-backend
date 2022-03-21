package br.org.cidadessustentaveis.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import br.org.cidadessustentaveis.model.administracao.ArquivoInstitucional;

public interface ArquivoInstitucionalRepository extends JpaRepository<ArquivoInstitucional, Long>{

  Set<ArquivoInstitucional> findByInstitucionalId(final Long idInstitucional);

}
