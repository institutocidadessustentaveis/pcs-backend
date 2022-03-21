package br.org.cidadessustentaveis.repository;


import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.planjementoIntegrado.MapaTematico;
	
@Repository
public interface MapaTematicoRepository extends JpaRepository<MapaTematico, Long>{

	Optional<MapaTematico> findById(Long id);

	List<MapaTematico> findByUsuarioIdAndIdShapeFile(Long idUsuario, Long idShapeFile);
	
	@Transactional
	@Modifying
	@Query("update MapaTematico mt set mt.exibirAuto = :exibirAuto"
		  +" where mt.id = :idMapa")
	void editarExibirAutoById(Long idMapa, boolean exibirAuto);
	
	@Transactional
	@Modifying
	@Query("update MapaTematico mt set mt.exibirAuto = false"
		  +" where mt.exibirAuto = true AND mt.idShapeFile = :idShape")
	void setExibirAutoToFalseAll(Long idShape);
	
	@Query(value = "select mt from MapaTematico mt"
		         + " where mt.idShapeFile = :idShapeFile AND mt.exibirAuto = true")
	MapaTematico mapaExibirAuto(Long idShapeFile);
	
	//
	
	@Transactional
	@Modifying
	@Query("update MapaTematico mt set mt.exibirLegenda = :exibirLegenda"
		  +" where mt.id = :idMapa")
	void editarExibirLegendaById(Long idMapa, boolean exibirLegenda);
}
