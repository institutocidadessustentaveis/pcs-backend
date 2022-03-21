package br.org.cidadessustentaveis.repository;

import br.org.cidadessustentaveis.model.planjementoIntegrado.HistoricoUsoShape;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoUsoShapeRepository extends JpaRepository<HistoricoUsoShape, Long> {

}
