package br.org.cidadessustentaveis.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.boaspraticas.SugestaoBoasPraticas;

@Repository
public interface SugestaoBoasPraticasRepository extends JpaRepository<SugestaoBoasPraticas, Long> {
	
	@Query("select new SugestaoBoasPraticas(s.id, s.titulo, s.descricao, u.nome) "
			+ "from SugestaoBoasPraticas s "
			+ "inner join s.usuario u "
			+ "order by s.id desc ")
	Page<SugestaoBoasPraticas> findAllSugestaoBoasPraticas(PageRequest pageRequest);
}
	
