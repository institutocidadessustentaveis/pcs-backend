package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.model.institucional.Arquivo;
import br.org.cidadessustentaveis.model.institucional.MaterialInstitucional;

@Repository
public interface MaterialInstitucionalRepository extends JpaRepository<MaterialInstitucional, Long>{

	@Query("SELECT new br.org.cidadessustentaveis.dto.ItemComboDTO(mat.id, mat.titulo) FROM MaterialInstitucional mat")
	List<ItemComboDTO> buscarParaCombo();

	@Query("SELECT arquivos FROM MaterialInstitucional mat WHERE mat.id = :idMaterialInstitucional")
	List<Arquivo> recuperarListaDeArquivos(Long idMaterialInstitucional);


	@Query("SELECT m FROM Publicacao p inner join p.materialInstitucional m WHERE p.id = :id")
	MaterialInstitucional findByPublicacaoId(Long id);

}
