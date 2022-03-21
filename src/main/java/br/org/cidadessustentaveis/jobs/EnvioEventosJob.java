package br.org.cidadessustentaveis.jobs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.mail.EmailException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.org.cidadessustentaveis.config.SpringContext;
import br.org.cidadessustentaveis.dto.EventoDTO;
import br.org.cidadessustentaveis.model.eventos.AlertaEvento;
import br.org.cidadessustentaveis.services.AlertaEventoService;
import br.org.cidadessustentaveis.services.EventoService;
import br.org.cidadessustentaveis.services.UsuarioService;
import br.org.cidadessustentaveis.util.EmailUtil;

@Component
@Transactional(propagation=Propagation.REQUIRES_NEW)
public class EnvioEventosJob implements Job {

	@Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	ApplicationContext appContext = SpringContext.getAppContext();
    	AlertaEventoService alertaEventoService = (AlertaEventoService)appContext.getBean("alertaEventoService");
    	EventoService eventoService = (EventoService)appContext.getBean("eventoService");
    	UsuarioService usuarioService = (UsuarioService)appContext.getBean("usuarioService");
    	EmailUtil emailUtil = (EmailUtil)appContext.getBean("emailUtil");
    	List<AlertaEvento> alertas = alertaEventoService.findByDataEnviar(LocalDate.now());
    	if(!alertas.isEmpty()) {
    		AlertaEvento alerta = alertas.get(0);
    		EventoDTO evento = alertaEventoService.buscarEventoDTOPorAlertaId(alerta.getId());
    		List<Long> listaAreaInteresse = eventoService.getTemasByEventoId(evento.getId());
    		
    		List<String> emailsUsuariosAreaDeInteresseIgualEvento = new ArrayList<>();    		
    		
    		if(!listaAreaInteresse.isEmpty()) {    		
    			emailsUsuariosAreaDeInteresseIgualEvento = usuarioService.listarEmailUsuarioComAreaDeInteresseId(listaAreaInteresse);  
    		}
    		
    		List<String> emailsUsuariosCidadeInteresseIgualEvento = usuarioService.listarEmailUsuarioComCidadeInteresseId(evento.getCidade());
    		
    		List<String> destinatarios = Stream.concat(emailsUsuariosAreaDeInteresseIgualEvento.stream(), emailsUsuariosCidadeInteresseIgualEvento.stream()).distinct().collect(Collectors.toList());
    		
    		try {
    			if(!destinatarios.isEmpty()) {
	    			emailUtil.enviarEmailHTMLPersonalizado(destinatarios, alerta.getTitulo(), eventoService.construirEmailPersonalizadoEvento(alerta.getDescricao(), evento).toString());
					alertaEventoService.definirEnviado(alerta);
    			}
			} catch (EmailException e) {
				e.printStackTrace();
			} 

    	}
    } 
  }