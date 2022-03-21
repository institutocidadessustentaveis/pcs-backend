package br.org.cidadessustentaveis.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.FormularioResumidoDTO;
import br.org.cidadessustentaveis.model.participacaoCidada.Formulario;

@Repository
public interface FormularioRepository extends JpaRepository<Formulario, Long>{
	@Query("select f from Formulario f WHERE f.usuarioCriador.prefeitura.cidade.id = ?1")
	List<Formulario> findByCidadeId(Long id);

	Formulario findByLink(String link);
	
	@Query("select f.link from Formulario f WHERE f.link like :link")
	String findByLinkLike(String link);
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.FormularioResumidoDTO(f.id, f.nome, f.descricao, f.link) FROM Formulario f "
			+ "WHERE f.usuarioCriador.prefeitura.cidade.id = :idCidade and f.exibirPaginaPrefeitura IS true "
			+ "and f.inicioPeriodoAtividade <= :dataAtual and f.fimPeriodoAtividade >= :dataAtual")
	List<FormularioResumidoDTO> buscarFormulariosResumido(LocalDate dataAtual, Long idCidade);

	List<Formulario> findAllByOrderByDataCriacaoDesc();

}
