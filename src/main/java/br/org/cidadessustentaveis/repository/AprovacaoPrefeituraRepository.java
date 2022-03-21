package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.administracao.AprovacaoPrefeitura;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;

@Repository
public interface AprovacaoPrefeituraRepository extends JpaRepository<AprovacaoPrefeitura, Long>{

	public List<AprovacaoPrefeitura> findByPrefeituraCidadeAndStatus(Cidade cidade, String string);
	public AprovacaoPrefeitura findByPrefeituraAndStatus(Prefeitura prefeitura, String string);
	
	public List<AprovacaoPrefeitura> findAllByOrderByDataDesc();
	
	@Query(value = "select ap from AprovacaoPrefeitura ap"
		 + " inner join ap.prefeitura p"
		 + " inner join p.cidade c"
		 + " where lower(c.nome) like lower(concat('%', ?1,'%'))"
		 + " order by ap.data desc")
	public List<AprovacaoPrefeitura> findByNomeByOrderByDataAsc(String nome);
}
