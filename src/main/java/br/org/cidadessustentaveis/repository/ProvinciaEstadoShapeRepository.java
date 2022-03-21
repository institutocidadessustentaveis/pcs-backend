package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.administracao.ProvinciaEstadoShape;

@Repository
public interface ProvinciaEstadoShapeRepository extends JpaRepository<ProvinciaEstadoShape, Long> {

	@Query("select shape from ProvinciaEstadoShape shape where shape.estado.id = :idEstado")
	ProvinciaEstadoShape buscarPorEstado(Long idEstado);

	@Query("select shape " +
			"from ProvinciaEstadoShape shape " +
			"where shape.estado.id in :idsEstados")
	List<ProvinciaEstadoShape> buscarPorIdsEstado(List<Long> idsEstados);

}
