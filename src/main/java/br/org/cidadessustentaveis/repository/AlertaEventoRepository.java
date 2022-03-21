package br.org.cidadessustentaveis.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.EventoDTO;
import br.org.cidadessustentaveis.model.eventos.AlertaEvento;
import br.org.cidadessustentaveis.model.eventos.Evento;

@Repository
public interface AlertaEventoRepository extends JpaRepository<AlertaEvento, Long>{

	List<AlertaEvento> findByEventoId(Long evento);
	
	List<AlertaEvento> findByDataEnviarAndEnviado(LocalDate dataEnviar, boolean b);
	@Query("select e from AlertaEvento ae join ae.evento e where ae.id = ?1")
	Evento findEventoById(Long id);
	
	@Query("select new br.org.cidadessustentaveis.dto.EventoDTO(e) from AlertaEvento ae join ae.evento e where ae.id = ?1")
	EventoDTO buscarEventoDTOPorAlertaId(Long id);

	
}
