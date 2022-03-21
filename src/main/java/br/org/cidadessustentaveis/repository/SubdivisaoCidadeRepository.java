package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.org.cidadessustentaveis.dto.SubdivisaoDTO;
import br.org.cidadessustentaveis.model.administracao.SubdivisaoCidade;

public interface SubdivisaoCidadeRepository extends JpaRepository<SubdivisaoCidade,Long>{

	@Query("SELECT new br.org.cidadessustentaveis.dto.SubdivisaoDTO(s.id, s.tipoSubdivisao.prefeitura.cidade.id, s.nome, s) FROM SubdivisaoCidade s WHERE s.cidade.id = :idCidade order by s.nome")
	List<SubdivisaoDTO> buscarTodosPorCidadeId(Long idCidade);
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.SubdivisaoDTO(s.id, s.tipoSubdivisao.prefeitura.cidade.id, s.nome, s) FROM SubdivisaoCidade s WHERE s.cidade.id = :idCidade order by s.tipoSubdivisao.nivel, s.nome ")
	List<SubdivisaoDTO> buscarTodosPorCidadeIdOrderByNivelAndSubdivisaoNome(Long idCidade);
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.SubdivisaoDTO(s.id, s.nome) FROM SubdivisaoCidade s WHERE s.subdivisaoPai.id= :idSubdivisao")
	List<SubdivisaoDTO> buscarTodasSubdivisaoRelacionadasComSubdivisaoPai(Long idSubdivisao);

	List<SubdivisaoCidade> findByCidadeIdAndTipoSubdivisaoNivel(Long idCidade, Long nivelTipo);

	List<SubdivisaoCidade> findBySubdivisaoPaiId(Long id);
	@Query("SELECT s from SubdivisaoCidade s WHERE slugify(unaccent(s.cidade.provinciaEstado.sigla)) = ?1 AND slugify(unaccent(s.cidade.nome)) = ?2 AND slugify(unaccent(s.nome)) = ?3")
	SubdivisaoCidade findByUfCidadeSubdivisao(String uf, String cidade, String subdivisao);
	
	@Query("SELECT s from SubdivisaoCidade s WHERE ?1 like CONCAT(s.tipoSubdivisao.nome,' - ',s.nome)")
	SubdivisaoCidade findBySheetName(String sheetName);
   
}
