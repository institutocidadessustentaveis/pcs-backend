package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.PlanoDeMetaHistoricoAnosDTO;
import br.org.cidadessustentaveis.model.indicadores.PlanoDeMetasDetalhadoHistorico;

@Repository
public interface PlanoDeMetasDetalhadoHistoricoRepository extends JpaRepository<PlanoDeMetasDetalhadoHistorico, Long>{
	@Query("select new br.org.cidadessustentaveis.dto.PlanoDeMetaHistoricoAnosDTO(plano.metaAnualPrimeiroAno, plano.metaAnualSegundoAno, plano.metaAnualTerceiroAno, plano.metaAnualQuartoAno, plano.indicador.id) from PlanoDeMetasDetalhadoHistorico plano"
			+ " where plano.planoDeMetasDetalhado.id = :idPlano and plano.indicador.id = :idIndicador")
	List<PlanoDeMetaHistoricoAnosDTO> buscarHistoricoAnos(Long idPlano, Long idIndicador);
}
