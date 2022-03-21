package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.administracao.MenuPagina;
	
@Repository
public interface MenuPaginaRepository extends JpaRepository<MenuPagina, Long>{

    @Query("SELECT m " +
            "FROM " +	
            "MenuPagina m")
	List<MenuPagina> buscarTodos();
    
    void deleteByNome(String nome);

	MenuPagina findByNome(String nome);

	@Query("SELECT m FROM MenuPagina m where m.nome = :nome")
    List<MenuPagina> findByNomeLista(String nome);
}
	