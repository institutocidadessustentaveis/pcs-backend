package br.org.cidadessustentaveis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.BibliotecaDTO;
import br.org.cidadessustentaveis.model.biblioteca.Biblioteca;
import br.org.cidadessustentaveis.model.institucional.Arquivo;

@Repository
public interface BibliotecaRepository extends JpaRepository<Biblioteca, Long>{

	@Query("select new br.org.cidadessustentaveis.dto.BibliotecaDTO(b.id, b.tituloPublicacao, b.subtitulo, b.dataPublicacao, b.autor) from Biblioteca b"
			+ " where b.usuario.id = :idUsuario"
			+ " order by b.dataPublicacao asc")
	List<BibliotecaDTO> buscarBibliotecasToList(Long idUsuario);
	
	@Query("select new br.org.cidadessustentaveis.dto.BibliotecaDTO(b.id, b.tituloPublicacao, b.subtitulo, b.dataPublicacao, b.autor) from Biblioteca b"
			+ " order by b.dataPublicacao asc")
	List<BibliotecaDTO> buscarBibliotecasToListAdmin();
	
	@Query("select new br.org.cidadessustentaveis.dto.BibliotecaDTO(b.id, b.tituloPublicacao) from Biblioteca b"
			+ " order by b.dataPublicacao asc")
	List<BibliotecaDTO> buscarBibliotecasParaComboBox();
	
	
	@Query("select b " +
			"from Biblioteca b " +
			"order by b.id desc")
	List<Biblioteca> carregarIdBibliotecas();

	@Query("SELECT b.arquivoMultimidia FROM Biblioteca b WHERE b.id = :idBiblioteca")
	List<Arquivo> buscarArquivoPublicacao(Long idBiblioteca);
	
	@Query("select b from Biblioteca b where b.id in ?1")
	List<Biblioteca> buscarBibliotecasPorIds(List<Long> ids);
	
	@Query("select b from Biblioteca b where b.id = :idBiblioteca and b.usuario.id = :idUsuario")
	Optional<Biblioteca> buscarBibliotecaPorIdEUsuario(Long idBiblioteca, Long idUsuario);
}