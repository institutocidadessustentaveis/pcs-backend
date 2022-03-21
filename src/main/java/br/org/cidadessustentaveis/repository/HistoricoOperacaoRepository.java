package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.sistema.HistoricoOperacao;

@Repository
public interface HistoricoOperacaoRepository extends JpaRepository<HistoricoOperacao, Long> {

    @Query("select h from HistoricoOperacao h")
    List<HistoricoOperacao> carregarHistoricoOperacao(Pageable pageable);

}
