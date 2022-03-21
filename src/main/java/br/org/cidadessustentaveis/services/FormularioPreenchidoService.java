package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.FormularioPreenchidoDTO;
import br.org.cidadessustentaveis.dto.RespostaFormularioDTO;
import br.org.cidadessustentaveis.model.administracao.Perfil;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.participacaoCidada.Formulario;
import br.org.cidadessustentaveis.model.participacaoCidada.FormularioPreenchido;
import br.org.cidadessustentaveis.model.participacaoCidada.FormularioPreenchidoResposta;
import br.org.cidadessustentaveis.model.participacaoCidada.Pergunta;
import br.org.cidadessustentaveis.model.participacaoCidada.Resposta;
import br.org.cidadessustentaveis.repository.FormularioPreenchidoRepository;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;

@Service
public class FormularioPreenchidoService {
	@Autowired
	private FormularioService formularioService;
	@Autowired
	private PerguntaService perguntaService;
	@Autowired
	private RespostaService respostaService;
	@Autowired
	private FormularioPreenchidoRepository formularioPreenchidoRepository;
	@Autowired
	private UsuarioContextUtil usuarioContextUtil;
	@Autowired
	private UsuarioService usuarioService;

	public void salvar(String link, List<RespostaFormularioDTO> respostas) throws AuthenticationException, org.apache.tomcat.websocket.AuthenticationException {
		Usuario usuario = null;
		try {
			usuario = usuarioContextUtil.getUsuario();
		} catch (Exception e) {
		}
		Boolean estaLogado = usuario != null ? true : false;
		Formulario formulario = formularioService.buscarPorLink(link);
		if(formulario.getApenasAutenticados()) {
			if(!estaLogado) {
				throw new AuthenticationException("Você precisa estar logado para preencher este formulário.");
			}
		}

		FormularioPreenchido formularioPreenchido = new FormularioPreenchido(null, LocalDate.now(), LocalTime.now(), estaLogado, formulario, usuario, new ArrayList<>() );
		List<FormularioPreenchidoResposta> listaRespostas = configurarRespostas(respostas,formularioPreenchido);
		listaRespostas.forEach(r -> {
			r.setFormularioPreenchido(formularioPreenchido);
			formularioPreenchido.getRespostas().add(r);
		});
		formularioPreenchidoRepository.save(formularioPreenchido);
		
	}

	public List<FormularioPreenchidoResposta> configurarRespostas(List<RespostaFormularioDTO> respostas, FormularioPreenchido formularioPreenchido){
		List<FormularioPreenchidoResposta> listaRespostas = new ArrayList<>();
		for(RespostaFormularioDTO dto : respostas) {
			Long idPergunta = Long.valueOf(dto.getKey().replaceAll("-outro", ""));
			Pergunta pergunta = this.perguntaService.findById(idPergunta);
			List<String> valores = dto.getValores();
			for(String valor : valores) {
				if(valor == null) {
					break;
				}
				FormularioPreenchidoResposta formularioPreenchidoResposta = new FormularioPreenchidoResposta();
				formularioPreenchidoResposta.setPergunta(pergunta);
				switch (pergunta.getTipo()) {
				case "TextoLivre":
					formularioPreenchidoResposta.setTextoLivre(valor);
					break;
				case "SimNao":
					Boolean valorBool = Boolean.valueOf(valor);
					formularioPreenchidoResposta.setSimNao(valorBool);
					break;
				case "Multiplo":
					Long idResposta = Long.valueOf(valor);
					Resposta respostaEscolhida = this.respostaService.getById(idResposta);
					formularioPreenchidoResposta.setResposta(respostaEscolhida);
					break;
				case "MultiploOutro":
					if(!dto.getKey().contains("-outro")) {
						Long idResposta2 = Long.valueOf(valor);
						Resposta respostaEscolhida2 = this.respostaService.getById(idResposta2);
						formularioPreenchidoResposta.setResposta(respostaEscolhida2);
					} else {
						formularioPreenchidoResposta.setOutro(valor);
					}
					break;
				}
				listaRespostas.add(formularioPreenchidoResposta);
			}
		}

		return listaRespostas;
	}
	
	public List<FormularioPreenchidoDTO> exportarFormularioPreenchido(Long id) throws Exception{
		
	List<FormularioPreenchidoDTO> formularios = formularioPreenchidoRepository.findByFormularioId(id);

	if(formularios.isEmpty()) {
		return formularios;
	}
		
	Usuario usuarioLogado = usuarioContextUtil.getUsuario();
	
	Usuario usuarioCriadorFormulario = usuarioService.buscarPorId(formularios.get(0).getFormulario().getIdUsuarioCriador());
	
	formularios.forEach(formulario -> {
		if(formulario.getFormulario() != null && usuarioLogado.getPrefeitura() != null) {
			if(formulario.getFormulario().getIdPrefeituraDoUsuarioCriador() != usuarioLogado.getPrefeitura().getId()) {
				formulario.setUsuarioNome("Usuário");
			}		
		}
		else {
			if(!isNomeExisteEmPerfis(usuarioCriadorFormulario.getCredencial().getListaPerfil(), "Administrador")) {
				formulario.setUsuarioNome("Usuário");
			}
		}
		
	});
	
	return formularios;
}
	
	public Boolean isNomeExisteEmPerfis(List<Perfil> perfis, String nomePerfil) {
		
		List<Perfil> perfisEncontrados = perfis.stream().filter(perfil -> perfil.getNome().equals(nomePerfil)).collect(Collectors.toList());
		
		if(!perfisEncontrados.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public Boolean isFormAnsweredByUser(Long idUsuario, Long idFormulario) {
		return formularioPreenchidoRepository.findByUsuarioAndFormularioId(idUsuario, idFormulario).size() > 0;
	}

}
