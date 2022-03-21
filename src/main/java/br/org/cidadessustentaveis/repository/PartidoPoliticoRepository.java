package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.model.administracao.PartidoPolitico;

public interface PartidoPoliticoRepository extends JpaRepository<PartidoPolitico, Long> {

	public List<PartidoPolitico> findAllByOrderBySiglaPartidoAsc();

	@Query("select new br.org.cidadessustentaveis.dto.ItemComboDTO(pp.id, pp.siglaPartido, pp.nome) from PartidoPolitico pp")
	public List<ItemComboDTO> buscarItemCombo();

	public PartidoPolitico findBySiglaPartido(String sigla);
}
