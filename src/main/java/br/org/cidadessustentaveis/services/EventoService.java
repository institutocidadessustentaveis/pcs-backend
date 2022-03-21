package br.org.cidadessustentaveis.services;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.config.SpringContext;
import br.org.cidadessustentaveis.dto.EventoDTO;
import br.org.cidadessustentaveis.dto.EventoDetalheDTO;
import br.org.cidadessustentaveis.dto.EventoMapaDTO;
import br.org.cidadessustentaveis.dto.EventosFiltradosDTO;
import br.org.cidadessustentaveis.dto.UsuarioDTO;
import br.org.cidadessustentaveis.model.administracao.Alerta;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.enums.TipoAlerta;
import br.org.cidadessustentaveis.model.eventos.AlertaEvento;
import br.org.cidadessustentaveis.model.eventos.Evento;
import br.org.cidadessustentaveis.repository.EventoRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.EmailUtil;
import br.org.cidadessustentaveis.util.ProfileUtil;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;

@Service
public class EventoService {
	
	@Autowired
	private ProfileUtil profileUtil;
	
	@Autowired
	private EventoRepository eventoRepository;
	
	@Autowired
	private AlertaEventoService alertaEventoService;
	
	@Autowired
	private AlertaService alertaService;
	
	@Autowired 
	private EixoService eixoService;
	
	@Autowired
	ObjetivoDesenvolvimentoSustentavelService odsService;
	
	@Autowired
	private PaisService paisService;
	
	@Autowired
	private CidadeService cidadeService;
	
	@Autowired
	ProvinciaEstadoService provinciaEstadoService;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	AreaInteresseService areaInteresseService;
	
	@Autowired
	NoticiaService noticiaService;
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private UsuarioContextUtil usuarioUtil;
	
	public Evento inserir(EventoDTO eventoDTO) throws Exception {

		Evento evento = eventoDTO.toEntityInsert(eventoDTO);
			
		Cidade cidadeEvento = cidadeService.buscarPorId(eventoDTO.getCidade());
		
		evento.setPais(eventoDTO.getPais() != null ? paisService.buscarPorId(eventoDTO.getPais()) : null);
		evento.setCidade(eventoDTO.getCidade() != null ? cidadeService.buscarPorId(eventoDTO.getCidade()) : null);
		evento.setTemas(eventoDTO.getTemas() != null && !eventoDTO.getTemas().isEmpty()? areaInteresseService.buscarAreasPorIds(eventoDTO.getTemas()) : null);
		evento.setEixos(eventoDTO.getEixos() != null && !eventoDTO.getEixos().isEmpty()? eixoService.buscarEixosPorIds(eventoDTO.getEixos()) : null);
		evento.setOds(eventoDTO.getOds() != null ? odsService.listarPorId(eventoDTO.getOds()) : null);
		evento.setProvinciaEstado(eventoDTO.getEstado() != null ? provinciaEstadoService.buscarPorId(eventoDTO.getEstado()) : null );
		evento.setUsuarioCadastro(buscarUsuarioLogado());
		evento.setNoticiasRelacionadas(eventoDTO.getNoticiasRelacionadas() != null && !eventoDTO.getNoticiasRelacionadas().isEmpty()? noticiaService.buscarNoticiasPorId(eventoDTO.getNoticiasRelacionadas()) : null);
		List<String> destinatarios = new ArrayList<String>();		
		if (evento.getTipo().equals("Capacitação para Prefeituras Signatárias") ) {
			List<UsuarioDTO> usuariosRecebemEmail  = usuarioService.findUsuariosRecebeEmailTrue();
			for (int i = 0; i < usuariosRecebemEmail.size(); i++) {
				if(usuariosRecebemEmail.get(i).getCidadeInteresse() != null && usuariosRecebemEmail.get(i).getCidadeInteresse().equals(cidadeEvento.getId().toString())) {
					destinatarios.add(usuariosRecebemEmail.get(i).getEmail());
				}
			}
		}
		List<AreaInteresse> areasInteresseEventoAlerta = evento.getTemas() != null ? evento.getTemas(): null;
		eventoRepository.save(evento);
		alertaService.salvar(Alerta.builder()
				.mensagem("Novo Evento: "+eventoDTO.getNome())
					.link("/eventos/detalhe/"+evento.getId())
					.tipoAlerta(TipoAlerta.EVENTO)
					.data(LocalDateTime.now())
					.cidade(cidadeEvento)
					.areasInteresse(areasInteresseEventoAlerta)
					.build());
		if (destinatarios.size() > 0) {
	    	ApplicationContext appContext = SpringContext.getAppContext();
	    	EmailUtil emailUtil = (EmailUtil)appContext.getBean("emailUtil");
			try {
				eventoDTO.setCidadeNome(evento.getCidade().getNome());
				eventoDTO.setEstadoNome(evento.getProvinciaEstado().getNome());
				eventoDTO.setPaisNome(evento.getPais().getNome());
				emailUtil.enviarEmailHTMLPersonalizado(destinatarios, "Novo Evento de Capacitação para Prefeituras Signatárias", construirEmailPersonalizadoEvento(eventoDTO.getDescricao(), eventoDTO).toString());
			} catch (EmailException e) {
				e.printStackTrace();
			}			
		}
		return evento;
	}
	
	public Evento buscarPorId(Long id) {
		Optional<Evento> obj = eventoRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Evento não encontrado!"));
	}
	
	public EventoDTO buscarEventoPorId(Long id) throws Exception {
		Evento eventoRef = buscarPorId(id);
		if (eventoEhDoUsuarioLogado(eventoRef) == true || usuarioLogadoEhAdmin() ) {			
			return new EventoDTO(eventoRef);
		}
		throw new Exception("Você não tem permissão para acessar este recurso.");
	}
	
	public EventoDetalheDTO buscarEventoDetalhePorId(Long id) throws Exception {
		Evento eventoRef = buscarPorId(id);
		if (eventoEhDoUsuarioLogado(eventoRef) == true || usuarioLogadoEhAdmin() ) {			
			return new EventoDetalheDTO(eventoRef);
		}
		throw new Exception("Você não tem permissão para acessar este recurso.");
	}
	
	public boolean eventoEhDoUsuarioLogado(Evento evento) throws Exception {
			Usuario usuarioLogado = buscarUsuarioLogado();
			if (evento.getUsuarioCadastro().getId().equals(usuarioLogado.getId()) ) {
				return true;
			}
			else {
				return false;
			}
	}
	
	public List<EventoDTO> buscarEventosToList() throws Exception {
		if (usuarioLogadoEhAdmin()) {
			return buscarEventosToListAdmin();
		}
		else {			
			return buscarEventosToListPrefeitura();
		} 
	}
	
	private boolean usuarioLogadoEhAdmin() throws Exception {
		Usuario usuarioLogado = buscarUsuarioLogado();
		for (int i = 0; i < usuarioLogado.getCredencial().getListaPerfil().size(); i++) {
			if (usuarioLogado.getCredencial().getListaPerfil().get(i).getNome().equals("Administrador")) {
				return true;
			}
		}
		return false;
	}
	
	public List<EventoDTO> buscarEventosToListAdmin() {
		return eventoRepository.buscarEventosToList();
	}
	
	public List<EventoDTO> buscarEventosToListPrefeitura() throws Exception {
		Cidade cidade = buscarCidadeUsuarioLogado();
		if(cidade != null) {
			return eventoRepository.buscarEventosToListByIdCidade(cidade.getId());
		}		
		
		else {
			throw new Exception("Você não tem permissão para acessar este recurso.");
		}
	}
	
	public Usuario buscarUsuarioLogado() throws Exception {
		Usuario usuario = usuarioUtil.getUsuario();
		if (usuario == null) {
			throw new Exception("Usuário não encontrado");
		}
		return usuario;
	}
	
	public Cidade buscarCidadeUsuarioLogado() throws Exception {
		Usuario usuario = buscarUsuarioLogado();
		if (usuario.getPrefeitura().getCidade() != null) {
			return usuario.getPrefeitura().getCidade();
		}
		else {
			return null;
		}
	}
	
	public List<EventoDTO> buscarEventosToListByIdCidade(Long idCidade) {
		return eventoRepository.buscarEventosToListByIdCidade(idCidade);
	}
	
	public List<EventoDTO> buscarEventosParticipacaoCidada() {
		return eventoRepository.buscarEventosParticipacaoCidada();
	}
	 
	public void deletar(Long id) {
		List<AlertaEvento> alertas = alertaEventoService.findByEventoId(id);
		
		if(!alertas.isEmpty()) {
			alertas.forEach(alerta -> alertaEventoService.excluir(alerta.getId()));
		}
		
		eventoRepository.deleteById(id);
	}
	
	public Evento alterar( EventoDTO eventoDTO) throws Exception {
		
		if (eventoDTO.getId() == null) {
			throw new Exception("Campo id divergente.");
		}
		
		Evento evento = eventoDTO.toEntityUpdate(buscarPorId(eventoDTO.getId()));
    		
		evento.setPais(eventoDTO.getPais() != null ? paisService.buscarPorId(eventoDTO.getPais()) : null);
		evento.setCidade(eventoDTO.getCidade() != null ? cidadeService.buscarPorId(eventoDTO.getCidade()) : null);
		evento.setTemas(eventoDTO.getTemas() != null && !eventoDTO.getTemas().isEmpty()? areaInteresseService.buscarAreasPorIds(eventoDTO.getTemas()) : null);
		evento.setNoticiasRelacionadas(eventoDTO.getNoticiasRelacionadas() != null && !eventoDTO.getNoticiasRelacionadas().isEmpty()? noticiaService.buscarNoticiasPorId(eventoDTO.getNoticiasRelacionadas()) : null);
		evento.setOds(eventoDTO.getOds() != null ? odsService.listarPorId(eventoDTO.getOds()) : null);
		evento.setProvinciaEstado(eventoDTO.getEstado() != null ? provinciaEstadoService.buscarPorId(eventoDTO.getEstado()) : null );
		evento.setEixos(eventoDTO.getEixos() != null && !eventoDTO.getEixos().isEmpty()? eixoService.buscarEixosPorIds(eventoDTO.getEixos()) : null);
		eventoRepository.save(evento);
			
		return evento;
		
	}

	public List<EventoMapaDTO> buscarProximosEventosOficiais() {
		List<EventoMapaDTO> eventos = eventoRepository.findEventosOficiaisApartir(LocalDate.now());
		return eventos;
	}
	
	public List<EventosFiltradosDTO> buscarEventosFiltrados(String tipo, String dataInicial, String dataFinal, String palavraChave) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<EventosFiltradosDTO> query = cb.createQuery(EventosFiltradosDTO.class);
		
		Root<Evento> evento = query.from(Evento.class);
		
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		query.multiselect(evento.get("id"), evento.get("dataEvento"), evento.get("tipo"), evento.get("nome"), evento.get("descricao"), 
				evento.get("organizador"), evento.get("latitude"), evento.get("longitude"), evento.get("publicado"), evento.get("horario"), evento.get("site"), evento.get("endereco"), evento.get("linkExterno"));
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
				
		if (tipo != null && !tipo.equals("")) {
			Path<String> tipoEvento = evento.get("tipo");
			predicateList.add(cb.equal(tipoEvento, tipo));
		}
		else {
			Path<String> tipoEvento = evento.get("tipo");
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, evento.get("dataEvento"));
			Predicate predicateForDataAtual = cb.greaterThanOrEqualTo(campoDataHora, LocalDate.now());
			Predicate predicateForTipo = cb.notEqual(tipoEvento, "Prefeitura");
			predicateList.add(cb.and(predicateForDataAtual, predicateForTipo));
		}
		
		if(dataInicial != null && !dataInicial.equals("")) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, evento.get("dataEvento"));
			LocalDate dataInicialFormatada = LocalDate.parse(dataInicial, df);
			Predicate predicateForData = cb.greaterThanOrEqualTo(campoDataHora, dataInicialFormatada);
			Predicate predicateForDataAtual = cb.greaterThanOrEqualTo(campoDataHora, LocalDate.now());
			predicateList.add(cb.and(predicateForData, predicateForDataAtual));
		}
		else {
			Path<String> tipoEvento = evento.get("tipo");
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, evento.get("dataEvento"));
			Predicate predicateForDataAtual = cb.greaterThanOrEqualTo(campoDataHora, LocalDate.now());
			Predicate predicateForTipo = cb.notEqual(tipoEvento, "Prefeitura");
			predicateList.add(cb.and(predicateForDataAtual, predicateForTipo));
		}
		
		if(dataFinal != null && !dataFinal.equals("")) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, evento.get("dataEvento"));
			LocalDate dataFinalFormatada = LocalDate.parse(dataFinal, df);
			predicateList.add(cb.lessThanOrEqualTo(campoDataHora, dataFinalFormatada));
		}
		
		if (palavraChave != null && !palavraChave.equals("")) {
			Path<String> tituloEvento = evento.get("nome");
			Path<String> descricaoEvento = evento.get("descricao");
			Predicate predicateForTitulo = cb.like(cb.lower(tituloEvento), "%" + palavraChave.toLowerCase() + "%");
			Predicate predicateForEvento = cb.like(cb.lower(descricaoEvento), "%" + palavraChave.toLowerCase() + "%");
			predicateList.add(cb.or(predicateForTitulo, predicateForEvento));
		}		
		
		query.orderBy(cb.asc(evento.get("dataEvento")));
				

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<EventosFiltradosDTO> typedQuery = em.createQuery(query);
		List<EventosFiltradosDTO> lista = typedQuery.getResultList();

		return lista;
	}
	
	public List<EventosFiltradosDTO> buscarEventosNaoPrefeitura(){
		return this.eventoRepository.findAllNotPrefeitura();
	}

	public List<Long> getTemasByEventoId(Long id) {
		return this.eventoRepository.findTemasById(id);
	}
	
	public List<EventosFiltradosDTO> buscarEventosParticipacaoCidadaFiltrados(String dataInicial, String dataFinal) {

		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// Validação para não quebrar a formatação de data sem valor
		// Foi preciso validar "Invalid date" pois o moment retorna esse valor em alguns casos
		if (dataInicial.equals("Invalid date") || dataInicial.equals("")) {
			dataInicial = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
		}

		LocalDate dataInicialFormatada = LocalDate.parse(dataInicial, df);
		LocalDate dataAtual = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		// Verifica se a data informada pelo usuario é anterior que a data atual, pois
		// não pode retornar eventos anteriores
		if (dataInicialFormatada.isBefore(dataAtual)) {
			dataInicialFormatada = dataAtual;
		}

		// Define qual Repository com Query personalizada irá chamar
		if (dataFinal.equals("") || dataFinal.equals("Invalid date")) {
			return eventoRepository.buscarEventosParticipacaoCidadaSemDataFinal(dataInicialFormatada);
		} else {
			LocalDate dataFinalFormatada = LocalDate.parse(dataFinal, df);
			return eventoRepository.buscarEventosParticipacaoCidadaComDataFinal(dataInicialFormatada, dataFinalFormatada);
		}
	}
	
	public StringBuffer construirEmailPersonalizadoEvento(String descricao, EventoDTO evento) {
		String urlAPI = profileUtil.getProperty("profile.api");
		StringBuffer msg = new StringBuffer();

	        		
			        	msg.append("<p>Olá,</p>");
			        	msg.append(descricao);
			        	msg.append("<p>Confira abaixo as informações sobre o evento desejado: </p>");
			        	msg.append("<br>");
			        	
			        	msg.append("<table cellpadding='10' style='margin-left: 55px'>");
			        		msg.append("<tr>");
			        			msg.append("<td> <img src='" + urlAPI + "/arquivo/imagem/999987' align='center'> </td>");
			        			msg.append("<td> <h4 style='margin: 0px'>Tipo de Evento</h4> <small>" + evento.getTipo() + "</small> </td>");
			        		msg.append("</tr>");	
			        		
			        		
			        		msg.append("<tr>");
			        			msg.append("<td> <img src='" + urlAPI + "/arquivo/imagem/999988' align='center'> </td>");
			        			msg.append("<td> <h4 style='margin-bottom: 0px'>Nome do Evento</h4> <small>" + evento.getNome() + "</small> </td>");
		        			msg.append("</tr>");
		        			

		        			
		        			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d/MM/uuuu");
			        		String dataFormatada = evento.getData().format(formatters);
			
			        		String result = LocalTime.parse(evento.getHorario().toString(), DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("hh:mm a")); 
			        		
			        		msg.append("<tr>");
			        			msg.append("<td> <img src='" + urlAPI + "/arquivo/imagem/999989' align='center'> </td>");
			        			msg.append("<td> <h4 style='margin-bottom: 0px'>" + dataFormatada + "</h4> <small>" + result + "</small> </td>");
		        			msg.append("</tr>");    
		        			

			        		
			        		msg.append("<tr>");
			        			msg.append("<td> <img src='" + urlAPI + "/arquivo/imagem/999990' align='center'> </td>");
			        			msg.append("<td> <h4 style='margin-bottom: 0px'>Organizador do evento</h4> <small>" + evento.getOrganizador() + "</small> </td>");
		        			msg.append("</tr>");
		        			

		        			if(evento.getTemasNomes() != null && !evento.getTemasNomes().isEmpty()) {
			        			String temasNome = "";
			        			for(String temaNome : evento.getTemasNomes()) {
			        				if(temasNome.equals("")) {
			        				 temasNome += temaNome;
			        				} else {
			        					temasNome += "," + temaNome;
			        				}
			        			}
			        			msg.append("<tr>");
			        				msg.append("<td> <img src='" + urlAPI + "/arquivo/imagem/999991' align='center'> </td>");
			        				msg.append("<td> <h4 style='margin-bottom: 0px'>Tema do evento</h4> <small>" + temasNome + "</small> </td>");
			        			msg.append("</tr>");
		        			}  			

		        			String enderecoExibicao = evento.getEndereco() + ", "+ evento.getCidadeNome() +", "+ evento.getEstadoNome()+ ", "+ evento.getPaisNome();
		        			
		        			if (evento.getLatitude() != null && evento.getLongitude() != null) {	
		        				msg.append("<tr>");
				        			msg.append("<td> <img src='" + urlAPI + "/arquivo/imagem/999992' align='center'> </td>");
				        			msg.append("<td> <h4 style='margin-bottom: 0px'>" + enderecoExibicao + "</h4> <small> <strong>Latitude: </strong>" + evento.getLatitude() + " <strong>Longitude: </strong>" + evento.getLongitude() + "</small></td>");
			        			msg.append("</tr>");
	        				}
	        				else {
	        					msg.append("<tr>");
				        			msg.append("<td> <img src='" + urlAPI + "/arquivo/imagem/999992' align='center'> </td>");
				        			msg.append("<td> <h4 style='margin-bottom: 0px'>Endereço</h4> <small>" + enderecoExibicao + "</small> </td>");
			        			msg.append("</tr>");
	        				}
		        			

		        			
		        			if(evento.getLinkExterno() != null) {			        		
				        		msg.append("<tr>");
				        			msg.append("<td> <img src='" + urlAPI + "/arquivo/imagem/999993' align='center'> </td>");
				        			msg.append("<td> <h4 style='margin-bottom: 0px'>Link de acesso ao evento</h4> <small>" + evento.getLinkExterno() + "</small> </td>");
			        			msg.append("</tr>");
		        			}		        			
		        			
			        	msg.append("</table>");
     
		return msg;
	}

	public List<EventoDTO> buscarEventosCapacitacao() {
		return eventoRepository.buscarEventosCapacitacao(LocalDate.now());
	}
	
	public List<EventoDTO> buscarTodosEventosCapacitacao() {
		return eventoRepository.buscarTodosEventosCapacitacao();
	}
	
	public List<EventoDTO> buscarEventosTipoAcademiaCalendario() {
		return eventoRepository.buscarEventosTipoAcademiaCalendario();
	}	
	
	public List<EventosFiltradosDTO> buscarEventosCapacitacaoFiltrados(String palavraChave, String dataInicial, String dataFinal) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<EventosFiltradosDTO> query = cb.createQuery(EventosFiltradosDTO.class);
		
		Root<Evento> evento = query.from(Evento.class);
		
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		query.multiselect(evento.get("id"), evento.get("dataEvento"), evento.get("tipo"), evento.get("nome"), evento.get("descricao"), 
				evento.get("organizador"), evento.get("latitude"), evento.get("longitude"), evento.get("publicado"), evento.get("horario"), evento.get("site"), evento.get("endereco"), evento.get("linkExterno"));
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		Boolean verificado = false;
			
		Predicate predicateForTipo = cb.equal(evento.get("tipo"), "Capacitação para Prefeituras Signatárias");
		Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, evento.get("dataEvento"));
		Predicate predicateForDataAtual = cb.greaterThanOrEqualTo(campoDataHora, LocalDate.now());
		
		if(dataInicial != null && !dataInicial.equals("")) {
			LocalDate dataInicialFormatada = LocalDate.parse(dataInicial, df);
			Predicate predicateForData = cb.greaterThanOrEqualTo(campoDataHora, dataInicialFormatada);
			predicateList.add(cb.and(predicateForData, predicateForDataAtual, predicateForTipo));
			verificado = true;
		}
		
		if(dataFinal != null && !dataFinal.equals("")) {
			LocalDate dataFinalFormatada = LocalDate.parse(dataFinal, df);
			Predicate predicateForData = cb.lessThanOrEqualTo(campoDataHora, dataFinalFormatada);
			predicateList.add(cb.and(predicateForData, predicateForDataAtual, predicateForTipo));
			verificado = true;
		}

		if (palavraChave != null && !palavraChave.equals("")) {
			Path<String> tituloEvento = evento.get("nome");
			Path<String> descricaoEvento = evento.get("descricao");
			Predicate predicateForTitulo = cb.like(cb.lower(tituloEvento), "%" + palavraChave.toLowerCase() + "%");
			Predicate predicateForEvento = cb.like(cb.lower(descricaoEvento), "%" + palavraChave.toLowerCase() + "%");
			Predicate predicateForPalavraChave = cb.or(predicateForTitulo, predicateForEvento);
			predicateList.add(cb.and(predicateForPalavraChave, predicateForTipo, predicateForDataAtual));
			verificado = true;
		}		
		
		if(!verificado) {
			predicateList.add(cb.and(predicateForTipo, predicateForDataAtual));
		}

		query.orderBy(cb.asc(evento.get("dataEvento")));
				

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<EventosFiltradosDTO> typedQuery = em.createQuery(query);
		List<EventosFiltradosDTO> lista = typedQuery.getResultList();

		return lista;
	}
	


	public List<EventoDTO> buscarEventosFiltradosPorNomeData(EventoDTO filtro) throws Exception{

		CriteriaBuilder cb = em.getCriteriaBuilder();
	
		CriteriaQuery<EventoDTO> query = cb.createQuery(EventoDTO.class);
			
		Root<Evento> evento = query.from(Evento.class);
	
		query.multiselect(
				evento.get("id"), 
				evento.get("dataEvento"), 
				evento.get("tipo"), 
				evento.get("nome"), 
				evento.get("descricao"), 
				evento.get("organizador"), 
				evento.get("latitude"), 
				evento.get("longitude"), 
				evento.get("publicado"), 
				evento.get("horario"), 
				evento.get("site"), 
				evento.get("endereco"), 
				evento.get("linkExterno"));
	
		List<Predicate> predicateList = new ArrayList<>();
		
		if(filtro.getNome() != null && !filtro.getNome().isEmpty()) {
			Path<String> campoNome = evento.get("nome");
			Predicate predicateForNome = cb.like(cb.lower(campoNome), "%" + filtro.getNome().toLowerCase() + "%");
			predicateList.add(cb.and(predicateForNome));
		}
	
		if(filtro.getDataInicio() != null) {
			Expression<LocalDate> campoData = cb.function("date", LocalDate.class, evento.get("dataEvento"));
			predicateList.add(cb.greaterThanOrEqualTo(campoData, filtro.getDataInicio()));
		}
	
		if(filtro.getDataFim() != null) {
			Expression<LocalDate> campoData = cb.function("date", LocalDate.class, evento.get("dataEvento"));
			predicateList.add(cb.lessThanOrEqualTo(campoData, filtro.getDataFim()));
		}
		
		if (usuarioLogadoEhAdmin() == false) {
			Expression<Usuario> campoUsuarioCadastro = evento.get("usuarioCadastro");
			predicateList.add(cb.equal(campoUsuarioCadastro, buscarUsuarioLogado().getId()));
		}
		
		Expression<Long> campoUsuarioCadastro = evento.get("usuarioCadastro").get("id");
		predicateList.add(cb.gt(campoUsuarioCadastro, 0));
	
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(evento.get("dataEvento")));
	
		query.orderBy(orderList);
	
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
	
		List<EventoDTO> lista = em.createQuery(query).getResultList();
		return lista;
	}

}
