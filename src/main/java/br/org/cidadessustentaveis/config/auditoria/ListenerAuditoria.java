package br.org.cidadessustentaveis.config.auditoria;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.org.cidadessustentaveis.config.SpringContext;
import br.org.cidadessustentaveis.dto.AtividadeGestorMunicipalDTO;
import br.org.cidadessustentaveis.dto.AtividadeUsuarioDTO;
import br.org.cidadessustentaveis.model.administracao.Perfil;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.enums.Modulo;
import br.org.cidadessustentaveis.model.enums.TipoAcaoAuditoria;
import br.org.cidadessustentaveis.model.sistema.HistoricoOperacao;
import br.org.cidadessustentaveis.services.AtividadeGestorMunicipalService;
import br.org.cidadessustentaveis.services.AtividadeUsuarioService;
import br.org.cidadessustentaveis.services.HistoricoOperacaoService;
import br.org.cidadessustentaveis.services.UsuarioService;

@Service

@Transactional(propagation=Propagation.REQUIRES_NEW)
public class ListenerAuditoria {
		
	@PostPersist
	public void aposInserir(Object object) throws IllegalArgumentException, IllegalAccessException {
		salvar(TipoAcaoAuditoria.CRIACAO, object);
		salvarAtividadeUsuario(TipoAcaoAuditoria.ALTERACAO, object);
		salvarAcaoGestorPublico(TipoAcaoAuditoria.ALTERACAO, object);
	}
	@PostRemove
	public void aposexcluir(Object object) throws IllegalArgumentException, IllegalAccessException {
		salvar(TipoAcaoAuditoria.REMOCAO, object);
		salvarAtividadeUsuario(TipoAcaoAuditoria.ALTERACAO, object);
		salvarAcaoGestorPublico(TipoAcaoAuditoria.ALTERACAO, object);
	}
	@PostUpdate
	public void aposAtualizar(Object object) throws Exception {  
		salvar(TipoAcaoAuditoria.ALTERACAO, object);
		salvarAtividadeUsuario(TipoAcaoAuditoria.ALTERACAO, object);
		salvarAcaoGestorPublico(TipoAcaoAuditoria.ALTERACAO, object);
	}

	public void salvar(TipoAcaoAuditoria tipoAcao, Object object) throws IllegalArgumentException, IllegalAccessException{
		
		Usuario usuario = null;
		String acao = getAcao(tipoAcao, object);
		Modulo modulo  = getModulo(object);
		try {
			usuario =  getUsuario();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 
	
		HistoricoOperacao historicoOperacao = new HistoricoOperacao(null , usuario, tipoAcao, LocalDateTime.now(), modulo, acao, null);

		ApplicationContext context = SpringContext.getAppContext();
		HistoricoOperacaoService historicoOperacaoService = (HistoricoOperacaoService)context.getBean("historicoOperacaoService");
		historicoOperacaoService.inserir(historicoOperacao);
	}
	
	public void salvarAcaoGestorPublico(TipoAcaoAuditoria tipoAcao, Object object) throws IllegalArgumentException, IllegalAccessException {
		Usuario usuario = null;
		String acao = getAcao(tipoAcao, object);
		try {
			usuario =  getUsuario();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		if(usuario == null || usuario.getCredencial() == null) {
			System.out.println("Usuario ou credencial nula");
			return;
		}
		boolean flagGestor = false;
		for (Perfil perfil : usuario.getCredencial().getListaPerfil()) {
			if (perfil.getGestaoPublica()) {
				flagGestor = true;
			}
		}
		if (flagGestor) {			
			AtividadeGestorMunicipalDTO atividadeGestorMunicipalDTO = new AtividadeGestorMunicipalDTO();
			ApplicationContext context = SpringContext.getAppContext();
			AtividadeGestorMunicipalService atividadeGestorMunicipalService = (AtividadeGestorMunicipalService)context.getBean("atividadeGestorMunicipalService");
			
			atividadeGestorMunicipalDTO.setAcao(acao);
			atividadeGestorMunicipalDTO.setDataHora(LocalDateTime.now());
			atividadeGestorMunicipalDTO.setUsuarioLogado(usuario.getNome());
			atividadeGestorMunicipalDTO.setNomeUsuario(usuario.getNome());
			if(usuario.getPrefeitura() != null) {
				atividadeGestorMunicipalDTO.setCidade(usuario.getPrefeitura().getCidade().getNome());
				atividadeGestorMunicipalDTO.setEstado(usuario.getPrefeitura().getCidade().getProvinciaEstado().getNome());
			}
			atividadeGestorMunicipalService.salvarAtividadeGestorMunicipal(atividadeGestorMunicipalDTO);
		}
	}
	
	public void salvarAtividadeUsuario(TipoAcaoAuditoria tipoAcao, Object object) throws IllegalArgumentException, IllegalAccessException {
		Usuario usuario = null;
		String acao = getAcao(tipoAcao, object);
		Modulo modulo  = getModulo(object);
		try {
			usuario =  getUsuario();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		AtividadeUsuarioDTO atividaUsuarioDTO = new AtividadeUsuarioDTO();
		ApplicationContext context = SpringContext.getAppContext();
		AtividadeUsuarioService atividadeUsuarioService = (AtividadeUsuarioService)context.getBean("atividadeUsuarioService");

		atividaUsuarioDTO.setAcao(acao);
		atividaUsuarioDTO.setDataHora(LocalDateTime.now());
		if(modulo != null) {
			atividaUsuarioDTO.setModulo(modulo.getDescricao());
		}
		if(usuario != null) {
			atividaUsuarioDTO.setNomeUsuario(usuario.getNome());
		}

		if(usuario != null) {
			atividaUsuarioDTO.setUsuarioLogado(usuario.getNome());
		}

		atividadeUsuarioService.salvarAtividadeUsuario(atividaUsuarioDTO);
	}
	
	public Long getId(Object object) throws IllegalArgumentException, IllegalAccessException {
		Long id = null;
		for (Field field : object.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			String name = field.getName();
			if(name.equals("id")) {
				Object value = field.get(object);
				id = (Long) value;
				break;
			}

		}
		return id;
	}

	private Usuario getUsuario() {
		ApplicationContext context = SpringContext.getAppContext();
		UsuarioService usuarioService = (UsuarioService)context.getBean("usuarioService");  
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmailCredencial(user);
		return usuario;
	}

	private String getAcao(TipoAcaoAuditoria tipo, Object object) throws IllegalArgumentException, IllegalAccessException {
		String acao = "";
		Long objId = getId(object);
		switch (tipo) {
			case CRIACAO:	
				acao ="Criou";
				break;
			case ALTERACAO:
				acao ="Alterou";				
				break;
			case REMOCAO:
				acao ="Removeu";				
				break;
			default:
				break;
		}
		
		/*if(object.getClass() != null && object.getClass().getSimpleName().equalsIgnoreCase("CartaCompromisso")) {
			
			String arquivo = null;
			for (Field field : object.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				String name = field.getName();
				if(name.equals("nomeArquivo")) {
					Object value = field.get(object);
					arquivo = (String) value;
					break;
				}
			}
			
			acao = acao +" "+object.getClass().getSimpleName()+", arquivo: " + arquivo +", id: "+objId.toString();
		} else {
			acao = acao +" "+object.getClass().getSimpleName()+", id: "+objId.toString();
	}*/
		
		acao = acao +" "+object.getClass().getSimpleName()+", id: "+objId.toString();
		
		return acao;
	}
	
	private Modulo getModulo(Object object){
		Modulo modulo = null; 
		String item = object.getClass().getPackage().getName();
		while(item.contains(".")) {
			int index = item.indexOf(".");
			String substring = item.substring(0, index+1);
			item = item.replace(substring, "");
		}
		
		switch (item) {
		case "administracao":
			modulo = Modulo.ADMINISTRACAO;
			break;
		case "indicadores":
			modulo = Modulo.INDICADORES;
			break;
		case "boaspraticas":
			modulo = Modulo.BOAS_PRATICAS;
			break;
		case "noticias":
			modulo = Modulo.NOTICIAS;
			break;
		case "institucional":
			modulo = Modulo.INSTITUCIONAL;
			break;
		case "planjementoIntegrado":
			modulo = Modulo.PLANEJAMENTO_INTEGRADO;
			break;
		case "eventos":
			modulo = Modulo.EVENTOS;
			break;
		case "participacaoCidada":
			modulo = Modulo.PARTICIPACAO_CIDADA;
			break;
		default:
			modulo = null;
			break;
		}

		return modulo;
	}
	
}

