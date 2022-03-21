package br.org.cidadessustentaveis.repository;

import br.org.cidadessustentaveis.model.sistema.HistoricoAcessoNoticia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoAcessoNoticiaRepository extends JpaRepository<HistoricoAcessoNoticia, Long> {

	@Modifying
    @Query("DELETE FROM HistoricoAcessoNoticia historico WHERE historico.noticia.id = :id")
	void deletarPorIdNoticia(Long id);
}
