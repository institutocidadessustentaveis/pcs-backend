package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.sistema.ParametroGeral;

@Repository
public interface ParametroGeralRepository extends JpaRepository<ParametroGeral, Long>{
	@Query(value="select new ParametroGeral(p.id, p.emailSugestaoBoaPratica) from ParametroGeral p where p.emailSugestaoBoaPratica != null")
	ParametroGeral findEmailSugestaoBoaPratica();
}
