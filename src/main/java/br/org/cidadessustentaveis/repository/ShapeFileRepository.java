package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.ShapeFileDTO;
import br.org.cidadessustentaveis.dto.ShapeFileOpenEndPointDTO;
import br.org.cidadessustentaveis.dto.ShapeFileVisualizarDetalheDTO;
import br.org.cidadessustentaveis.dto.ShapeListagemMapaDTO;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ShapeFile;
import br.org.cidadessustentaveis.model.planjementoIntegrado.TemaGeoespacial;

import java.util.Optional;

import javax.transaction.Transactional;


@Repository
public interface ShapeFileRepository extends JpaRepository<ShapeFile, Long>{

	@Query("SELECT new br.org.cidadessustentaveis.dto.ShapeFileDTO("
			+ "shapefile.id, shapefile.ano, shapefile.titulo, shapefile.instituicao, shapefile.fonte, "
			+ "shapefile.sistemaDeReferencia, shapefile.tipoArquivo, shapefile.nivelTerritorial, shapefile.publicar) "
			+ "from ShapeFile shapefile")
	List<ShapeFileDTO> buscarTodosDto();

	@Query("SELECT s FROM ShapeFile s where s.publicar = true AND s.titulo != '$shape_zoneamento$' ORDER BY s.titulo ASC")
	Page<ShapeFile> buscarComPaginacao(Pageable pageable);
	
	@Query("SELECT DISTINCT s FROM ShapeFile s join s.usuario.credencial.listaPerfil p where s.id in (SELECT sf.id FROM ShapeFile sf where p.id = 1 AND s.titulo != '$shape_zoneamento$') OR s.id in"
			+ "(SELECT sf.id FROM ShapeFile sf where sf.publicar = true AND sf.titulo != '$shape_zoneamento$') ORDER BY s.titulo ASC")
	Page<ShapeFile> buscarComPaginacaoPrefeituraPublicadoTrueETodosDeAdmin(Pageable pageable);
	
	@Query("SELECT s FROM ShapeFile s join s.usuario.credencial.listaPerfil p where p.id = 1 AND s.titulo != '$shape_zoneamento$' ORDER BY s.titulo ASC")
	Page<ShapeFile> buscarComPaginacaoAdmin(Pageable pageable);
	
	@Query("SELECT DISTINCT s FROM ShapeFile s where s.id in (SELECT sf.id FROM ShapeFile sf where sf.publicar = true AND sf.titulo != '$shape_zoneamento$' AND sf.prefeitura.id != :idPrefeitura) OR s.id in"
			+ "(SELECT sf.id FROM ShapeFile sf where sf.publicar = true AND sf.titulo != '$shape_zoneamento$' AND sf.prefeitura is null) OR s.id in"
			+ "(SELECT sf.id FROM ShapeFile sf where sf.titulo != '$shape_zoneamento$' AND sf.prefeitura.id = :idPrefeitura) ORDER BY s.titulo ASC")
	Page<ShapeFile> buscarComPaginacaoAdminEOutrasPrefeiturasPublicadoTrueETodosDaPrefeituraLogada(Pageable pageable, Long idPrefeitura);
	
	@Query("SELECT count(s) FROM ShapeFile s where s.publicar = true")
	Long countShapesPublicados();

	@Query("SELECT new br.org.cidadessustentaveis.dto.ShapeFileDTO("
			+ "shapefile.id, shapefile.ano, shapefile.titulo, shapefile.instituicao, shapefile.fonte, "
			+ "shapefile.sistemaDeReferencia, shapefile.tipoArquivo, shapefile.nivelTerritorial, shapefile.publicar) "
			+ "from ShapeFile shapefile where shapefile.prefeitura.id = :idPrefeitura AND shapefile.titulo != '$shape_zoneamento$'")
	List<ShapeFileDTO> buscarTodosPorPrefeituraDto(Long idPrefeitura);

	@Query("select s from ShapeFile s where titulo != '$shape_zoneamento$'")
	List<ShapeFile> findAllByOrderByDataHoraCadastroDesc();
	
	@Query("select s from ShapeFile s where titulo != '$shape_zoneamento$' and s.prefeitura.id = :idPrefeitura")
	List<ShapeFile> findAllByOrderByDataHoraCadastroDescPrefeitura(Long idPrefeitura);

	@Query("SELECT shapefile FROM ShapeFile shapefile WHERE shapefile.cidade.id = :idCidade AND shapefile.titulo LIKE '%$shape_zoneamento$%' AND shapefile.publicar = true")
	ShapeFile buscarShapeZoneamento(Long idCidade);

	@Query("SELECT new br.org.cidadessustentaveis.dto.ShapeListagemMapaDTO(shapefile.id, shapefile.titulo, concat(c.nome,'-',e.sigla), shapefile.exibirAuto) from ShapeFile shapefile left join shapefile.prefeitura p left join p.cidade c left join c.provinciaEstado e where shapefile.publicar = true and shapefile.titulo != '$shape_zoneamento$' ORDER BY shapefile.titulo")
	public List<ShapeListagemMapaDTO> buscarShapesListagemMapa();
	
	@Query("SELECT shapefile.fileName FROM ShapeFile shapefile WHERE shapefile.id = :idShapeFile")
	String buscarFileNameShapeFilePorId(Long idShapeFile);

	boolean existsByTitulo(String titulo);

	boolean existsByTituloAndIdNot(String trim, Long id);
	
	@Query("SELECT shapefile FROM ShapeFile shapefile WHERE shapefile.id = :id and shapefile.publicar = true")
	Optional<ShapeFile> buscarPorIdPublicarIsTrue(Long id);
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.ShapeFileOpenEndPointDTO(sf.id, sf.titulo, c, sf.dataHoraCadastro) FROM ShapeFile sf LEFT JOIN sf.cidade c WHERE sf.publicar = true")
	List<ShapeFileOpenEndPointDTO> buscarShapeFileOpenEndPointDTO();
	
	@Query("SELECT shapefile FROM ShapeFile shapefile WHERE shapefile.titulo LIKE '%$shape_zoneamento$%' and shapefile.fileName LIKE :fileName AND shapefile.publicar = false")
	ShapeFile buscarShapeZoneamentoMunicipios(String fileName);
	
	@Query("select s from ShapeFile s where titulo != '$shape_zoneamento$' and s.subdivisaoCidade.id = :idSubdivisaoCidade")
	ShapeFile findByidSubdivisaoCidade(Long idSubdivisaoCidade);
	
	@Query("select s.id from ShapeFile s where titulo != '$shape_zoneamento$' and s.subdivisaoCidade.id = :idSubdivisaoCidade")
	Long buscarShapeFileIdPorSubdivisaoId(Long idSubdivisaoCidade);

	ShapeFile findBySubdivisaoCidadeId(Long id);
	
	
	@Query(" select distinct new br.org.cidadessustentaveis.dto.ShapeFileVisualizarDetalheDTO(shape) from ShapeFile shape where shape.id = :idShapeFile" )
    ShapeFileVisualizarDetalheDTO buscarShapeFileVisualizarDetalheDTOPorIdShapeFile(Long idShapeFile);
	
	@Query("SELECT s.ods FROM ShapeFile s where s.id = :idShapeFile")
	List<ObjetivoDesenvolvimentoSustentavel> buscarOdsDoShapeFileId(Long idShapeFile);
	
	@Transactional
	@Modifying
	@Query("update ShapeFile s set s.exibirAuto = false"
		  +" where s.exibirAuto = true")
	void setExibirAutoToFalseAll();
	
	@Query("select shape from ShapeFile shape where shape.titulo like :nomeCamada")
	ShapeFile buscarShapeCGEE(String nomeCamada);
}

