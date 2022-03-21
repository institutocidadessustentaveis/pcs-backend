package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.ObjetivoDesenvolvimentoSustentavelDTO;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;

public interface ObjetivoDesenvolvimentoSustentavelRepository extends JpaRepository<ObjetivoDesenvolvimentoSustentavel, Long> {

	List<ObjetivoDesenvolvimentoSustentavel> findAllByOrderByNumeroAsc();
	
	@Query("select new br.org.cidadessustentaveis.dto.ItemComboDTO(ods.id, ods.numero || ' - ' || ods.titulo) from Eixo e inner join e.listaODS ods where e.id = :id")
	List<ItemComboDTO> buscarOdsPorIdEixo(Long id);

	@Query("SELECT new br.org.cidadessustentaveis.dto.ItemComboDTO(ods.id, ods.numero, ods.titulo) FROM ObjetivoDesenvolvimentoSustentavel ods ORDER BY ods.numero ASC")
	List<ItemComboDTO> buscarOdsParaCombo();

	@Query("SELECT new br.org.cidadessustentaveis.dto.ObjetivoDesenvolvimentoSustentavelDTO(ods) FROM ObjetivoDesenvolvimentoSustentavel ods WHERE ods.id IN ?1")
	List<ObjetivoDesenvolvimentoSustentavelDTO> buscarPorListaIds(List<Long> listaIds);
	
	@Query("SELECT ods FROM ObjetivoDesenvolvimentoSustentavel ods WHERE ods.id IN ?1")
	List<ObjetivoDesenvolvimentoSustentavel> buscarPorIds(List<Long> listaIds);
}
