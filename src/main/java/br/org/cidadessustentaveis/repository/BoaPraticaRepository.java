package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.BoaPraticaResumidoToListDTO;
import br.org.cidadessustentaveis.dto.CidadeComBoasPraticasDTO;
import br.org.cidadessustentaveis.dto.CidadeDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import br.org.cidadessustentaveis.model.indicadores.Indicador;

@Repository
public interface BoaPraticaRepository extends JpaRepository<BoaPratica, Long>{

	@Query("SELECT boaPratica from BoaPratica boaPratica where boaPratica.tipo like ?1")
	List<BoaPratica> buscarBoasPraticasPorTipo(String tipo);

	@Query("SELECT new br.org.cidadessustentaveis.dto.BoaPraticaResumidoToListDTO(bp.id, m.nome, bp.titulo, bp.nomeResponsavel, bp.dataPublicacao, bp.tipo, p.id) from BoaPratica bp "
			+ "left join bp.municipio as m "
			+ "left join bp.prefeitura as p")
	List<BoaPraticaResumidoToListDTO> buscarBoasPraticasGeral();

	@Query("SELECT new br.org.cidadessustentaveis.dto.BoaPraticaResumidoToListDTO(bp.id, m.nome, bp.titulo, bp.nomeResponsavel, bp.dataPublicacao, bp.tipo, p.id) from BoaPratica bp "
			+ "join bp.municipio as m "
			+ "join bp.prefeitura as p "
			+ "where bp.prefeitura.id = :idPrefeitura")
	List<BoaPraticaResumidoToListDTO> findByPrefeituraId(Long idPrefeitura);
	
    @Query("SELECT " +
            "    new br.org.cidadessustentaveis.dto.CidadeComBoasPraticasDTO(b.municipio, COUNT(b)) " +
            "FROM " +
            "    BoaPratica b " +
            "WHERE " +
            "    b.prefeitura is null " +
            "GROUP BY " +
            "    b.municipio")
	List<CidadeComBoasPraticasDTO> buscarCidadescomBoasPraticas();
    
    @Query("SELECT " +
            "     new br.org.cidadessustentaveis.dto.CidadeDTO(b.municipio.id, b.municipio.nome, b.municipio.provinciaEstado)" +
            "FROM " +
            "    BoaPratica b")
	List<CidadeDTO> buscarCombosCidadesComBoasPraticas();
    
    

	List<BoaPratica> findAllByOrderByIdDesc();
	
	
	@Query("select n " +
			"from BoaPratica n " +
			"where n.prefeitura is null and n.paginaInicial = true " +
            "order by n.id desc")
	List<BoaPratica> carregarUltimasBoasPraticas(Pageable a);
	
	
    @Query("SELECT " +
            "    b.ods " +
            "FROM " +
            "    BoaPratica b "
            + "where b.id = :idBoaPratica")
	List<ObjetivoDesenvolvimentoSustentavel> buscarOdsDaBoaPratica(Long idBoaPratica);
    
    @Query("SELECT " +
            "    b.indicadores " +
            "FROM " +
            "    BoaPratica b "
            + "where b.id = :idBoaPratica")
	List<Indicador> buscarIndicadoresDaBoaPratica(Long idBoaPratica);
    
    @Query("SELECT " +
            "    b.metasOds " +
            "FROM " +
            "    BoaPratica b "
            + "where b.id = :idBoaPratica")
	List<MetaObjetivoDesenvolvimentoSustentavel> buscarMetasOdsDaBoaPratica(Long idBoaPratica);

}
