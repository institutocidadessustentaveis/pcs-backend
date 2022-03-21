package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.administracao.CidadeDetalhes;

@Repository	
public interface CidadeDetalhesRepository extends JpaRepository<CidadeDetalhes, Long>{
	
	public CidadeDetalhes findByCodigoIbge(Long codigoIbge);

}
