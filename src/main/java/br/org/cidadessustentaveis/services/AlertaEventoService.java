package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.AlertaEventoDTO;
import br.org.cidadessustentaveis.dto.EventoDTO;
import br.org.cidadessustentaveis.model.administracao.Perfil;
import br.org.cidadessustentaveis.model.eventos.AlertaEvento;
import br.org.cidadessustentaveis.model.eventos.Evento;
import br.org.cidadessustentaveis.repository.AlertaEventoRepository;

@Service
public class AlertaEventoService {
	@Autowired
	private AlertaEventoRepository alertaRepository;
	@Autowired
	private EventoService eventoService;
	@Autowired
	private PerfilService perfilService;
	
	public AlertaEvento salvar(AlertaEventoDTO dto) {
		if(dto != null) {
			Evento evento = eventoService.buscarPorId(dto.getIdEvento());
			LocalDate diaEvento = evento.getDataEvento();
			
			AlertaEvento alerta = null;
			if(dto.getId() != null) {
				alerta = alertaRepository.findById(dto.getId()).orElse(new AlertaEvento());
				alerta = copyPropertiesAlerta(alerta, dto);
			} else {
				alerta = new AlertaEvento(null,dto.getTitulo(), dto.getDescricao(), dto.getQtdDias(), dto.getApenasPrefeitura(), null, null, evento, false, null, null );
			}
			LocalDate dataEnviar = diaEvento.minusDays(dto.getQtdDias());
			alerta.setDataEnviar(dataEnviar);
			if(dto.getPerfis() != null) {
				alerta.setPerfis(new ArrayList<>());
				for(Long idPerfil : dto.getPerfis()) {
					Perfil p = perfilService.buscarPorId(idPerfil);
					alerta.getPerfis().add(p);
				}
			} else {
				dto.setPerfis(null);
			}
					
			alertaRepository.save(alerta);
			return alerta;
		}
		return null;
	}
	
	public AlertaEvento copyPropertiesAlerta(AlertaEvento alerta, AlertaEventoDTO dto) {
		alerta.setTitulo(dto.getTitulo());
		alerta.setDescricao(dto.getDescricao());
		alerta.setQtdDias(dto.getQtdDias());
		alerta.setApenasPrefeitura(dto.getApenasPrefeitura());
		if(!dto.getPerfis().isEmpty()) {
			List<Perfil> perfisDTO = new ArrayList<Perfil>();
			dto.getPerfis().forEach(id -> {
				Perfil perfilTemp = perfilService.buscarPorId(id);
				if(perfilTemp != null) {
					perfisDTO.add(perfilTemp);
				};
			});
			alerta.setPerfis(perfisDTO);
		}
//		alerta.setImagem(dto.getImagem());
		alerta.setEvento(eventoService.buscarPorId(dto.getIdEvento()));
		alerta.setEnviado(false);
		alerta.setDataEnviar(dto.getDataEnviar());
		alerta.setDataEnvio(dto.getDataEnvio());
		
		return alerta;
	}

	public List<AlertaEvento> buscarAlertas(String email ) {
		List<AlertaEvento> alertas = null;
		return alertas;
	}

	public void excluir(Long id) {
		this.alertaRepository.deleteById(id);
	}

	public List<AlertaEvento> findByEventoId(Long evento) {
		return this.alertaRepository.findByEventoId(evento);
	}

	public List<AlertaEvento> findByDataEnviar(LocalDate dataEnviar) {
		List<AlertaEvento> lista = this.alertaRepository.findByDataEnviarAndEnviado(dataEnviar, false);
		
		return lista ;
	}

	public void definirEnviado(AlertaEvento alerta) {
		alerta.setEnviado(true);
		alerta.setDataEnvio(LocalDate.now());
		this.alertaRepository.save(alerta);
		
	}

	public Evento buscarEventoPorAlertaId(Long id) {
		Evento evento = this.alertaRepository.findEventoById(id);
		return evento;
	}
	
	public EventoDTO buscarEventoDTOPorAlertaId(Long id) {
		EventoDTO evento = this.alertaRepository.buscarEventoDTOPorAlertaId(id);
		return evento;
	}
	
}
