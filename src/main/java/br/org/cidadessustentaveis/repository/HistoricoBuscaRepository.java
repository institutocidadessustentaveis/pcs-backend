package br.org.cidadessustentaveis.repository;

import br.org.cidadessustentaveis.model.sistema.HistoricoBusca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoBuscaRepository extends JpaRepository<HistoricoBusca, Long> {

}
