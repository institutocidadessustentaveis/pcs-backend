package br.org.cidadessustentaveis.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.boaspraticas.ImagemBoaPratica;

@Repository
public interface ImagemBoaPraticaRepository extends JpaRepository<ImagemBoaPratica, Long>{
	
	@Query("SELECT imagemBoaPratica from ImagemBoaPratica imagemBoaPratica where imagemBoaPratica.boaPratica.id = ?1 and imagemBoaPratica.tipo like ?2")
	ImagemBoaPratica buscarImagemPrincipalPorIdBoaPratica(Long idBoaPratica,String tipo);
}
