package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.org.cidadessustentaveis.dto.CidadeComBoasPraticasDTO;
import br.org.cidadessustentaveis.dto.CidadeComPrefeituraDTO;
import br.org.cidadessustentaveis.dto.CidadeComboDTO;
import br.org.cidadessustentaveis.dto.CidadeDTO;
import br.org.cidadessustentaveis.dto.CidadeMandatosDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.PrefeituraDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;

public interface PrefeituraRepository extends JpaRepository<Prefeitura, Long> {
	@Query("SELECT p FROM Prefeitura p WHERE p.signataria IS TRUE  AND p.cidade = :cidade ORDER BY p.id ASC")
	List<Prefeitura> findByCidade(Cidade cidade);

	@Query("select new Prefeitura(p.id, p.nome, p.cargo, p.email, p.telefone, "
			+ "p.cidade.id, p.cidade.nome, p.cidade.latitude, p.cidade.longitude, p.cidade.codigoIbge, "
			+ "p.partidoPolitico.id, p.cidade.populacao, p.partidoPolitico.nome, p.partidoPolitico.siglaPartido, "
			+ "p.cidade.provinciaEstado.id, p.cidade.provinciaEstado.nome, p.cidade.provinciaEstado.sigla, p.signataria, p.inicioMandato, p.fimMandato) " +
            "from Prefeitura p " +
            "where p.signataria = true")
	List<Prefeitura> buscarPrefeiturasSignatarias();
	
	@Query("select new br.org.cidadessustentaveis.dto.CidadeMandatosDTO( p.inicioMandato, p.fimMandato,  p.signataria, p.cidade.nome, p.cidade.provinciaEstado.sigla, p.cidade.id) " +
            "from Prefeitura p " +
            "where p.inicioMandato != null and p.cidade.id = :idCidade and p.signataria != null")
	List<CidadeMandatosDTO> buscarCidadesSignatariasDataMandatos(Long idCidade);

	@Query("select p " +
            "from Prefeitura p " +
            "where p.signataria = true and " +
            "p.cidade.provinciaEstado.id = :idEstado")
	List<Prefeitura> buscarPrefeiturasSignatariasPorEstado(Long idEstado);
	
	@Query("select distinct new br.org.cidadessustentaveis.dto.ItemComboDTO(c.id, c.nome, c.provinciaEstado.sigla) from Prefeitura p inner join p.cidade c order by c.nome")
	List<ItemComboDTO> findComboBoxPrefeitura();
	
	@Query("select p " +
            "from Prefeitura p " +
			"left join p.aprovacaoPrefeitura ap " +
            "where p.cidade.id = :idCidade and ap.status = 'Aprovada' and p.signataria = true")
	Prefeitura findByIdCidadeOrderByCidadeId(Long idCidade);
	
	@Query("select new br.org.cidadessustentaveis.dto.PrefeituraDTO(p.id, p.cidade.id, p.inicioMandato, p.fimMandato) from Prefeitura p where p.id = :id")
	PrefeituraDTO buscarLogin(Long id);

	Page<Prefeitura> findBySignataria(boolean b, Pageable of);

	Long countBySignataria(boolean b);

	List<Prefeitura> findBySignatariaOrderByCidadeNome(boolean b);
	
	@Query("select distinct new br.org.cidadessustentaveis.dto.CidadeComboDTO(p.cidade.id, p.cidade.nome, p.cidade.provinciaEstado.sigla) from Prefeitura p WHERE p.cidade.isSignataria IS TRUE and p.signataria is TRUE order by p.cidade.nome")
	List<CidadeComboDTO> buscarCidadesSignatarias();

	Prefeitura findTopByCidadeIdAndSignatariaOrderByIdDesc(Long idCidade, boolean b);

	Prefeitura findTopByCidadeIdAndInicioMandatoIsNotNullOrderByIdDesc(Long idCidade);
	
	@Query("select new br.org.cidadessustentaveis.model.administracao.Prefeitura(p.id, c.id, c.codigoIbge, p.inicioMandato, p.fimMandato) from Usuario u inner join u.prefeitura p inner join p.cidade c where u.id = :idUsuario")
	Prefeitura buscarPrefeituraPorIdUsuario(Long idUsuario);
	
	@Query("select p " +
            "from Prefeitura p " +
            "where p.cidade.id = :idCidade order by p.fimMandato desc")
	List<Prefeitura> buscarPorIdCidadeUltimaPrefeitura(Long idCidade);
	
	@Query("select distinct new br.org.cidadessustentaveis.dto.CidadeComPrefeituraDTO(p.cidade.nome, p.cidade.codigoIbge, p.cidade.provinciaEstado.nome, p.cidade.provinciaEstado.sigla, p.cidade.populacao, p.cidade.id)"
			+ "from Prefeitura p"
			+ " order by p.cidade.nome")
	List<CidadeComPrefeituraDTO> findCidadesComPrefeitura();
	
	@Query("select new br.org.cidadessustentaveis.dto.CidadeMandatosDTO( p.inicioMandato, p.fimMandato, p.signataria, p.cidade.nome, p.cidade.provinciaEstado.sigla, p.cidade.id) " +
            "from Prefeitura p " +
            "where p.cidade.id = :idCidade and p.signataria != null")
	List<CidadeMandatosDTO> buscarPrefeiturasSignatariasPorIdCidade(Long idCidade);	
	
	@Query("select distinct new br.org.cidadessustentaveis.dto.CidadeComPrefeituraDTO(p.cidade.nome, p.cidade.codigoIbge, p.cidade.provinciaEstado.nome, p.cidade.provinciaEstado.sigla, p.cidade.populacao, p.cidade.id) " +
			"from Prefeitura p " +
			"where p.signataria != null " +
			"order by p.cidade.nome")
	List<CidadeComPrefeituraDTO> findCidadesSignatariasComPrefeitura();


	@Query("select new br.org.cidadessustentaveis.dto.CidadeComBoasPraticasDTO( bp.municipio.id, bp.municipio.nome, bp.municipio.provinciaEstado.sigla, count(bp)) " +
            "from BoaPratica bp " +
            "where bp.prefeitura.inicioMandato != null " +
            "group by bp.municipio.id,  bp.municipio.nome, bp.municipio.provinciaEstado.sigla " +
            "order by bp.municipio.nome")
	List<CidadeComBoasPraticasDTO> buscarPrefeiturasSignatariasComBoasPraticas();
	
	@Query(value = "select distinct new br.org.cidadessustentaveis.dto.PrefeituraDTO(p.id, p.inicioMandato, p.fimMandato) from Prefeitura p "
			+ "inner join p.cidade c where c.id = :idCidade")
	List<PrefeituraDTO> buscarMandatoPorIdCidade(Long idCidade);
	
	@Query("SELECT p FROM Prefeitura p WHERE p.signataria IS TRUE AND p.cidade.id = :idCidade ORDER BY p.id ASC")
	List<Prefeitura> findByCidadeId(Long idCidade);
	
	@Query("select new Prefeitura(p.id, p.nome, p.cargo, p.telefone, p.email, p.cidade, p.inicioMandato, p.fimMandato, p.partidoPolitico) " +
            "from Prefeitura p " +
            "where p.signataria = true")
	List<Prefeitura> buscarDadosCadastraisPrefeiturasSignatarias();
}
