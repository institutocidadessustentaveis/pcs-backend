package br.org.cidadessustentaveis.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.administracao.AprovacaoPrefeitura;
import br.org.cidadessustentaveis.model.administracao.EmailToken;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.enums.FuncionalidadeToken;
import br.org.cidadessustentaveis.repository.EmailTokenRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class EmailTokenService {
	@Autowired
	private EmailTokenRepository emailTokenRepository;
	
	@Autowired
	private AprovacaoPrefeituraService aprovacaoPrefeituraService;
	
	public void salvar(EmailToken emailToken) {
		emailTokenRepository.save(emailToken);
	}
	
	public EmailToken getByID(Long id) {
		Optional<EmailToken> obj = emailTokenRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Token não encontrado!"));
	}
	
	public EmailToken getByHash(String hash){
		Optional<EmailToken> obj = emailTokenRepository.findByHash(hash);
		EmailToken emailToken = obj.orElseThrow(() -> new ObjectNotFoundException("Token não encontrado!"));
		if(!emailToken.getAtivo()) {
			throw new ObjectNotFoundException("O link não é mais valido. Por favor, entre em contato com o administrador.");
		}
		return emailToken;
	}

	public void inativarTokenAprovacaoPrefeituraPrefeitura(Prefeitura prefeitura) {
		EmailToken emailToken = emailTokenRepository.findByAprovacaoPrefeituraPrefeitura(prefeitura);
		emailToken.setAtivo(Boolean.FALSE);
		salvar(emailToken);		
	}

	public void inativarTokenUsuarioPrefeitura(Usuario usuario) {
		EmailToken emailToken = emailTokenRepository.findByUsuarioPrefeituraAndUsuario(usuario.getPrefeitura() ,usuario);
		emailToken.setAtivo(Boolean.FALSE);
		salvar(emailToken);		
	}
	
	public void inativarTokenUsuarioSenha(Usuario usuario) {
		List<EmailToken> tokens = emailTokenRepository.findByUsuarioAndFuncionalidadeTokenAndAtivo(usuario,FuncionalidadeToken.RECUPERACAO_SENHA,Boolean.TRUE);
		for(EmailToken emailToken : tokens) {
			emailToken.setAtivo(Boolean.FALSE);
			salvar(emailToken);					
		}
	}
	
	public EmailToken reenviarEmailPrefeitura(Prefeitura prefeitura, FuncionalidadeToken funcionalidade, boolean ativo) {
		EmailToken emailToken = emailTokenRepository.findByAprovacaoPrefeituraPrefeitura(prefeitura);
		if(emailToken != null) {
			if(emailToken.getFuncionalidadeToken() == funcionalidade && emailToken.getAtivo() == true) {
				return emailToken;
			}
		}
		return null;
	}
	
	public Boolean isAtivoByIdAprovacaoPrefeitura(Long id) {
		AprovacaoPrefeitura aprovacaoPrefeitura = aprovacaoPrefeituraService.buscarPorId(id);
		EmailToken emailToken = emailTokenRepository.findByAprovacaoPrefeitura(aprovacaoPrefeitura);
		if (emailToken.getAtivo()) {
			return true;
		}
		return false;
	}
	
	public EmailToken buscarPorPrefeitura(Prefeitura prefeitura) {
		EmailToken emailToken = emailTokenRepository.findByAprovacaoPrefeituraPrefeitura(prefeitura);
		return emailToken;
	}
}
