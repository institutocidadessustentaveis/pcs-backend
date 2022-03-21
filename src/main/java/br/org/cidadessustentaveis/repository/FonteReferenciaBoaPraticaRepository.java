package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.boaspraticas.FonteReferenciaBoaPratica;

@Repository
public interface FonteReferenciaBoaPraticaRepository extends JpaRepository<FonteReferenciaBoaPratica, Long>{

}
