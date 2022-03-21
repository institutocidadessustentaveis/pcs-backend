package br.org.cidadessustentaveis.repository;

import br.org.cidadessustentaveis.model.planjementoIntegrado.HistoricoShape;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface HistoricoShapeRepository extends JpaRepository<HistoricoShape, Long> {

    @Query("select h from HistoricoShape h order by h.dataCriacao desc")
    List<HistoricoShape> buscarHistorico(Pageable pageable);

}
