package br.org.cidadessustentaveis.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.EventoDTO;
import br.org.cidadessustentaveis.dto.EventoMapaDTO;
import br.org.cidadessustentaveis.dto.EventosFiltradosDTO;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.eventos.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
	@Query("SELECT new br.org.cidadessustentaveis.dto.EventoMapaDTO(e.id, e.tipo, e.nome, e.descricao, e.dataEvento, e.organizador, e.latitude, e.longitude ) FROM Evento e where e.tipo not like 'Prefeitura' and e.dataEvento >= ?1")
	List<EventoMapaDTO> findEventosOficiaisApartir(LocalDate data);
	
	@Query("SELECT " +
            "    e.eixos " +
            "FROM " +
            "    Evento e "
            + "where e.id = :idEvento")
	List<Eixo> findEixoById(Long idEvento);
	
	@Query("SELECT " +
            "    e.temas " +
            "FROM " +
            "    Evento e "
            + "where e.id = :idEvento")
	List<AreaInteresse> findAreaInteresseById(Long idEvento);

	
	@Query("select new br.org.cidadessustentaveis.dto.EventoDTO(e.id, e.tipo, e.nome, e.dataEvento, e.horario, e.publicado, e.endereco, e.descricao, e.site, e.latitude, e.longitude) from Evento e"
			+ " where e.usuarioCadastro.id != null")
	List<EventoDTO> buscarEventosToList();
	
	@Query("select new br.org.cidadessustentaveis.dto.EventoDTO(e.id, e.tipo, e.nome, e.dataEvento, e.horario, e.publicado, e.endereco, e.descricao, e.site, e.latitude, e.longitude) from Evento e"
			+ " where e.cidade.id = :idCidade and e.tipo like 'Prefeitura' and e.usuarioCadastro.id != null")
	List<EventoDTO> buscarEventosToListByIdCidade(Long idCidade);
	
	List<EventoDTO> findByTemas(List<AreaInteresse> temas);
	
	@Query("select new br.org.cidadessustentaveis.dto.EventoDTO(e.id, e.tipo, e.nome, e.dataEvento, e.horario, e.publicado, e.endereco, e.descricao, e.site, e.latitude, e.longitude) from Evento e"
			+ " join e.temas as t"
			+ " where t.nome like '%Participação Cidadã%' and e.publicado = true")
	List<EventoDTO> buscarEventosParticipacaoCidada();
	
	@Query("select e from Evento e"
			+ " join e.temas as t" + " where t.nome like '%Participação Cidadã%' and e.publicado = true"
			+ " and e.dataEvento >= :dataInicial ORDER BY e.dataEvento ASC")
	List<EventosFiltradosDTO> buscarEventosParticipacaoCidadaSemDataFinal(LocalDate dataInicial);

	@Query("select e from Evento e"
			+ " join e.temas as t" + " where t.nome like '%Participação Cidadã%' and e.publicado = true"
			+ " and e.dataEvento between :dataInicial and :dataFinal ORDER BY e.dataEvento ASC")
	List<EventosFiltradosDTO> buscarEventosParticipacaoCidadaComDataFinal(LocalDate dataInicial, LocalDate dataFinal);
	
	@Query("select te.id from Evento e join e.temas te where e.id = ?1")
	List<Long> findTemasById(Long id);
	
	@Query("select e from Evento e where e.tipo != 'Prefeitura' ORDER BY e.dataEvento ASC")
	List<EventosFiltradosDTO>findAllNotPrefeitura();
	
	@Query("select new br.org.cidadessustentaveis.dto.EventoDTO(e.id, e.tipo, e.nome, e.dataEvento, e.horario, e.publicado, e.endereco, e.descricao, e.site, e.latitude, e.longitude) from Evento e"
			+ " where e.tipo like '%Capacitação para Prefeituras Signatárias%' "
			+ " and e.dataEvento >= ?1" )
	List<EventoDTO> buscarEventosCapacitacao(LocalDate data);
	
	@Query("select new br.org.cidadessustentaveis.dto.EventoDTO(e.id, e.tipo, e.nome) from Evento e"
			+ " where e.tipo like '%Capacitação para Prefeituras Signatárias%' ")
	List<EventoDTO> buscarTodosEventosCapacitacao();
	
	@Query("select new br.org.cidadessustentaveis.dto.EventoDTO(e.id, e.tipo, e.nome, e.dataEvento, e.horario, e.publicado, e.endereco, e.descricao, e.site, e.latitude, e.longitude) from Evento e"
			+ " WHERE e.tipo = 'Academia' and e.publicado = true")
	List<EventoDTO> buscarEventosTipoAcademiaCalendario();	
	
}
