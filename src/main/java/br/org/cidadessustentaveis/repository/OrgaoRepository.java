package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.administracao.Orgao;

@Repository
public interface OrgaoRepository extends JpaRepository<Orgao, Long> {

	@Query("select new br.org.cidadessustentaveis.model.administracao.Orgao(if.id, if.nome) from Orgao if order by if.nome")
	List<Orgao> findComboBoxOrgao();

	Orgao findByNomeAndInstanciaOrgaoNome(String nomeOrgao, String nomeInstancia);
}
