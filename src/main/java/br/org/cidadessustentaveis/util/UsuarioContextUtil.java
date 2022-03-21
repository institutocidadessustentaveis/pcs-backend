package br.org.cidadessustentaveis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.services.UsuarioService;

@Service
public class UsuarioContextUtil {

	@Autowired 
	private UsuarioService usuarioService;

	public Usuario getUsuario() throws Exception {
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		if(user == "anonymousUser") {
			return null;
		} else {
			Usuario usuario  = usuarioService.buscarPorEmailCredencial(user);
			return usuario;
		}
		
		
	}
}
