package br.org.cidadessustentaveis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.ProvinciaEstadoDTO;
import br.org.cidadessustentaveis.dto.ProviniciaEstadoBuscaDTO;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;

@Repository
public interface ProvinciaEstadoRepository extends JpaRepository<ProvinciaEstado, Long> {

	Optional<ProvinciaEstado> findByNome(String nome);

	@Query("select estado from ProvinciaEstado estado where lower(estado.nome) "
			+ "like %:nome% "
			+ " or lower(estado.sigla) = :nome "
			+ "order by estado.nome")
	Page<ProvinciaEstado> findByNomeLike(String nome, Pageable page);
	
	List<ProvinciaEstado> findByPaisOrderByNomeAsc(Pais pais);
	
	@Query("select estado from ProvinciaEstado estado where lower(estado.pais.nome) "
			+ "like %:nomePais% "
			+ "order by estado.nome asc")
	List<ProvinciaEstado> findAllByPaisNomeOrderByNomeAsc(String nomePais);
	
	@Query("select new br.org.cidadessustentaveis.model.administracao.ProvinciaEstado(p.id, p.nome) from ProvinciaEstado p order by p.nome")
	List<ProvinciaEstado> findComboBoxProvinciaEstado();

	@Query("select concat(concat(pe.id , ','), pe.nome) as nome  from ProvinciaEstado pe inner join pe.pais p on pe.pais = p.id where p.id = :id order by pe.nome")
	List<String> buscarNomeProvinciaEstadoPorIdPais(Long id);
	
	
	@Query("select new br.org.cidadessustentaveis.dto.ProviniciaEstadoBuscaDTO(estado.id) from ProvinciaEstado estado where estado.nome = :nomeEstado")
	ProviniciaEstadoBuscaDTO buscarIdPorNome(String nomeEstado);

	@Query("select new br.org.cidadessustentaveis.dto.ProvinciaEstadoDTO(estado.id, estado.nome) from ProvinciaEstado estado where estado.pais.id = :idPais order by estado.nome")
	List<ProvinciaEstadoDTO> buscarPorPaisResumido(Long idPais);
	
	@Query("select new br.org.cidadessustentaveis.model.administracao.ProvinciaEstado(p.id, p.nome) from ProvinciaEstado p where p.pais = 1 order by p.nome")
	List<ProvinciaEstado> findComboBoxProvinciaEstadoBrasil();
}
