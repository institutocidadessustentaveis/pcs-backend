package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.TipoSubdivisaoDTO;
import br.org.cidadessustentaveis.model.administracao.TipoSubdivisao;

@Repository
public interface TipoSubdivisaoRepository extends JpaRepository<TipoSubdivisao, Long>{

	@Query("SELECT ts FROM TipoSubdivisao ts WHERE ts.prefeitura.id = :idPrefeitura")
	List<TipoSubdivisaoDTO> buscarTodosPorPrefeituraId(Long idPrefeitura);

	@Query("SELECT ts FROM TipoSubdivisao ts WHERE ts.prefeitura.cidade.id = ?1 order by ts.nivel asc")
	List<TipoSubdivisao> findByPrefeituraCidadeId(Long idCidade);
}
