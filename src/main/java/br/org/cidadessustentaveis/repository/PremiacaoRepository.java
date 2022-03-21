package br.org.cidadessustentaveis.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.administracao.Premiacao;
import br.org.cidadessustentaveis.model.administracao.PremiacaoPrefeitura;
import br.org.cidadessustentaveis.model.enums.StatusPremiacao;
	
@Repository
public interface PremiacaoRepository extends JpaRepository<Premiacao, Long>{

	@Query("select premiacao from Premiacao premiacao where lower(premiacao.descricao) like %:descricao% order by premiacao.descricao")
	List<Premiacao> findByDescricaoLike(String descricao);
	
	@Query("select pp.premiacao from PremiacaoPrefeitura pp where pp.prefeitura.id = ?1 ")
	List<Premiacao> buscarPremiacoesPorPrefeitura(Long idPrefeitura);
	
	@Query("select pp from PremiacaoPrefeitura pp where pp.prefeitura.id = ?1 and pp.premiacao.status = ?2")
	List<PremiacaoPrefeitura> buscarPremiacoesEmAvaliacaoPorPrefeitura(Long idPrefeitura , StatusPremiacao statusPremiacao);
	
	@Query("select p from Premiacao p where p.status = ?1")
	List<Premiacao> buscarPremiacoesEmInscricoesAbertas(StatusPremiacao statusPremiacao);
}
	