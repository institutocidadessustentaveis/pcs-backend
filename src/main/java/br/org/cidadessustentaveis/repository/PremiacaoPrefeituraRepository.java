package br.org.cidadessustentaveis.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.administracao.PremiacaoPrefeitura;

@Repository
public interface PremiacaoPrefeituraRepository extends JpaRepository<PremiacaoPrefeitura, Long>{
	
	List<PremiacaoPrefeitura> findByPrefeituraIdAndPremiacaoId(Long idPrefeitura,Long idPremiacao);
	
	// Arrumar a consulta abaixo
	@Query("SELECT p from PremiacaoPrefeitura p where p.premiacao.id = :idPremiacao")
	List<PremiacaoPrefeitura> buscarCidadesInscritas(Long idPremiacao);
		
	
}
