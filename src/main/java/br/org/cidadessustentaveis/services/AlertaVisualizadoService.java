package br.org.cidadessustentaveis.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.administracao.Alerta;
import br.org.cidadessustentaveis.model.administracao.AlertaVisualizado;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.repository.AlertaVisualizadoRepository;

@Service
public class AlertaVisualizadoService {

	@Autowired
	private AlertaVisualizadoRepository alertaVisualizadoRepository;
	@Autowired
	private UsuarioService usuarioService;
	public void salvar(Alerta alerta, String emailUsuario) {		
		Usuario usuario = usuarioService.buscarPorEmailCredencial(emailUsuario);
		AlertaVisualizado alertaVisualizado = AlertaVisualizado.builder().alerta(alerta).usuario(usuario).data(LocalDateTime.now()).build();
		alertaVisualizadoRepository.save(alertaVisualizado);
	}
	
	public List<AlertaVisualizado> buscarAlertas(String emailUsuario) {
		Usuario usuario = usuarioService.buscarPorEmailCredencial(emailUsuario);
		List<AlertaVisualizado> alertasVisualizados = alertaVisualizadoRepository.findByUsuarioOrderByAlertaIdDesc(usuario);
		return alertasVisualizados;
	}
	
	
	
	
}
