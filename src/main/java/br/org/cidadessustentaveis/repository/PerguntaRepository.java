package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.participacaoCidada.Pergunta;

@Repository
public interface PerguntaRepository extends JpaRepository<Pergunta, Long>{
	@Query("select p.id from Pergunta p where p.secao is null")
	public List<Long> perguntasSemSecao();
}
