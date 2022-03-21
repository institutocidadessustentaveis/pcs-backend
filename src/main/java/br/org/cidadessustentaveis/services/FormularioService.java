package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.slugify.Slugify;

import br.org.cidadessustentaveis.dto.FormularioDTO;
import br.org.cidadessustentaveis.dto.FormularioResumidoDTO;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.participacaoCidada.Formulario;
import br.org.cidadessustentaveis.repository.FormularioRepository;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;

@Service
public class FormularioService {
	@Autowired
	private UsuarioContextUtil usuarioContextUtil;
	@Autowired
	private FormularioRepository formularioRepository;
	@Autowired
	private EixoService eixoService;
	@Autowired
	private ObjetivoDesenvolvimentoSustentavelService odsService;
	@Autowired
	private AreaInteresseService areaInteresseService;
	@Autowired
	private Slugify slugify;
	@Autowired
	private RespostaService respostaService;
	@Autowired
	private PerguntaService perguntaService;
	@Autowired
	private SecaoFormularioService secaoFormularioService;
	@Autowired
	private FormularioPreenchidoService formularioPreenchidoService;
	
	public List<FormularioDTO> listar() throws Exception {
		Usuario usuario = usuarioContextUtil.getUsuario();
		List<Formulario> listaFormulario = new ArrayList<>();
		if(usuario.getPrefeitura() == null) {
			listaFormulario = formularioRepository.findAllByOrderByDataCriacaoDesc();
		} else {
			listaFormulario = formularioRepository.findByCidadeId(usuario.getPrefeitura().getCidade().getId());
		}
		List<FormularioDTO> listaDto = listaFormulario.stream().map(formulario -> new FormularioDTO(formulario)).collect(Collectors.toList());
		
		return listaDto;
	}
	
	public List<FormularioResumidoDTO> buscarFormulariosResumido(Long idCidade){
		LocalDate dataAtual = LocalDate.now();
		List<FormularioResumidoDTO> lista = formularioRepository.buscarFormulariosResumido(dataAtual, idCidade);

		return lista;
	}

	public void salvar(FormularioDTO dto) throws Exception {
		Usuario usuario = usuarioContextUtil.getUsuario();
		Formulario formulario = dto.toEntity();
		formulario.setUsuarioCriador(usuario);
		formulario.setDataCriacao(LocalDate.now());
		
		if(dto.getEixos() != null) {
			List<Eixo> eixos = eixoService.buscarEixosPorIds(dto.getEixos());
			formulario.setEixos(eixos);
		}if(dto.getOds() != null) {
			List<ObjetivoDesenvolvimentoSustentavel> ods = odsService.buscarPorIds(dto.getOds());
			formulario.setOds(ods);
		}if(dto.getTemas() != null) {
			List<AreaInteresse> temas = areaInteresseService.buscarAreasPorIds(dto.getTemas());
			formulario.setTemas(temas);
		}
		

		formulario = gerarLinkParaFormulario(usuario, formulario, dto);
		
		this.formularioRepository.save(formulario);	
		
	}
	
	public Formulario gerarLinkParaFormulario(Usuario usuario, Formulario formulario, FormularioDTO dto) {
		boolean gravar = false;
		int contador = 1;	
		
		//Tudo que for mexido dentro do while de um, precisa replicar para o outro
		if(usuario.getPrefeitura() != null ) {
			String link = usuario.getPrefeitura().getCidade().getProvinciaEstado().getSigla();
			link = link+" "+usuario.getPrefeitura().getCidade().getNome();
			link = slugify.slugify(link+" "+dto.getNome());					
			while(gravar != true) {
				String valorDoBanco = formularioRepository.findByLinkLike(link);
				if(valorDoBanco == null) {
					formulario.setLink(link);
					gravar = true;
				}
				else if(gravar != true){
					valorDoBanco = formularioRepository.findByLinkLike(link + "-" + contador);
					if(valorDoBanco == null) {	
						formulario.setLink(link + "-" + contador);
						gravar = true;
					}
					else {
						contador++;
					}
				}
			}
			
		} else {
			String link = slugify.slugify(dto.getNome());			
			while(gravar != true) {
				String valorDoBanco = formularioRepository.findByLinkLike(link);
				if(valorDoBanco == null) {
					formulario.setLink(link);
					gravar = true;
				}
				else if(gravar != true){
					valorDoBanco = formularioRepository.findByLinkLike(link + "-" + contador);
					if(valorDoBanco == null) {	
						formulario.setLink(link + "-" + contador);
						gravar = true;
					}
					else {
						contador++;
					}
				}
			}		
		}
		
		if(formulario.getSecoes() != null) {
			formulario.getSecoes().forEach(s -> s.setFormulario(formulario));
		}
		
		return formulario;
	}
	
	public void atualizar(FormularioDTO dto) throws AuthenticationException,Exception {
		Usuario usuario = this.usuarioContextUtil.getUsuario();
		Formulario formulario = buscarPorId(dto.getId());
		if(usuario.getPrefeitura() != null) {
			if(formulario.getUsuarioCriador().getPrefeitura() == null) {
				throw new AuthenticationException("Você não pode editar esse registro");
			}
			if(usuario.getPrefeitura().getCidade().getId() != formulario.getUsuarioCriador().getPrefeitura().getCidade().getId()) {
				throw new AuthenticationException("Você não pode editar esse registro");
			}
		} else {
			if(formulario.getUsuarioCriador().getPrefeitura() != null) {
				throw new AuthenticationException("Você não pode editar esse registro");
			}
		}
		
		formulario.atualizar(dto);
		if(dto.getEixos() != null && !dto.getEixos().isEmpty()) {
			formulario.setEixos(eixoService.buscarEixosPorIds(dto.getEixos()));
		}
		if(dto.getOds() != null && !dto.getOds().isEmpty()) {
			formulario.setOds(odsService.buscarPorIds(dto.getOds()));
		}
		if(dto.getTemas() != null && !dto.getTemas().isEmpty()){
			formulario.setTemas(areaInteresseService.buscarAreasPorIds(dto.getTemas()));
		}
		
		this.formularioRepository.save(formulario);
		this.respostaService.excluirRespostasSemPergunta();
		this.perguntaService.excluirPerguntasSemSecao();
		this.secaoFormularioService.excluirSecoesSemFormulario();
	}

	public Formulario buscarPorId(Long id) {
		Formulario formulario = this.formularioRepository.findById(id).orElse(null);
		return formulario;
	}

	public void excluir(Long id) throws Exception   {
		Usuario usuario = this.usuarioContextUtil.getUsuario();
		Formulario formulario = buscarPorId(id);
		
		if(usuario.getPrefeitura() != null) {
			if(formulario.getUsuarioCriador().getPrefeitura() == null) {
				throw new AuthenticationException("Você não pode editar esse registro");
			}
			if(usuario.getPrefeitura().getCidade().getId() != formulario.getUsuarioCriador().getPrefeitura().getCidade().getId()) {
				throw new AuthenticationException("Você não pode editar esse registro");
			}
		} 
		this.formularioRepository.deleteById(id);
	}

	public Formulario buscarPorLink(String link) throws AuthenticationException {
		Usuario usuarioLogado = null;
		Formulario formulario = this.formularioRepository.findByLink(link);
		try {
			usuarioLogado = usuarioContextUtil.getUsuario();
		} catch (Exception e) {
			return formulario;
		}
		if (formularioPreenchidoService.isFormAnsweredByUser(usuarioLogado.getId(), formulario.getId())) {
			throw new AuthenticationException("Você já respondeu este formulário");
		}
		else {
			return formulario;
		}
	}
}
