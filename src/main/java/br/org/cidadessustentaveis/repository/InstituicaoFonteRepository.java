package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.administracao.InstituicaoFonte;

@Repository
public interface InstituicaoFonteRepository extends JpaRepository<InstituicaoFonte, Long> {

	@Query("select new br.org.cidadessustentaveis.model.administracao.InstituicaoFonte(if.id, if.nome) from InstituicaoFonte if  left join if.cidade c where c is null or c.id =:idCidade order by if.nome")
	List<InstituicaoFonte> findComboBoxInstituicaoFonte(Long idCidade);
	@Query("select if from InstituicaoFonte if  left join if.cidade c where if.nome = :nome and (c is null or c.id =:idCidade ) order by if.nome")
	List<InstituicaoFonte> findByNomeAndCidadeId(String nome, Long idCidade);
}
