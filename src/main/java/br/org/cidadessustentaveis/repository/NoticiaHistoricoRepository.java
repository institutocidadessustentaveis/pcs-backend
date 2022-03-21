package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.noticias.Noticia;
import br.org.cidadessustentaveis.model.noticias.NoticiaHistorico;

@Repository
public interface NoticiaHistoricoRepository extends JpaRepository<NoticiaHistorico, Long>{

	List<Noticia> findAllByOrderByDataHoraDesc();	
	
	@Modifying
    @Query("DELETE FROM NoticiaHistorico historico WHERE historico.noticia.id = :id")
	void deletarPorIdNoticia(Long id);

}
