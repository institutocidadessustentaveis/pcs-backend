package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.org.cidadessustentaveis.dto.FormularioAtendimentoDTO;
import br.org.cidadessustentaveis.model.administracao.FormularioAtendimento;

@Repository
public interface FormularioAtendimentoRepository extends JpaRepository<FormularioAtendimento, Long> {
	
	@Transactional
	@Modifying
	@Query("update FormularioAtendimento f set f.respondido = true "
			+ "where f.id = :id")
	void changeFormularioParaRespondido(Long id);
	
	@Query("select new br.org.cidadessustentaveis.dto.FormularioAtendimentoDTO(f) from FormularioAtendimento f "
			+ "order by f.dataHora desc")
	List<FormularioAtendimentoDTO> buscarTodos();

}
