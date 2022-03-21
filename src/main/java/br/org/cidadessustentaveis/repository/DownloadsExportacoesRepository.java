package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.administracao.DownloadsExportacoes;

@Repository
public interface DownloadsExportacoesRepository extends JpaRepository<DownloadsExportacoes, Long>{
	
	@Query(value="select distinct on (nomeArquivo) id, nomeArquivo, dataHora, nomeUsuario from downloads_exportacoes order by nomeArquivo", nativeQuery=true)
	List<DownloadsExportacoes> findComboBoxArquivo();
}
