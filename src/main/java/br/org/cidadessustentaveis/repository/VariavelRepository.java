package br.org.cidadessustentaveis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.VariavelDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.indicadores.Variavel;

@Repository
public interface VariavelRepository extends JpaRepository<Variavel, Long>{
	public List<Variavel> findByOrderByIdDesc();
	
	@Query("SELECT v FROM Variavel v WHERE v.prefeitura = ?1 AND v.id = ?2")
	public Optional<Variavel> findByPrefeituraAndId(Prefeitura prefeitura, Long id);
	
	@Query("SELECT v FROM Variavel v WHERE v.prefeitura.id = ?1 ORDER BY v.nome ASC")
	public List<Variavel> findByPrefeitura(Long idPrefeitura);
	
	@Query("SELECT v FROM Variavel v WHERE v.prefeitura is null ORDER BY v.nome ASC")
	public List<Variavel> buscarSemPrefeituraOrderByName();

	@Query("SELECT v FROM Variavel v WHERE v.prefeitura = ?1")
	public List<Variavel> findAll(Prefeitura prefeitura);
	
	public List<Variavel> findByPrefeituraIsNull();

	public List<Variavel> findByTipoInAndPrefeituraIsNullOrderByNome(List<String> tipos);
	
	@Query("select new br.org.cidadessustentaveis.dto.VariavelDTO(v.id, v.nome) from Variavel v where v.id in :idsIndicador")
	public List<VariavelDTO> buscarPorIdIndicador(List<Long> idsIndicador);
	
	@Query("select v.id from Indicador i inner join i.variaveis v where i.id = :idIndicador")
	public List<Long> buscarIdsPorIdIndicador(Long idIndicador);
	
	@Query("select new br.org.cidadessustentaveis.dto.ItemComboDTO(v.id, v.nome) from Variavel v where v.prefeitura is null ORDER BY v.nome ASC")
	public List<ItemComboDTO> buscarVariaveisPcsParaCombo();
	
	@Query(value = "select v.id from Variavel v where lower(v.nome) like %:nome%")
	public List<Long> findByNomeLike(String nome);
	
	@Query("SELECT v FROM Variavel v WHERE v.prefeitura.id = null OR v.prefeitura.id = :idPrefeitura ORDER BY v.nome ASC")
	public List<Variavel> listarApenasPCSEPrefeituraPorId(Long idPrefeitura);
	
}
