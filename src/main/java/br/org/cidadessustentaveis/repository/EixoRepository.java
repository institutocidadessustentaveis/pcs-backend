package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.EixoDTO;
import br.org.cidadessustentaveis.dto.EixoListagemDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.model.administracao.Eixo;

@Repository
public interface EixoRepository extends JpaRepository<Eixo, Long>{

	List<Eixo> findAllByOrderByNomeAsc();
	
	@Query("select new br.org.cidadessustentaveis.dto.ItemComboDTO(eixo.id, eixo.nome) from Eixo eixo order by eixo.nome")
	List<ItemComboDTO> buscarEixosParaComboBox();
	
	@Query("select new br.org.cidadessustentaveis.dto.EixoDTO(eixo.id, eixo.nome, eixo.link) from Eixo eixo order by eixo.nome")
	List<EixoDTO> buscarEixosDto();
	
	@Query("select eixo from Eixo eixo where eixo.id in ?1")
	List<Eixo> buscarEixosPorIds(List<Long> ids);
	
	@Query("select new br.org.cidadessustentaveis.dto.EixoListagemDTO(eixo.id, eixo.nome, eixo.icone) from Eixo eixo order by eixo.nome")
	List<EixoListagemDTO> buscarEixosList();

}
