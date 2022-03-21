package br.org.cidadessustentaveis.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.AjusteGeralDTO;
import br.org.cidadessustentaveis.model.administracao.Alerta;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.repository.AlertaRepository;
import br.org.cidadessustentaveis.util.EmailUtil;

@Service
public class AlertaService {
	@Autowired
	private AlertaRepository alertaRepository;
	@Autowired
	private UsuarioService usuarioService;
	
	public Alerta salvar(Alerta alerta) {
		return alertaRepository.save(alerta);
	}

	public List<Alerta> buscarAlertas(String email ) {
		Usuario usuario = usuarioService.buscarPorEmailCredencial(email);
		List<Alerta> alertas = alertaRepository.buscarAlertasNaoVisualizados(usuario);
		List<Alerta> alertasValido = new ArrayList<Alerta>(); 
		Set<String> funcionalidades = usuario.getCredencial().getFuncionalidades();
		for (Alerta alerta : alertas) {
			switch (alerta.getTipoAlerta()) {
				case PROPOSTA_MUNICIPIO:
					if(funcionalidades.contains("ALERTA_PROPOSTA_MUNICIPIO")){
						if(usuario.getPrefeitura() != null ){
								alertasValido.add(alerta);
						}
					} 
					break;
				case SHAPE_FORA_DA_PREFEITURA:
					if(funcionalidades.contains("ALERTA_CAMADA_FORA_AREA")){
						alertasValido.add(alerta);
					}
					break;
				case EVENTO:
					if(funcionalidades.contains("ALERTA_EVENTOS_INTERESSE")){
						if(alerta.getCidade().getId().toString().equals(usuario.getCidadeInteresse())){
							alertasValido.add(alerta);
						} else if(alerta.getAreasInteresse() != null && usuario.getAreasInteresse() != null) {
							for (AreaInteresse areaInteresse : usuario.getAreasInteresse()) {
								if(alerta.getAreasInteresse().contains(areaInteresse)) {
									alertasValido.add(alerta);
									break;
								}
							}
						}
					}
					break;
				case PEDIDO_ADESAO_PREFEITURA:
					if(funcionalidades.contains("ALERTA_SOLICITACAO_ADESAO_PREFEITURA")){
						alertasValido.add(alerta); 
					}
					break;
				case RESPOSTA_COMENTARIO_FORUM:
					if(funcionalidades.contains("ALERTA_COMENTARIO_FORUM")){
						alertasValido.add(alerta); 
					}
				case CADASTRO_INDICADOR:
					if(funcionalidades.contains("ALERTA_CADASTRO_INDICADOR")){
						alertasValido.add(alerta); 
					}
					break;
				case CADASTRO_PLANO_DE_METAS:
					if(funcionalidades.contains("ALERTA_CADASTRO_PLANO_DE_METAS")){
						alertasValido.add(alerta); 
					}
					break;
				default:
					break;
			}
		}
		return alertasValido;
	}
	public Long contarNovosAlertas(String email ) {
		Usuario usuario = usuarioService.buscarPorEmailCredencial(email);
		List<Alerta> alertas = alertaRepository.buscarAlertasNaoVisualizados(usuario);
		Long quantidade = (long) 0;
		Set<String> funcionalidades = usuario.getCredencial().getFuncionalidades();
		for (Alerta alerta: alertas) {
			switch (alerta.getTipoAlerta()) {
				case PROPOSTA_MUNICIPIO:
					if(funcionalidades.contains("ALERTA_PROPOSTA_MUNICIPIO")){
						if(usuario.getPrefeitura() != null 
							&& alerta.getCidade().getId().equals(usuario.getPrefeitura().getCidade().getId())){
							quantidade++;
						}
					}
					break;
				case SHAPE_FORA_DA_PREFEITURA:
					if(funcionalidades.contains("ALERTA_CAMADA_FORA_AREA")){
						quantidade++;
					}
					break;
				case EVENTO:
					if(funcionalidades.contains("ALERTA_EVENTOS_INTERESSE")){
						if(alerta.getCidade().getId().toString().equals(usuario.getCidadeInteresse())){
							quantidade++;
						}  else if(alerta.getAreasInteresse() != null && usuario.getAreasInteresse() != null) {
							for (AreaInteresse areaInteresse : usuario.getAreasInteresse()) {
								if(alerta.getAreasInteresse().contains(areaInteresse)) {
									quantidade++;
									break;
								}
							}
						}
					}
					break;
				case PEDIDO_ADESAO_PREFEITURA:
					if(funcionalidades.contains("ALERTA_SOLICITACAO_ADESAO_PREFEITURA")){
						quantidade++;
					}
					break;
				case RESPOSTA_COMENTARIO_FORUM:
					if(funcionalidades.contains("ALERTA_COMENTARIO_FORUM")){
						quantidade++;
					}
				case CADASTRO_INDICADOR:
					if(funcionalidades.contains("ALERTA_CADASTRO_INDICADOR")){
						quantidade++;
					}
					break;
				case CADASTRO_PLANO_DE_METAS:
					if(funcionalidades.contains("ALERTA_CADASTRO_PLANO_DE_METAS")){
						quantidade++;
					}
					break;
			
				default:
					break;
			}
		}
		return quantidade;
	}
	
	
}
