package br.org.cidadessustentaveis.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.model.administracao.Pais;

@Repository
public interface PaisRepository extends JpaRepository<Pais, Long>{

	public Optional<Pais> findByNome(String nome);

	@Query("select pais from Pais pais where lower(pais.nome) like %:nome% order by pais.nome")
	Page<Pais> findByNomeLike(String nome, Pageable pageable);
	
	@Query("select pais from Pais pais where pais.continente like %:continente% order by pais.nome")
	public List<Pais> findByContinente(String continente);

	@Query("select new br.org.cidadessustentaveis.dto.ItemComboDTO(pais.id, pais.nome) from Pais pais order by pais.nome")
	public List<ItemComboDTO> buscarPaisesCombo();
}
