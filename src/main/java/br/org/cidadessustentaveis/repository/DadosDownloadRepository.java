
package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.DadosDownloadDTO;
import br.org.cidadessustentaveis.model.administracao.DadosDownload;

@Repository
public interface DadosDownloadRepository extends JpaRepository<DadosDownload, Long>{

	@Query("SELECT new br.org.cidadessustentaveis.dto.DadosDownloadDTO(dados) from DadosDownload dados")
	List<DadosDownloadDTO> buscarTodosDadosDownload();
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.DadosDownloadDTO(dados.id, dados.acao) from DadosDownload dados")
	List<DadosDownloadDTO> findComboBoxAcao();
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.DadosDownloadDTO(dados.pagina, dados.id ) from DadosDownload dados")
	List<DadosDownloadDTO> findComboBoxPagina();
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.DadosDownloadDTO(dados.id, dados.nomeCidade, dados.cidade.id ) from DadosDownload dados")
	List<DadosDownloadDTO> findComboBoxCidade();

	@Query("SELECT dados FROM DadosDownload dados ORDER BY dados.dataDownload DESC")
	Page<DadosDownload> buscarComPaginacao(Pageable pageable);
}
