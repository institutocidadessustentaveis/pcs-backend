package br.org.cidadessustentaveis.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.indicadores.ValorReferencia;
import br.org.cidadessustentaveis.model.indicadores.Variavel;

@Repository
public interface ValorReferenciaRepository extends JpaRepository<ValorReferencia, Long> {
	
	@Query("SELECT vr FROM ValorReferencia vr WHERE vr.variavel = ?1 AND ?2 BETWEEN vr.valorde AND vr.valorate")
	Optional<ValorReferencia> findValorReferenciaByVariavelAndValor(Variavel variavel, Double valor);

}
