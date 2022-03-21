package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoImagem;

@Repository
public interface InstitucionalDinamicoImagemRepository extends JpaRepository<InstitucionalDinamicoImagem, Long> {	

}