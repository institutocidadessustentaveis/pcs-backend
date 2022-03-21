package br.org.cidadessustentaveis.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.UsuarioDTO;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	/*
	 * CAROS AMIGXS PARA EVITAR QUALQUER TIPO DE DUPLICAÇÃO DE EMAILS, NÃO SE ESQUEÇA 
	 * AO CRIAR UMA QUERY DE USUARIOS, NÃO BUSCAR AQUELES COM A PROPRIEDADE CREDENCIAL.SNEXCLUIDO = TRUE
	 * FORTE ABRAÇO 
	 * */
	@Query(value = "select * from usuario usr\r\n" + 
	    "left join credencial crd on crd.id = usr.credencial\r\n" + 
		"left join credencial_perfil crp on crp.id_credencial = crd.id\r\n" + 
		"where crp.id_perfil = :id", nativeQuery = true)
	public List<Usuario> findByCredencialPerfilId(Long id);
	
	@Query("SELECT u.prefeitura FROM Usuario u LEFT JOIN u.credencial.listaPerfil lp "
			+ "WHERE lp.gestaoPublica = true and u.email = ?1")
	public Optional<Prefeitura> findPrefeituraByEmailGestor(String email);

	@Query("SELECT DISTINCT u.email FROM Usuario u inner join u.areasInteresse ai "
			+ "WHERE ai in ?1 and u.recebeEmail = true")
	public List<String> findByAreasInteresseIn(List<AreaInteresse> listaAreaInteresse);
	
	@Query("SELECT DISTINCT u.email FROM Usuario u WHERE u.cidadeInteresse = :idCidade and u.recebeEmail = true")
	public List<String> listarEmailUsuarioComCidadeInteresseId(String idCidade);

	@Query("SELECT DISTINCT u.email FROM Usuario u inner join u.areasInteresse ai "
			+ "WHERE ai.id in ?1 and u.recebeEmail = true")
	public List<String> findByAreasInteresseIdIn(List<Long> listaAreaInteresse);
	
	@Query("SELECT DISTINCT u.email FROM Usuario u WHERE u.recebeEmail = true")
	public List<String> listarEmailUsuarioRecebeEmailTrue();
	
	
	@Query("select new br.org.cidadessustentaveis.model.administracao.Usuario(u.id, u.nome) from Usuario u order by u.nome")
	List<Usuario> findComboBoxUsuario();

	public Page<Usuario> findAllByCredencialSnExcluido(Boolean snExcluido, Pageable pageRequests);

	public Page<Usuario> findAllByCredencialSnExcluidoAndPrefeituraIsNotNull(Boolean snExcluido, Pageable pageRequests);


	public Page<Usuario> findByCredencialSnExcluidoAndNomeContainingIgnoreCaseAndPrefeituraCidadeProvinciaEstadoSiglaContainingIgnoreCaseAndPrefeituraCidadeNomeContainingIgnoreCaseAndPrefeituraIsNotNull(
			boolean b, String nome, String uf, String cidade, Pageable pageable);

	public Page<Usuario> findByCredencialSnExcluidoAndNomeContainingIgnoreCaseAndPrefeituraCidadeProvinciaEstadoSiglaContainingIgnoreCaseAndPrefeituraCidadeNomeContainingIgnoreCaseAndCredencialListaPerfilIdInAndPrefeituraIsNotNull(
			boolean b, String nome, String uf, String cidade, List<Long> perfil, Pageable pageable);

	@Query("select new br.org.cidadessustentaveis.dto.ItemComboDTO(u.id, u.nome) from Usuario u where u.prefeitura is null order by u.nome")
	List<ItemComboDTO> buscarComboBoxUsuarioSemPrefeitura();
	
	@Query("select new br.org.cidadessustentaveis.dto.ItemComboDTO(u.id, u.nome) from Usuario u where u.prefeitura is not null order by u.nome")
	List<ItemComboDTO> buscarComboBoxUsuarioDePrefeitura();
	
	@Query("select distinct u.email from Usuario u join u.areasInteresse ai where u.prefeitura is not null order by u.nome")
	List<String> findUsuarioEmailByAreaInteresseId(List<Long> idAreaInteresse);
	
	@Transactional
	@Modifying
	@Query(	"update Usuario u set u.bloqueadoForum = :result" +
		    " where u.id = :idUsuario")
	 void updateBloqueadoForum(Long idUsuario, boolean result);
	
	@Query("select u from Usuario u where u.recebeEmail = true")
	List<UsuarioDTO> findUsuariosRecebeEmailTrue();
	
	@Query("select u from Usuario u join u.credencial cr where u.recebeEmail = true and cr.snExcluido = false")
	List<UsuarioDTO> findUsuariosRecebeEmailTrueECredencialNaoExcluida();

	@Query("select new br.org.cidadessustentaveis.model.administracao.Usuario(u) from Usuario u inner join u.prefeitura p where p.id = :id")
	public List<Usuario> findByPrefeituraId(Long id);
	
	@Query("select new br.org.cidadessustentaveis.dto.UsuarioDTO(usr.id, usr.email, usr.nome) from Usuario usr " + 
		    "inner join usr.credencial crd " + 
			"inner join crd.listaPerfil crp " + 
			"where crd.snExcluido = false and crp.id = :idPerfil")
		public List<UsuarioDTO> buscarPorPerfil(Long idPerfil);
	
	@Query("select new br.org.cidadessustentaveis.dto.UsuarioDTO(u.id) from Usuario u join u.credencial cr where u.prefeitura.id = :id and cr.snExcluido = false")
	List<UsuarioDTO> countUsuariosPrefeitura(Long id);
	
	@Query("select new br.org.cidadessustentaveis.dto.UsuarioDTO(u) from Usuario u JOIN u.credencial cr WHERE u.prefeitura.id = :idPrefeitura and cr.snExcluido = false")
	List<UsuarioDTO> buscarListaUsuariosPorPrefeitura(Long idPrefeitura);
	
}

