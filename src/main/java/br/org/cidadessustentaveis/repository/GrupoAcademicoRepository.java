package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.GrupoAcademicoCardDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoComboDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoDetalheDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoPainelDTO;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.biblioteca.Biblioteca;
import br.org.cidadessustentaveis.model.contribuicoesAcademicas.GrupoAcademico;

@Repository
public interface GrupoAcademicoRepository extends JpaRepositoryImplementation<GrupoAcademico, Long>{

	@Query("select new br.org.cidadessustentaveis.dto.GrupoAcademicoDTO(ga.id, ga.nomeGrupo, ga, ga.tipo)"
			+ " from GrupoAcademico ga"
			+ " order by ga.nomeGrupo asc")
	List<GrupoAcademicoDTO> buscarGruposAcademicosToListAdmin();
	
	@Query("select new br.org.cidadessustentaveis.dto.GrupoAcademicoDTO(ga.id, ga.nomeGrupo, ga, ga.tipo)"
			+ " from GrupoAcademico ga"
			+ " where ga.usuario.id = :idUsuario"
			+ " order by ga.nomeGrupo asc")
	List<GrupoAcademicoDTO> buscarGruposAcademicosToListByUsuarioId(Long idUsuario);
	
	@Query("select new br.org.cidadessustentaveis.dto.GrupoAcademicoCardDTO(ga.id, ga.nomeGrupo) from GrupoAcademico ga where ga.id = :id")
	GrupoAcademicoCardDTO buscarGrupoAcademicoPorIdCard(Long id);
	
	@Query("select new br.org.cidadessustentaveis.dto.GrupoAcademicoPainelDTO(ga.id, ga.nomeGrupo, ga.pais.nome, ga.estado.nome, ga.cidade.nome, ga.tipo, ga.paginaOnline, ga.nomeContato, ga.emailContato, ga.telefoneContato, ga.emailInstitucional, ga.telefoneInstitucional, ga.linkBaseDados, ga.observacoes, ga.descricaoInstituicao, ga.experienciasDesenvolvidas, ga.logradouro, ga.numero, ga.complemento, ga.quantidadeAlunos, ga.nomeAcademia, ga.nomeApl, ga.descricaoApl) from GrupoAcademico ga where ga.id = :id")
	GrupoAcademicoPainelDTO buscarGrupoAcademicoPorIdPainel(Long id);
	
	@Query("select new br.org.cidadessustentaveis.dto.GrupoAcademicoDTO(ga.id, ga.nomeGrupo, ga.latitude, ga.longitude) from GrupoAcademico ga")
	List<GrupoAcademicoDTO> buscarGruposAcademicosMapa();
	
	@Query("SELECT " +
            "    ga.areasInteresse " +
            "FROM " +
            "    GrupoAcademico ga "
            + "where ga.id = :idGrupoAcademico")
	List<AreaInteresse> findAreaInteresseById(Long idGrupoAcademico);
	
	@Query("SELECT " +
            "    ga.eixos " +
            "FROM " +
            "    GrupoAcademico ga "
            + "where ga.id = :idGrupoAcademico")
	List<Eixo> findEixoById(Long idGrupoAcademico);
	
	@Query("SELECT " +
            "    ga.ods " +
            "FROM " +
            "    GrupoAcademico ga "
            + "where ga.id = :idGrupoAcademico")
	List<ObjetivoDesenvolvimentoSustentavel> findODSById(Long idGrupoAcademico);
	
	@Query("SELECT " +
            "    ga.bibliotecas " +
            "FROM " +
            "    GrupoAcademico ga "
            + "where ga.id = :idGrupoAcademico")
	List<Biblioteca> findBibliotecaById(Long idGrupoAcademico);
	
	@Query("select new br.org.cidadessustentaveis.dto.GrupoAcademicoComboDTO(ga.id, ga.nomeGrupo) from GrupoAcademico ga where ga.tipoCadastro = 'Grupo Acadêmico'")
	List<GrupoAcademicoComboDTO> buscarComboGrupoAcademico();
	
	@Query("select distinct new br.org.cidadessustentaveis.dto.GrupoAcademicoDetalheDTO(ga) from GrupoAcademico ga where ga.id = :id")
	GrupoAcademicoDetalheDTO buscarGrupoAcademicoPorIdDetalhesDTO(Long id);
	
	@Query("SELECT ga.ods FROM GrupoAcademico ga where ga.id = :id")
	List<ObjetivoDesenvolvimentoSustentavel> buscarOdsDoGrupoAcademicoPorId(Long id);
	
	@Query("select new br.org.cidadessustentaveis.dto.GrupoAcademicoDTO(ga.id, ga.nomeGrupo, ga, ga.tipo) from GrupoAcademico ga"
			+ " where lower(ga.nomeGrupo) like lower(concat('%', ?1,'%')) "
			+ " order by ga.nomeGrupo asc")
	List<GrupoAcademicoDTO> findByNomeGrupo(String nomeGrupo);
}
