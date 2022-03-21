package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.planjementoIntegrado.TemaGeoespacial;

@Repository
public interface TemaGeoespacialRepository extends JpaRepository<TemaGeoespacial, Long>{
	
}
