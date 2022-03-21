package br.org.cidadessustentaveis.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.HomeImagemDTO;
import br.org.cidadessustentaveis.model.home.HomeImagem;	
	
@Repository
public interface HomeImagemRepository extends JpaRepository<HomeImagem, Long>{
		
	@Query("select h " +	
            "from HomeImagem h " +	
            "WHERE h.home.id = ?1 order by h.indice")
	List<HomeImagem> findAllByIdHome(Long id);
	
	@Query("select new br.org.cidadessustentaveis.dto.HomeImagemDTO(h.id, h.tipo, h.extensao, h.nomeAutor, h.titulo, h.subtitulo, h.link, h.indice, h.exibirBusca) " +	
            "from HomeImagem h " +	
            "WHERE h.home.id = ?1 order by h.indice")
	List<HomeImagemDTO> buscarTodasSemConteudoPorIdHome(Long id);
}
