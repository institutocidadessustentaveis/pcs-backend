package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoPublicacao;
	
@Repository
public interface InstitucionalDinamicoPublicacaoRepository extends JpaRepository<InstitucionalDinamicoPublicacao, Long>{	

}
