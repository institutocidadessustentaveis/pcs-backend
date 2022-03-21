package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.PrimeiraSecaoDTO;
import br.org.cidadessustentaveis.model.home.PrimeiraSecao;
	
@Repository
public interface PrimeiraSecaoRepository extends JpaRepository<PrimeiraSecao, Long>{
	
	@Query("select h " +	
            "from PrimeiraSecao h " +	
            "WHERE h.home.id = ?1 and h.exibir = true order by h.indice")
	List<PrimeiraSecao> findByIdHome(Long id);	
	
	
	@Query("select new br.org.cidadessustentaveis.dto.PrimeiraSecaoDTO(p.id, p.indice, p.primeiroTitulo, p.exibir, p.tipo) " +	
            "from PrimeiraSecao p " +	
            "WHERE p.home.id = ?1 order by p.indice")
	List<PrimeiraSecaoDTO> buscarListaPrimeiraSecaoResumidaPorId(Long id);	
	
	@Query("select new br.org.cidadessustentaveis.dto.PrimeiraSecaoDTO(p) " +	
            "from PrimeiraSecao p " +	
            "WHERE p.id = ?1")
	PrimeiraSecaoDTO buscarPrimeiraSecaoDetalhe(Long id);	
	
}
