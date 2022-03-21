package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.org.cidadessustentaveis.dto.AjusteGeralDTO;
import br.org.cidadessustentaveis.model.administracao.AjusteGeral;

@Repository
public interface AjusteGeralRepository extends JpaRepository<AjusteGeral, Long>{
	
	@Modifying
	@Transactional
	@Query("DELETE FROM AjusteGeral aj WHERE aj.localAplicacao like :localApp")
	void deleteAjustePorLocalApp(String localApp);
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.AjusteGeralDTO(a.id, a.conteudo, a.localAplicacao) FROM AjusteGeral a WHERE a.localAplicacao like :localApp")
	AjusteGeralDTO buscarAjustePorLocalApp(String localApp);
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.AjusteGeralDTO(a.id, a.conteudo, a.localAplicacao) FROM AjusteGeral a WHERE a.localAplicacao like :localApp")
	List<AjusteGeralDTO> buscarListaAjustes(String localApp);
}
