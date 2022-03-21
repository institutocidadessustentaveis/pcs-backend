package br.org.cidadessustentaveis.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.administracao.CartaCompromisso;
@Repository
public interface CartaCompromissoRepository extends JpaRepository<CartaCompromisso, Long> {
	@Modifying
    @Query("DELETE FROM CartaCompromisso c WHERE c.id = ?1")
    @Transactional
    void delete(Long id);
	
	@Modifying
    @Query("DELETE FROM CartaCompromisso c WHERE c.prefeitura.id = ?1")
    @Transactional
    void deletarPorPrefeitura(Long id);
}
