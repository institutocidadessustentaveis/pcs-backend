package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.CertificadoDTO;
import br.org.cidadessustentaveis.model.capacitacao.Certificado;

@Repository
public interface CertificadoRepository extends JpaRepository<Certificado, Long>{
	
	@Query("select new br.org.cidadessustentaveis.dto.CertificadoDTO(c.id, c.titulo, c.texto1, c.texto2, c.texto3, c.orientacaoPaisagem) from Certificado c")
	List<CertificadoDTO> buscarCertificadoToList();
	
	@Query("select new br.org.cidadessustentaveis.dto.CertificadoDTO(c.id, c.titulo) from Certificado c")
	List<CertificadoDTO> buscarCertificadoToListResumido();
}