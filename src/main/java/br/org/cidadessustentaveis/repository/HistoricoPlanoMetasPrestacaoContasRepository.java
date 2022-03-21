package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.administracao.HistoricoPlanoMetasPrestacaoContas;

@Repository
public interface HistoricoPlanoMetasPrestacaoContasRepository extends JpaRepository<HistoricoPlanoMetasPrestacaoContas, Long> {
	
//	@Query("select new HistoricoPlanoMetasPrestacaoContas(h.id, h.planoMetas, h.prestacaoContas, h.dataHoraPlanoMetas, h.dataHoraPrestacaoContas, mandato, p.id) " +
//		    "from HistoricoPlanoMetasPrestacaoContas h " +
//			"inner join h.prefeitura p " +
//		    "where p.id = :idPrefeitura")
//		    HistoricoPlanoMetasPrestacaoContas buscaHistoricoPlanoMetasPrestacaoContasPorIdPrefeitura(Long idPrefeitura);
	
	
	@Query("select h from HistoricoPlanoMetasPrestacaoContas h inner join h.prefeitura p where p.id = :idPrefeitura")
		    HistoricoPlanoMetasPrestacaoContas buscaHistoricoPlanoMetasPrestacaoContasPorIdPrefeitura(Long idPrefeitura);
}
