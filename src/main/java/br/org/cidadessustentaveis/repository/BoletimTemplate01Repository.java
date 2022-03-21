package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.noticias.BoletimTemplate01;

@Repository
public interface BoletimTemplate01Repository extends JpaRepository<BoletimTemplate01, Long>{

}
