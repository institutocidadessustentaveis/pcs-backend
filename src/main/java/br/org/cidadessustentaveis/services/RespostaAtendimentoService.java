package br.org.cidadessustentaveis.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.RespostaAtendimentoDTO;
import br.org.cidadessustentaveis.model.administracao.FormularioAtendimento;
import br.org.cidadessustentaveis.model.administracao.RespostaAtendimento;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.repository.RespostaAtendimentoRepository;
import br.org.cidadessustentaveis.util.EmailUtil;

@Service
public class RespostaAtendimentoService {
	
	@Autowired
	private RespostaAtendimentoRepository repository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private FormularioAtendimentoService formularioAtendimentoService;
	
	@Autowired
	private EmailUtil emailUtil;
	
	public RespostaAtendimento salvar(RespostaAtendimentoDTO respostaDTO) {
		FormularioAtendimento formulario = formularioAtendimentoService.buscarPorId(respostaDTO.getIdFormularioAtendimento());
		Usuario usuario = usuarioService.buscarPorId(respostaDTO.getIdUsuario());
		
		try {
			this.enviarRespostaPorEmail(formulario.getEmailContato(), respostaDTO.getResposta());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		RespostaAtendimento objToSave = new RespostaAtendimento();
		objToSave.setResposta(respostaDTO.getResposta());
		objToSave.setFormularioAtendimento(formulario);
		objToSave.setUsuario(usuario);
		objToSave.setDataHora(LocalDateTime.now());
		
		return repository.save(objToSave);
	}
	
	public void enviarRespostaPorEmail(String email, String resposta) throws EmailException {
		

		List<String> destinatario = new ArrayList<>();
		destinatario.add(email);
		
		String assunto = "Resposta de sua solicitação de atendimento";
		String mensagem = resposta;
		
		emailUtil.enviarEmailHTML(destinatario, assunto, mensagem);	
	}

	public RespostaAtendimentoDTO buscarRespostaAtendimentoPorIdFormulario(Long id) {
		return repository.buscarRespostaAtendimentoPorIdFormulario(id);
	}
}
