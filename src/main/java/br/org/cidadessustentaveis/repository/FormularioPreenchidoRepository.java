package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.FormularioPreenchidoDTO;
import br.org.cidadessustentaveis.model.participacaoCidada.FormularioPreenchido;

@Repository
public interface FormularioPreenchidoRepository extends JpaRepository<FormularioPreenchido, Long>{

	@Query("select new br.org.cidadessustentaveis.dto.FormularioPreenchidoDTO(fp) from FormularioPreenchido fp where fp.formulario.id = :id")
	public List<FormularioPreenchidoDTO> findByFormularioId(Long id);
	
	@Query("select new br.org.cidadessustentaveis.dto.FormularioPreenchidoDTO(fp) from FormularioPreenchido fp where fp.usuario.id = :idUsuario AND fp.formulario.id = :idFormulario")
	public List<FormularioPreenchidoDTO> findByUsuarioAndFormularioId(Long idUsuario, Long idFormulario);
	
}
