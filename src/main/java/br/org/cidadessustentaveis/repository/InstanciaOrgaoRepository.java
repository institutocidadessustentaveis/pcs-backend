package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.administracao.InstanciaOrgao;

@Repository
public interface InstanciaOrgaoRepository extends JpaRepository<InstanciaOrgao, Long> {

	@Query("select new br.org.cidadessustentaveis.model.administracao.InstanciaOrgao(if.id, if.nome) from InstanciaOrgao if order by if.nome")
	List<InstanciaOrgao> findComboBoxInstanciaOrgao();

	InstanciaOrgao findByNome(String nomeInstanciaOrgao);
}
