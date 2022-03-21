package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.MaterialApoioDTO;
import br.org.cidadessustentaveis.model.institucional.Arquivo;
import br.org.cidadessustentaveis.model.planjementoIntegrado.MaterialApoio;

@Repository
public interface MaterialApoioRepository extends JpaRepository<MaterialApoio, Long>{
	
	@Query("select new br.org.cidadessustentaveis.dto.MaterialApoioDTO(ma.id, ma.titulo, ma.dataPublicacao, ma.tipoDocumento, ma.localExibicao) from MaterialApoio ma")
	List<MaterialApoioDTO> buscarMateriaisDeApoioToList();

	@Query("SELECT new br.org.cidadessustentaveis.dto.ItemComboDTO(mat.id, mat.titulo) FROM MaterialApoio mat")
	List<ItemComboDTO> buscarParaCombo();

	@Query("SELECT mat.arquivoPublicacao FROM MaterialApoio mat WHERE mat.id = :idMaterialApoio")
	Arquivo buscarArquivoPublicacao(Long idMaterialApoio);

	Page<MaterialApoio> findByInstituicaoLikeOrderByIdDesc(String string, Pageable of);

	Page<MaterialApoio> findByInstituicaoNotLikeAndIdNotInOrderByIdDesc(String string, List<Long> idsParaExcluir,
			Pageable of);

	Page<MaterialApoio> findByInstituicaoNotLikeOrderByIdDesc(String string, Pageable of );

	Page<MaterialApoio> findByInstituicaoLikeAndIdNotInOrderByIdDesc(String string, List<Long> idsParaExcluir,
			Pageable of);

}
