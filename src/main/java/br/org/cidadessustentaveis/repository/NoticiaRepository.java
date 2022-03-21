package br.org.cidadessustentaveis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.NoticiaItemDTO;
import br.org.cidadessustentaveis.model.noticias.Noticia;

@Repository
public interface NoticiaRepository extends JpaRepository<Noticia, Long>{

	Optional<Noticia> findByTitulo(String titulo);

	Optional<Noticia> findByTituloAndIdNotIn(String titulo, List<Long> ids);
	
	@Query("select n " +
            "from Noticia n " +
            "where n.isPublicada = true order by n.dataHoraPublicacao desc")
	List<Noticia> carregarUltimasNoticias(Pageable a);
	
	@Query("select n " +
			"from Noticia n " +
			"where n.isPublicada = true and n.isNoticiaEvento = true order by n.dataHoraPublicacao desc")
	List<Noticia> carregarIdNoticiasEventos();
	
	@Query("select n " +
            "from Noticia n " +
			"where lower(n.palavraChave) like %:chave% and " +
			"n.isPublicada = true and " +
			"n.exibirEventoTelaInicial = true " +
            "order by n.dataHoraPublicacao desc")
	List<Noticia> carregarUltimasNoticiasAgenda(String chave,Pageable a);
	
	@Query("select count(n.id) " +
            "from Noticia n where n.isPublicada = true")
	int countNoticias();

	@Query("select n " +
			"from Noticia n " +
			"where n.id in (:ids)")
	public List<Noticia> buscarNoticiasPorId(List<Long> ids);
	
	@Query("select new br.org.cidadessustentaveis.dto.NoticiaItemDTO(n) " +
			"from Noticia n " +
			"where n.id in (:ids)")
	public List<NoticiaItemDTO> buscarNoticiasItemPorId(List<Long> ids);
	
	@Query("select new br.org.cidadessustentaveis.dto.NoticiaItemDTO(n.id,n.titulo,n.subtitulo,n.autor,n.usuario,n.dataHoraCriacao, n.dataHoraPublicacao, n.palavraChave,n.url) " +
			"from Noticia n " +
			"where n.id in (:ids) and n.isPublicada = true")
	public List<NoticiaItemDTO> buscarNoticiasDTOPorListaId(List<Long> ids);

	public Noticia findByUrl(String url);
	
	@Query("select new br.org.cidadessustentaveis.dto.ItemComboDTO(n.id, n.titulo) from Noticia n order by n.id desc")
	public List<ItemComboDTO> buscarNoticiaTituloEId();
	
	@Query("select n " +
			"from Noticia n " +
			"where n.url = :url and n.isPublicada = true")
	public Noticia findByUrlPublicada(String url);
	
	@Query("select n " +
			"from Noticia n " +
			"where n.id = :id and n.isPublicada = true")
	Optional<Noticia> findByIdPublicada(Long id);

	List<Noticia> findAllByOrderByDataHoraCriacaoDesc();
	
	@Query(value = "select * " +
			"from Noticia n " +
			"where lower(n.palavra_chave) not like '%evento%' and n.publicada = true order by n.data_hora_publicacao desc limit 5", nativeQuery = true)
	public List<Noticia> buscarUltimasNoticiasPaginaInicial();
	
	@Query("select n " +
            "from Noticia n " +
            "where n.isPublicada = true and lower(n.palavraChave) not like '%evento%' order by n.id desc")
	List<Noticia> carregarNoticiaBannerPaginaInicial(Pageable a);

}
