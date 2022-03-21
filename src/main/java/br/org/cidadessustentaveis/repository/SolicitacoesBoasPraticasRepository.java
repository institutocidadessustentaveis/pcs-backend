package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.SolicitacoesBoasPraticasDTO;
import br.org.cidadessustentaveis.model.administracao.SolicitacoesBoasPraticas;

@Repository
public interface SolicitacoesBoasPraticasRepository  extends JpaRepositoryImplementation<SolicitacoesBoasPraticas, Long> {
	
	@Query("select new br.org.cidadessustentaveis.dto.SolicitacoesBoasPraticasDTO(s.id, s.solicitacao, s.nomeUsuario, s.dataPublicacao, s.horarioPublicacao, cid) from SolicitacoesBoasPraticas s"
			+ " left join s.cidade cid")
	List<SolicitacoesBoasPraticasDTO> BuscarSolicitacoesBoasPraticasToList();
}
