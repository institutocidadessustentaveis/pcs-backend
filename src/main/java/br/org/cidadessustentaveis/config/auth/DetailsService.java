package br.org.cidadessustentaveis.config.auth;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.org.cidadessustentaveis.model.administracao.Credencial;
import br.org.cidadessustentaveis.model.administracao.Perfil;
import br.org.cidadessustentaveis.model.administracao.Permissao;
import br.org.cidadessustentaveis.model.enums.TipoAcaoAuditoria;
import br.org.cidadessustentaveis.model.sistema.HistoricoOperacao;
import br.org.cidadessustentaveis.repository.CredencialRepository;
import br.org.cidadessustentaveis.services.HistoricoOperacaoService;
import br.org.cidadessustentaveis.services.HistoricoSessaoUsuarioService;

@Service
public class DetailsService implements UserDetailsService{
	
	private Logger logger = LoggerFactory.getLogger(DetailsService.class);

	@Autowired
	private CredencialRepository repository;
	@Autowired
	private HistoricoSessaoUsuarioService historicoSessaoUsuarioService;
	@Autowired
	private HistoricoOperacaoService historicoOperacaoService;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		login = login.toLowerCase();
		Credencial credencial = repository.findByLoginIgnoreCaseAndSnExcluido(login, false);
		
		if (credencial == null) {
			logger.error("Erro em loadUserByUsername(): Usuário'"+login+"' não existe");
			throw new UsernameNotFoundException("Usuário '"+login+"' não existe!");
		}
		List<GrantedAuthority> authorities = new ArrayList<>();
		for(Perfil perfil: credencial.getListaPerfil()) {
			for (Permissao permissao : perfil.getPermissoes()) {
				if (permissao.getHabilitada()) {
					authorities.add(new SimpleGrantedAuthority(permissao.calculaRegraDePermissaoHabilitada()));
				}
			}
		}
		
		
		boolean enabled = false;
		if (Boolean.FALSE.equals(credencial.getSnExcluido())) {
			enabled = true;
		}
		
		boolean accountNonLocked = false;
		if (Boolean.FALSE.equals(credencial.getSnBloqueado())) {
			accountNonLocked = true;
		}
		
		
		historicoSessaoUsuarioService.registrarLogin(credencial.getUsuario());

		HistoricoOperacao historicoOperacao = new HistoricoOperacao(null , credencial.getUsuario(), TipoAcaoAuditoria.LOGIN, LocalDateTime.now(), null, "Realizou Acesso", null);
		historicoOperacaoService.inserir(historicoOperacao);
		return new User(credencial.getLogin(), credencial.getSenha(), enabled, true, true, accountNonLocked, authorities);
	}
	
	@Transactional(readOnly=true)
	public Credencial findByLogin(String login) {
		return repository.findByLoginIgnoreCaseAndSnExcluido(login,false);
	}
	

}
