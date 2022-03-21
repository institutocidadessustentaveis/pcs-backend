package br.org.cidadessustentaveis.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;

public interface MetaObjetivoDesenvolvimentoSustentavelRepository extends JpaRepository<MetaObjetivoDesenvolvimentoSustentavel, Long>{

  Set<MetaObjetivoDesenvolvimentoSustentavel> findByOdsId(final Long idOds);
  
  @Query("select new br.org.cidadessustentaveis.dto.ItemComboDTO(meta.id, meta.numero, meta.descricao) from MetaObjetivoDesenvolvimentoSustentavel meta order by meta.numero ASC")
  List<ItemComboDTO> buscarMetasParaCombo();
  
  @Query("SELECT metaOds FROM MetaObjetivoDesenvolvimentoSustentavel metaOds WHERE metaOds.id IN ?1")
  List <MetaObjetivoDesenvolvimentoSustentavel> buscarPorIds(List<Long> ids);
  
  @Query("SELECT new br.org.cidadessustentaveis.dto.ItemComboDTO(meta.id, meta.numero, meta.descricao) FROM MetaObjetivoDesenvolvimentoSustentavel meta WHERE meta.ods.id IN ?1 order by meta.numero ASC")
  List <ItemComboDTO> buscarMetaOdsPorIdOdsItemCombo(List<Long> ids);
  

}
