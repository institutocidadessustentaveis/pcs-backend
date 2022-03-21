package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.administracao.AreaAtuacao;

@Repository
public interface AreaAtuacaoRepository extends JpaRepository<AreaAtuacao, Long>{

}
