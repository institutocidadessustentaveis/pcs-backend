package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.ShapeItemDTO;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ShapeItem;

@Repository
public interface ShapeItemRepository extends JpaRepository<ShapeItem, Long>{	

	public List<ShapeItem> findByShapeFileId(Long idShapeFile);
	
	@Query(value="select si.id , si.id as idShapeFile, cast(si.atributos as varchar) as atributos from shape_itens si where si.id_shape_file = :idShapeFile and lower(cast(si.atributos ->> :atributo as varchar)) = lower(:referencia)", nativeQuery = true)
	public List<ShapeItemDTO> findByShapeFileIdAndAtributo(Long idShapeFile, String atributo, String referencia );

	@Query(value="select * from shape_itens where atributos ->> :atributo = :referencia", nativeQuery = true)
	public List<ShapeItem> buscarPorAtributo(String atributo, String referencia);

	@Query(value = "select ST_Contains(ST_Buffer(si.shape,0.005), (select e.shape from shape_itens e where e.id = :idShapeInterno)) from shape_itens as si where si.id = :idShapeEnvolvente", nativeQuery = true)
	public boolean shapesTemInterseccao(Long idShapeEnvolvente, Long idShapeInterno);
	
	@Query(value="select si.id , si.id as idShapeFile, cast( si.atributos as varchar) as atributos from shape_itens si where si.id_shape_file = :idShapeFile",nativeQuery = true)
	public List<ShapeItemDTO> findDtoByShapeFileId(Long idShapeFile);
	
	@Query(value="delete from shape_itens s where s.id  = :idShapeItem", nativeQuery = true)
	public void deleteByIdShapeItem(Long idShapeItem);
	
	@Query(value="select * from shape_itens si where si.atributos ->> :atributo = :referencia and si.id_shape_file = :idShapeFile", nativeQuery = true)
	public List<ShapeItem> buscarPorAtributoPorShapeFileId(String atributo, String referencia,Long idShapeFile);

}

