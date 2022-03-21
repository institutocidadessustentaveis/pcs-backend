package br.org.cidadessustentaveis.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.IndicadorParaComboDTO;
import br.org.cidadessustentaveis.dto.IndicadorcomparativomesmacidadeDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.VariavelIndicadorDuplicadaDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.Variavel;
import br.org.cidadessustentaveis.model.planjementoIntegrado.MaterialApoio;

@Repository
public interface IndicadorRepository extends JpaRepository<Indicador, Long>{
	
	@Query("SELECT i FROM Indicador i WHERE i.prefeitura = ?1 AND i.id = ?2")
	public Optional<Indicador> findByPrefeituraAndId(Prefeitura prefeitura, Long id);

	@Query("SELECT i" +
            " from Indicador i " +
            " left outer join i.prefeitura p" +
            " where p.id = :idPrefeitura")
	public List<Indicador> findByPrefeitura(Long idPrefeitura);

	@Query("SELECT i" +
			" from Indicador i " +
			" left outer join i.prefeitura p" +
			" where p.id = :idPrefeitura")
	public List<Indicador> findByPrefeitura(Long idPrefeitura, Pageable pageable);

	@Query("SELECT i FROM Indicador i WHERE i.prefeitura IS NULL OR i.prefeitura = ?1")
	public List<Indicador> findForPrefeitura(Prefeitura prefeitura);

	List<Indicador> findByVariaveisIn(Variavel variavel);

	@Query("SELECT i FROM Indicador i left join i.ods o WHERE i.prefeitura IS NULL ORDER BY i.nome")
	public List<Indicador> buscarIndicadoresPcs();

	@Query("SELECT i FROM Indicador i WHERE i.metaODS.id IN ?1")
	public List<Indicador> findByIdsMetasOds(List<Long> idsMetasOds);

	@Query("SELECT distinct new br.org.cidadessustentaveis.dto.IndicadorcomparativomesmacidadeDTO(i.id, i.nome) FROM IndicadorPreenchido ip"
			+ " inner join ip.indicador i"
			+ " inner join ip.prefeitura p"
			+ " inner join p.cidade c"
			+ " where c.id = :idCidade ORDER BY i.nome ASC")
	List<IndicadorcomparativomesmacidadeDTO> findByIdCidade(Long idCidade);
	
	
	@Query("SELECT distinct new br.org.cidadessustentaveis.dto.IndicadorcomparativomesmacidadeDTO(i.id, i.nome) FROM IndicadorPreenchido ip"
			+ " inner join ip.indicador i"
			+ " inner join ip.prefeitura p"
			+ " inner join p.cidade c"
			+ " where c.id = :idCidade and ip.ano = :ano ORDER BY i.nome ASC")
	List<IndicadorcomparativomesmacidadeDTO> findTodosIndicadoresPorIdCidadePorAno(Long idCidade, Short ano);
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.IndicadorParaComboDTO(i.id, i.nome) FROM Indicador i WHERE i.prefeitura IS NULL ORDER BY i.nome")
	public List<IndicadorParaComboDTO> buscarIndicadoresPcsParaCombo();
	
	public List<Indicador> findByEixoIdAndPrefeituraIsNullOrderByNome(Long idEixo);
	
	@Query("select new br.org.cidadessustentaveis.dto.ItemComboDTO(i.id, i.nome) from Indicador i inner join i.ods ods where ods.id = :id")
	List<ItemComboDTO> buscarOdsPorIdOds(Long id);

	@Query("select new br.org.cidadessustentaveis.dto.ItemComboDTO(i.id, i.nome) from Indicador i")
	List<ItemComboDTO> buscarItemCombo();
	
	@Query("select new br.org.cidadessustentaveis.dto.ItemComboDTO(i.id, i.nome) FROM Indicador i WHERE i.eixo.id IN ?1 ORDER BY i.nome ASC")
	List<ItemComboDTO> buscarIndicadoresPorIdEixoItemCombo(List<Long> idEixo);
	
	@Query("select i from Indicador i inner join fetch i.ods o where o.id = :idOds")
	List<Indicador> findAllByIdOds(Long idOds);

	List<Indicador> findByPrefeituraCidadeIdOrderByNome(Long idCidade);

	@Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM Indicador i WHERE i.prefeitura.cidade.id = :idCidade")
    boolean cidadePossuiIndicadoresCadastrados(Long idCidade);

	public List<Indicador> findByIdGreaterThanEqual(Long idMinimo);
	
	@Query("SELECT id FROM Indicador")
	public List<Long> findAllId();
	
	@Query("SELECT id FROM Indicador where prefeitura is null")
	public List<Long> findPcsId();

	@Query("SELECT id FROM Indicador where prefeitura is not null")
	public List<Long> findPrefeituraId();
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.IndicadorParaComboDTO(i.id, i.nome) FROM Indicador i WHERE i.prefeitura IS NULL and ?1 member i.variaveis ORDER BY i.nome")
	public List<IndicadorParaComboDTO> buscarIndicadoresPcsParaComboPorVariavel(Variavel variavel);
	
	@Query(value="select new br.org.cidadessustentaveis.dto.VariavelIndicadorDuplicadaDTO(i.id, v.id, count(v.id)) from Indicador i join i.variaveis v group by i.id, v.id having count(v.id) > 1  ")
	public List<VariavelIndicadorDuplicadaDTO> buscarVariavelIndicadorDuplicada();
	
	@Query("select indicador from Indicador indicador where indicador.id in ?1")
	List<Indicador> buscarIndicadoresPorIds(List<Long> ids);
	
	@Query("SELECT distinct new br.org.cidadessustentaveis.dto.IndicadorParaComboDTO(i.id, i.nome) FROM IndicadorPreenchido ip"
			+ " inner join ip.indicador i"
			+ " where i.prefeitura IS NULL ORDER BY i.nome ASC")
	public List<IndicadorParaComboDTO> buscarIndicadoresParaComboPorPreenchidos();
	
	@Query("SELECT distinct ip.ano FROM IndicadorPreenchido ip inner join ip.indicador i WHERE i.id = :idIndicador ORDER BY ip.ano DESC")
	List<Short> carregarComboAnosPreenchidosPorIdIndicador(Long idIndicador);

	public List<Indicador> findByTipoConteudoIsNullAndPrefeituraIsNull();
	
	@Query("SELECT i.id FROM Indicador i WHERE ?1 member i.variaveis")
	public List<Long> buscarIdsIndicadoresPorVariavel(Variavel variavel);
	
	@Query(value="select distinct c.id_indicador from indicador_variavel c where c.id_variavel in ?1", nativeQuery = true)
	public List<BigInteger> buscarIdsIndicadoresPorVariaveis(List<Long> ids);
	
	@Query("select indicador from Indicador indicador where indicador.id = :idIndicador")
	Indicador buscarIndicadorPorId(Long idIndicador);

	@Query("select indicador from Indicador indicador where indicador.nome = :nome")
	Indicador buscarIndicadorPorNome(String nome);
}
