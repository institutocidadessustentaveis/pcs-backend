package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.administracao.SubDivisao;

@Repository
public interface SubdivisaoRepository extends JpaRepository<SubDivisao, Long> {

}
