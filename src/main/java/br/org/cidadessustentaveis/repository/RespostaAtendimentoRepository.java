package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.RespostaAtendimentoDTO;
import br.org.cidadessustentaveis.model.administracao.RespostaAtendimento;

@Repository
public interface RespostaAtendimentoRepository extends JpaRepository<RespostaAtendimento, Long>{
	
	@Query("select new br.org.cidadessustentaveis.dto.RespostaAtendimentoDTO(ra) from RespostaAtendimento ra "
			+ "where ra.formularioAtendimento.id = :id")
	RespostaAtendimentoDTO buscarRespostaAtendimentoPorIdFormulario(Long id);

}
