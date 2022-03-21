package br.org.cidadessustentaveis.resources;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.CidadeDetalheDTO;
import br.org.cidadessustentaveis.dto.VariavelDTO;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.enums.TipoAcaoAuditoria;
import br.org.cidadessustentaveis.model.sistema.HistoricoOperacao;
import br.org.cidadessustentaveis.services.HistoricoOperacaoService;
import br.org.cidadessustentaveis.services.HistoricoSessaoUsuarioService;
import br.org.cidadessustentaveis.services.UsuarioService;
import springfox.documentation.annotations.ApiIgnore;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/sessao")
public class SessaoResource {
	
	@Autowired
	private HistoricoSessaoUsuarioService historicoSessaoUsuarioService;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private HistoricoOperacaoService historicoOperacaoService;
	@Autowired
	private UsuarioContextUtil usuarioContextUtil;
	


	
	@GetMapping(value = "/logout")
	public ResponseEntity<List<VariavelDTO>> listar(Principal principal) {
		if (principal == null) {
			throw new UsernameNotFoundException("Usuário não existe!");
		}
		
		historicoSessaoUsuarioService.registrarLogout(principal.getName());
		Usuario usuario = usuarioService.buscarPorEmailCredencial(principal.getName());
		HistoricoOperacao historicoOperacao = new HistoricoOperacao(null , usuario, TipoAcaoAuditoria.LOGOUT, LocalDateTime.now(), null, "Saiu do sistema", null);
		historicoOperacaoService.inserir(historicoOperacao);
		return null;
	}

	
	@GetMapping(value = "/cidade")
	public ResponseEntity<CidadeDetalheDTO> cidade() throws Exception {
		Usuario usuario = usuarioContextUtil.getUsuario();
		CidadeDetalheDTO dto = new CidadeDetalheDTO(usuario.getPrefeitura().getCidade());
		return ResponseEntity.ok(dto);
		
	}

	
}
