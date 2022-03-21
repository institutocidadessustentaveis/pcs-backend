package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.participacaoCidada.Resposta;

@Repository
public interface RespostaRepository extends JpaRepository<Resposta, Long>{
	@Query("select r.id from Resposta r where r.pergunta is null")
	public List<Long> respostasSemPerguntas();
}
