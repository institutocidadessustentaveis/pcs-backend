package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.HistoricoCertificadoDTO;
import br.org.cidadessustentaveis.model.capacitacao.HistoricoCertificado;

@Repository
public interface HistoricoCertificadoRepository extends JpaRepository<HistoricoCertificado, Long>{
	
	@Query("select new br.org.cidadessustentaveis.dto.HistoricoCertificadoDTO(hc) from HistoricoCertificado hc")
	List<HistoricoCertificadoDTO> buscarHistoricoCertificadoToList();
}