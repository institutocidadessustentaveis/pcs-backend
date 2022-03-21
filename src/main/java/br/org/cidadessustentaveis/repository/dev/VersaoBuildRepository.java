package br.org.cidadessustentaveis.repository.dev;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.dev.VersaoBuild;
import br.org.cidadessustentaveis.model.enums.TipoBuild;
@Repository

public interface VersaoBuildRepository extends JpaRepository<VersaoBuild, Long>{
	public VersaoBuild findTopByTipoBuildOrderByIdDesc(TipoBuild tipoBuild);
	public VersaoBuild findByTipoBuildOrderByIdDesc(TipoBuild tipoBuild);
}
