package br.org.cidadessustentaveis.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.config.auth.DetailsService;
import br.org.cidadessustentaveis.dto.AjusteGeralDTO;
import br.org.cidadessustentaveis.dto.CredencialDTO;
import br.org.cidadessustentaveis.dto.FiltroUsuarioDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.PerfilDTO;
import br.org.cidadessustentaveis.dto.PrefeituraDTO;
import br.org.cidadessustentaveis.dto.RegistroUsuariosFiltroDTO;
import br.org.cidadessustentaveis.dto.UsuarioCadastroDTO;
import br.org.cidadessustentaveis.dto.UsuarioDTO;
import br.org.cidadessustentaveis.dto.UsuarioSimplesDTO;
import br.org.cidadessustentaveis.model.administracao.AreaAtuacao;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Credencial;
import br.org.cidadessustentaveis.model.administracao.EmailToken;
import br.org.cidadessustentaveis.model.administracao.Perfil;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.enums.FuncionalidadeToken;
import br.org.cidadessustentaveis.model.institucional.Newsletter;
import br.org.cidadessustentaveis.repository.CredencialRepository;
import br.org.cidadessustentaveis.repository.UsuarioRepository;
import br.org.cidadessustentaveis.services.exceptions.BusinessLogicErrorException;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.EmailUtil;
import br.org.cidadessustentaveis.util.ProfileUtil;
import br.org.cidadessustentaveis.util.SenhaUtil;


@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;
	@Autowired
	private PerfilService perfilService;
	@Autowired
	private PrefeituraService prefeituraService;
	@Autowired
	private EmailTokenService emailTokenService;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private ProfileUtil profileUtil;

	@Autowired
	private CredencialRepository repositoryCredential;
	
	@Autowired
	private AreaAtuacaoService areaAtuacaoService;
	
	@Autowired
	private AreaInteresseService areaInteresseService;
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private AjusteGeralService ajusteGeralService;
	
	public static final String ALERTA_EMAIL = "ALERTA-EMAIL-NOVA-CIDADE";

	public final Long PERFIL_ADMINISTRADOR_ID = 1L;
	private Logger logger = LoggerFactory.getLogger(DetailsService.class);

	public List<Usuario> buscar() {
		return repository.findAll();
	}

	public Page<Usuario> buscarComPaginacao(Integer page, Integer linesPerPage , String orderBy, String direction) {
		if(orderBy.equals("municipio")) orderBy = "prefeitura.cidade.nome";

		Pageable pageable = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAllByCredencialSnExcluido(false, pageable);
	}
	public Page<Usuario> buscarComPaginacaoPrefeitura(Integer page, Integer linesPerPage , String orderBy, String direction, String nome, String uf, String cidade, List<Long> perfil) {
		Pageable pageable = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy); 
		if(perfil == null || perfil.isEmpty()){
			return repository.findByCredencialSnExcluidoAndNomeContainingIgnoreCaseAndPrefeituraCidadeProvinciaEstadoSiglaContainingIgnoreCaseAndPrefeituraCidadeNomeContainingIgnoreCaseAndPrefeituraIsNotNull(false,nome,uf,cidade, pageable);
		} else {
			return repository.findByCredencialSnExcluidoAndNomeContainingIgnoreCaseAndPrefeituraCidadeProvinciaEstadoSiglaContainingIgnoreCaseAndPrefeituraCidadeNomeContainingIgnoreCaseAndCredencialListaPerfilIdInAndPrefeituraIsNotNull(false,nome,uf,cidade, perfil, pageable);
		}
	}
	
	public List<FiltroUsuarioDTO> buscarRegistroUsuarioFiltrado(RegistroUsuariosFiltroDTO filtro) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<FiltroUsuarioDTO> query = cb.createQuery(FiltroUsuarioDTO.class);
		
		Root<Usuario> usuario = query.from(Usuario.class);
		
		Join<Usuario, Prefeitura> joinPrefeitura = usuario.join("prefeitura",JoinType.LEFT);
		Join<Usuario, Credencial> joinCredencial = usuario.join("credencial", JoinType.LEFT);
		
			query.multiselect(
							usuario.get("id"), 
							usuario.get("nome"), 
							usuario.get("telefone"), 
							usuario.get("email"), 
							joinPrefeitura.get("cidade").get("nome"), 
							joinPrefeitura.get("cidade").get("provinciaEstado").get("nome"),  
							usuario.get("organizacao"), 
							usuario.get("credencial") 
						 	).distinct(true);
			
			
			
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		Path<Boolean> snExcluidoUsuario = joinCredencial.get("snExcluido");
		Predicate predicateForSnExcluido = cb.isFalse(snExcluidoUsuario); 
		
		if (filtro.getTipoUsuario() != null) {
			Path<List<Long>> perfilUsuario = joinCredencial.get("listaPerfil");
			Predicate predicateForPerfil = cb.isMember(filtro.getTipoUsuario(), perfilUsuario);
			predicateList.add(cb.and(predicateForPerfil, predicateForSnExcluido));
		}
		
		if(filtro.getInstituicao() != null && !filtro.getInstituicao().equals("")) {
			Path<String> organizacaoUsuario = usuario.get("organizacao");
			Predicate predicateForOrganizacao = cb.like(cb.lower(organizacaoUsuario), "%" + filtro.getInstituicao().toLowerCase() + "%");
			predicateList.add(cb.and(predicateForOrganizacao, predicateForSnExcluido));
		}
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<FiltroUsuarioDTO> typedQuery = em.createQuery(query);
		List<FiltroUsuarioDTO> lista = typedQuery.getResultList();
		
		return lista;
		
	}
	
	public List<UsuarioCadastroDTO> buscarUsuarioPrefeituraFiltrado(String nome, String uf, String cidade, List<Long> perfil) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UsuarioCadastroDTO> query = cb.createQuery(UsuarioCadastroDTO.class);
		
		Root<Usuario> usuario = query.from(Usuario.class);
		
		Join<Usuario, Prefeitura> joinPrefeitura = usuario.join("prefeitura",JoinType.LEFT);
		Join<Usuario, Credencial> joinCredencial = usuario.join("credencial", JoinType.LEFT);
		
			query.multiselect(
							usuario.get("id"), 
							usuario.get("nome"), 
							usuario.get("telefone"), 
							usuario.get("email"), 
							joinPrefeitura.get("cidade").get("nome"), 
							joinPrefeitura.get("cidade").get("provinciaEstado").get("sigla"), 
							usuario.get("credencial"),
							joinPrefeitura.get("cidade").get("codigoIbge")
						 	).distinct(true);
			
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		Path<Boolean> snExcluidoUsuario = joinCredencial.get("snExcluido");
		Predicate predicateForSnExcluido = cb.isFalse(snExcluidoUsuario); 
		
		if(nome != null && !nome.equals("")) {
			Path<String> nomeUsuario = usuario.get("nome");
			Predicate predicateForNome = cb.like(cb.lower(nomeUsuario), "%" + nome + "%");
			predicateList.add(cb.and(predicateForNome, predicateForSnExcluido));
		}
		
		if(uf != null && !uf.equals("")) {
			Path<String> ufUsuario = joinPrefeitura.get("cidade").get("provinciaEstado").get("sigla");
			Predicate predicateForUf = cb.like(cb.lower(ufUsuario), "%" + uf + "%");
			predicateList.add(cb.and(predicateForUf, predicateForSnExcluido));
		}
		
		if(cidade != null && !cidade.equals("")) {
			Path<String> cidadeUsuario = joinPrefeitura.get("cidade").get("nome");
			Predicate predicateForCidade = cb.like(cb.lower(cidadeUsuario), "%" + cidade + "%");
			predicateList.add(cb.and(predicateForCidade, predicateForSnExcluido));
		}
		
		if (perfil != null) {
			perfil.forEach(p -> { 
				Path<List<Long>> perfilUsuario = joinCredencial.get("listaPerfil");
				Predicate predicateForPerfil = cb.isMember(p, perfilUsuario);
				predicateList.add(cb.and(predicateForPerfil, predicateForSnExcluido));
			});
		}
		
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<UsuarioCadastroDTO> typedQuery = em.createQuery(query);
		List<UsuarioCadastroDTO> lista = typedQuery.getResultList();
		
		return lista;
		
	}
	
	public List<FiltroUsuarioDTO> buscarFiltrado(String nome, Long cidade, Long perfil, String organizacao) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<FiltroUsuarioDTO> query = cb.createQuery(FiltroUsuarioDTO.class);
		
		Root<Usuario> usuario = query.from(Usuario.class);
		
		Join<Usuario, Prefeitura> joinPrefeitura = usuario.join("prefeitura",JoinType.LEFT);
		Join<Prefeitura, Cidade> joinCidade = joinPrefeitura.join("cidade",JoinType.LEFT);
		Join<Cidade, ProvinciaEstado> joinEstado = joinCidade.join("provinciaEstado",JoinType.LEFT);
		Join<Usuario, Credencial> joinCredencial = usuario.join("credencial", JoinType.LEFT);
		
			query.multiselect(
							usuario.get("id"), 
							usuario.get("nome"), 
							usuario.get("telefone"), 
							usuario.get("email"), 
							joinCidade.get("nome"), 
							joinEstado.get("nome"),  
							usuario.get("organizacao"), 
							usuario.get("credencial") 
						 	).distinct(true);
			
			
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		Path<Boolean> snExcluidoUsuario = joinCredencial.get("snExcluido");
		Boolean verificado = false;
		Predicate predicateForSnExcluido = cb.isFalse(snExcluidoUsuario); 
		
		if(nome != null && !nome.equals("")) {
			Path<String> nomeUsuario = usuario.get("nome");
			Predicate predicateForNome = cb.like(cb.lower(nomeUsuario), "%" + nome.toLowerCase() + "%");
			predicateList.add(cb.and(predicateForNome, predicateForSnExcluido));
			verificado = true;
		}
		
		if(organizacao != null && !organizacao.equals("")) {
			Path<String> organizacaoUsuario = usuario.get("organizacao");
			Predicate predicateForOrganizacao = cb.like(cb.lower(organizacaoUsuario), "%" + organizacao.toLowerCase() + "%");
			predicateList.add(cb.and(predicateForOrganizacao, predicateForSnExcluido));
			verificado = true;
		}
		
		if (cidade != null) {
			Path<Long> cidadeUsuario = joinPrefeitura.get("cidade");
			Predicate predicateForCidade = cb.equal(cidadeUsuario, cidade);
			predicateList.add(cb.and(predicateForCidade, predicateForSnExcluido));
			verificado = true;
		}
		
		if (perfil != null) {
			Path<List<Long>> perfilUsuario = joinCredencial.get("listaPerfil");
			Predicate predicateForPerfil = cb.isMember(perfil, perfilUsuario);
			predicateList.add(cb.and(predicateForPerfil, predicateForSnExcluido));
			verificado = true;
		}
		
		if (!verificado) {
			predicateList.add(predicateForSnExcluido);
		}
		
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<FiltroUsuarioDTO> typedQuery = em.createQuery(query);
		List<FiltroUsuarioDTO> lista = typedQuery.getResultList();
		
		return lista;
	}

	public Usuario buscarPorId(Long id) {
		Optional<Usuario> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado!"));
	}

	public Usuario buscarPorEmail(String email) {
		Usuario obj = new Usuario();
		try {
			Credencial credencial = repositoryCredential.findByLoginIgnoreCaseAndSnExcluido(email, false);
			obj = credencial.getUsuario();
		} catch (Exception ex) {
			throw new ObjectNotFoundException("Usuário não encontrado!");
		}
		
		return obj;
	}

	public Usuario buscarPorEmailCredencial(String email) {
		Usuario obj = new Usuario();
		try {
			Credencial credencial = repositoryCredential.findByLoginIgnoreCaseAndSnExcluido(email, false);
			obj = credencial.getUsuario();
			
		} catch (Exception ex) {
			throw new ObjectNotFoundException("Usuário não encontrado!");
		}
		
		return obj;
	}

	public void inserirResponsavel(List<UsuarioDTO> usuariosDto)  throws Exception{

		Prefeitura prefeitura = null;
		for (UsuarioDTO usuarioDTO : usuariosDto) {
			Usuario usuario = usuarioDTO.toEntityInsert();
			recuperaReferenciaPerfil(usuarioDTO, usuario);
			recuperaReferenciaPrefeitura(usuarioDTO, usuario);
			usuario.getCredencial().setLogin(usuario.getEmail());
			salvar(usuario);
			prefeitura = usuario.getPrefeitura();
			

			EmailToken emailToken = EmailToken.builder().ativo(Boolean.TRUE)
					.funcionalidadeToken(FuncionalidadeToken.GESTAO_PUBLICA)
					.hash(SenhaUtil.criptografarSHA2(usuario.getId() + "" + usuario.getNome() + "" + usuario.getEmail()
							+ "" + LocalDateTime.now().getNano()))
					.usuario(usuario).build();
			emailTokenService.salvar(emailToken);
			try {
				enviarEmailDeNotificacaoDeResponsavelCadastrado(usuariosDto, null, false, prefeitura);
				emailUsuarioGestaoPublica(emailToken);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		emailTokenService.inativarTokenAprovacaoPrefeituraPrefeitura(prefeitura);
	}

	public void inserirResponsavel(UsuarioSimplesDTO dto) throws Exception {
		Usuario usuario = new Usuario();
		usuario.setNome(dto.getNome());
		usuario.setEmail(dto.getEmail());
		usuario.setTelefone_fixo(dto.getTelefone());
		usuario.setTelefone(dto.getCelular());
		usuario.setCargo(dto.getCargo());
		Credencial c = new Credencial(null, dto.getEmail(), null , false, false, false);
		List<Perfil> perfis = new ArrayList<>();
		for(Long idPerfil : dto.getPerfis()) {
			Perfil p = perfilService.buscarPorId(idPerfil);
			perfis.add(p);
		}
		c.setListaPerfil(perfis);
		usuario.setCredencial(c);
		Prefeitura prefeitura = prefeituraService.listarPorIdCidade(dto.getIdCidade());
		
		if(prefeitura == null) {
			throw new Exception("Não encontramos uma prefeitura signatária para esta cidade."); 
		}
		
		usuario.setPrefeitura(prefeitura);
		salvar(usuario);
		
		EmailToken emailToken = EmailToken.builder().ativo(Boolean.TRUE)
				.funcionalidadeToken(FuncionalidadeToken.GESTAO_PUBLICA)
				.hash(SenhaUtil.criptografarSHA2(usuario.getId() + "" + usuario.getNome() + "" + usuario.getEmail()
						+ "" + LocalDateTime.now().getNano()))
				.usuario(usuario).build();
		emailTokenService.salvar(emailToken);
		try {
			enviarEmailDeNotificacaoDeResponsavelCadastrado(null, usuario, false, prefeitura);
			emailUsuarioGestaoPublica(emailToken);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

	public void alterarResponsavel(UsuarioSimplesDTO dto) throws Exception {
		Usuario usuario = buscarPorId(dto.getId());
		usuario.setNome(dto.getNome());
		usuario.setCargo(dto.getCargo());
		usuario.setEmail(dto.getEmail());
		usuario.setTelefone_fixo(dto.getTelefone());
		usuario.setTelefone(dto.getCelular());
		usuario.getCredencial().setLogin(dto.getEmail());
		usuario.getCredencial().setListaPerfil(new ArrayList<>());
		for(Long idPerfil : dto.getPerfis()) {
			Perfil p = perfilService.buscarPorId(idPerfil);
			usuario.getCredencial().getListaPerfil().add(p);
		}
		Prefeitura prefeitura = prefeituraService.listarPorIdCidade(dto.getIdCidade());
		if(prefeitura == null) {
			throw new Exception("Não encontramos uma prefeitura signatária para esta cidade."); 
		}
		
		try {
			enviarEmailDeNotificacaoDeResponsavelCadastrado(null, usuario, true, prefeitura);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		usuario.setPrefeitura(prefeitura);
		salvar(usuario);
		
	}
	
	public void enviarEmailDeNotificacaoDeResponsavelCadastrado(List<UsuarioDTO> usuariosDto, Usuario usuarioRef, Boolean isAlteracao, Prefeitura prefeitura) throws EmailException {		
		List<AjusteGeralDTO> listaEmails = ajusteGeralService.buscarListaAjustes(ALERTA_EMAIL);
		
		List<String> listaDestinatarios = new ArrayList<String>();
		
		for(AjusteGeralDTO email : listaEmails) {
			listaDestinatarios.add(email.getConteudo());
		}
		
		String mensagem = null;
		
		if(isAlteracao) {
			mensagem = "<p>A Prefeitura do município de " + prefeitura.getCidade().getNome() + " alterou os responsáveis na Plataforma. </p>";
		} else {
			mensagem = "<p>A Prefeitura do município de " + prefeitura.getCidade().getNome() + " cadastrou novos responsáveis na Plataforma. </p>";
		}
		 
		
		if(usuariosDto != null) {
			
			for(UsuarioDTO usuario : usuariosDto) {
				
				String nomePerfil = null;
				Iterator<PerfilDTO> it = usuario.getCredencial().getListaPerfil().iterator();
				StringBuilder sb = new StringBuilder();
				while(it.hasNext()) {
					sb.append(it.next().getNome());
					if(it.hasNext()) {
						sb.append(", ");
					}
				}
				
				nomePerfil = sb.toString();
				
				String mensagemAux = "<br>"
									+ "<p>Nome:  " + usuario.getNome() + "</p>"
									+ "<p>E-mail: " + usuario.getEmail() + "</p>"
									+ "<p>Atribuições: " + nomePerfil + "</p>";
				
				mensagem = mensagem + mensagemAux;
			}
		} else if(usuarioRef != null) {
			
			String nomePerfil = null;
			Iterator<Perfil> it = usuarioRef.getCredencial().getListaPerfil().iterator();
			StringBuilder sb = new StringBuilder();
			while(it.hasNext()) {
				sb.append(it.next().getNome());
				if(it.hasNext()) {
					sb.append(", ");
				}
			}
			
			nomePerfil = sb.toString();
			
			String mensagemAux = "<br>"
					+ "<p>Nome:  " + usuarioRef.getNome() + "</p>"
					+ "<p>E-mail: " + usuarioRef.getEmail() + "</p>"
					+ "<p>Atribuições: " + nomePerfil + "</p>"
					+ "<br>";
			
			mensagem = mensagem + mensagemAux;
		}
		
		if(!listaDestinatarios.isEmpty()) {
			emailUtil.enviarEmailHTML(listaDestinatarios, "Notificação de Cadastro de Responsáveis na Plataforma", mensagem);
		}
		
	}
	
	private void emailUsuarioGestaoPublica(EmailToken emailToken) throws EmailException {
		String urlPCS = profileUtil.getProperty("profile.frontend");
		String urlFormularioSenha = urlPCS + "/cadsenha?token=" + emailToken.getHash();
		List<String> emails = new ArrayList<String>();
		emails.add(emailToken.getUsuario().getEmail());
		emailUtil.enviarEmailHTML(emails, "PCS - Usuário cadastrado!",
				"<h2 style='font-family:Arial, Helvetica, sans-serif; color:#000000;'>Olá "
						+ emailToken.getUsuario().getNome() + "</h2>"
						+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>Seu usuário foi cadastrado com sucesso na Plataforma Cidades Sustentáveis!</p>"
						+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>Utilize seu email: "
						+ emailToken.getUsuario().getEmail() + " para realizar login no sistema.</p>"
						+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>Clique <a href=\""
						+ urlFormularioSenha + "\">aqui</a> para cadastrar sua senha, ou no link abaixo:  </p>"
						+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'> <a href='"
						+ urlFormularioSenha + "'>Formulário de cadastro de senha</a></p>");
	}

	private void emailUsuarioRecuperacaoSenha(EmailToken emailToken) throws EmailException {
		String urlPCS = profileUtil.getProperty("profile.frontend");
		String urlFormularioSenha = urlPCS + "/recuperasenha?token=" + emailToken.getHash();
		List<String> emails = new ArrayList<String>();
		emails.add(emailToken.getUsuario().getEmail());
		emailUtil.enviarEmailHTML(emails, "Programa Cidades Sustentáveis - Solicitação de mudança de senha",
				"<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>Olá, " + emailToken.getUsuario().getNome() + "</p>"
						+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>Informamos que foi solicitado na Plataforma do Programa Cidades Sustentáveis um pedido de alteração de sua senha de acesso. </p>"
						+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>Clique no link abaixo para acessar a página de recadastramento de senha:</p>"
						+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>" + urlFormularioSenha + "</p>"
						+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>Caso você não tenha feito esta solicitação de alteração de senha, por favor desconsidere esta mensagem.</p>");	
	}

	public Usuario inserirGestorPlataforma(UsuarioDTO usuarioDto) throws EmailException {
		String frontEndUrlLogin = profileUtil.getProperty("profile.frontend") + "/login";
		Usuario usuario = usuarioDto.toEntityInsertGestorPlataforma();
		recuperaReferenciaPerfil(usuarioDto, usuario);
		String senha = SenhaUtil.gerarSenha(10);
		usuario.getCredencial().setSenha(SenhaUtil.criptografarSHA2(senha));
		usuario.getCredencial().setLogin(usuario.getEmail());
		salvar(usuario);

		List<String> emails = new ArrayList<String>();
		emails.add(usuario.getEmail());
		emailUtil.enviarEmailHTML(emails, "PCS - Usuário cadastrado!",
				"<h2 style='font-family:Arial, Helvetica, sans-serif; color:#000000;'>Olá " + usuario.getNome()
						+ "</h2>"
						+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>Seu usuário foi cadastrado com sucesso na base do PCS!</p>"
						+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>Sua senha de acesso é: <b><span style='font-size:18px'>"
						+ senha + "</span></b></p>"
						+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>Para acessar sua conta basta <a href='"
						+ frontEndUrlLogin + "'>clicar aqui</a>! ou acessar o link abaixo: </p>"
						+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'><a href='"
						+ frontEndUrlLogin + "'>" + frontEndUrlLogin + "</a></p>");
		return usuario;
	}

	
	public Usuario inserirCidadao(UsuarioDTO usuarioDto) throws EmailException {
		String frontEndUrlLogin = profileUtil.getProperty("profile.frontend") + "/login";
		Usuario usuario = usuarioDto.toEntityInsertCidadao();
		recuperaReferenciaPerfil(usuarioDto, usuario);
		usuario.getCredencial().setSenha(SenhaUtil.criptografarSHA2(usuario.getCredencial().getSenha()));

		salvar(usuario);

		List<String> emails = new ArrayList<String>();
		emails.add(usuario.getEmail());
		emailUtil.enviarEmailHTML(emails, "PCS - Usuário cadastrado!",
				"<h2 style='font-family:Arial, Helvetica, sans-serif; color:#000000;'>Olá " + usuario.getNome()
				+ "</h2>"
				+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>Seu usuário foi cadastrado com sucesso na base do PCS!</p>"
				+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>Para acessar sua conta basta <a href='"
				+ frontEndUrlLogin + "'>clicar aqui</a>! ou acessar o link abaixo: </p>"
				+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'><a href='"
				+ frontEndUrlLogin + "'>" + frontEndUrlLogin + "</a></p>");
		
		if (usuario.getOrganizacao() != null) {
		  emails = new ArrayList<String>();
		  for (Usuario usuarioAdmin: usuariosComPerfilId(PERFIL_ADMINISTRADOR_ID)) {
		    emails.add(usuarioAdmin.getEmail());
		  }
		
		  emailUtil.enviarEmailHTML(emails, "PCS - Nova Instuição cadastrada!",
		      "<h2 style='font-family:Arial, Helvetica, sans-serif; color:#000000;'>Nova Instituição cadastrada</h2>\r\n" + 
		      "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>\r\n" + 
		      "Foi efetuada o cadastro de instituição " + usuario.getOrganizacao() + " na plataforma.\r\n</p>");	
		}
		
		return usuario;
	}

	public Usuario editar(UsuarioDTO usuarioDto, Long id) {
		Usuario userRef = buscarPorId(id);
		userRef = usuarioDto.toEntityUpdate(userRef);
		recuperaReferenciaPerfil(usuarioDto, userRef);
		userRef.getCredencial().setLogin(userRef.getEmail());
		return salvar(userRef);
	}
	
	public void editarBloqueadoForum(UsuarioDTO usuarioDto, Long id) {
		Usuario userRef = buscarPorId(id);
		if(userRef.isBloqueadoForum()) {
			userRef.setBloqueadoForum(false);
		}
		else {
			userRef.setBloqueadoForum(true);
		}
		userRef = usuarioDto.toEntityUpdateBloqueadoForum(userRef);
		repository.updateBloqueadoForum(userRef.getId(), userRef.isBloqueadoForum());
	}

	public void deletar(Long id) {
		Usuario usuario = buscarPorId(id);
		usuario.getCredencial().setSnExcluido(true);
		try {
			repository.save(usuario);
		} catch (DataIntegrityViolationException e) {
			logger.error(" Usuário id" + id + " já está sendo usado no sistema");
			throw new DataIntegrityException("O registro está sendo usado no sistema");
		}
	}

	private void recuperaReferenciaPrefeitura(UsuarioDTO usuarioDto, Usuario usuario) {
		Prefeitura prefeitura = prefeituraService.buscarPorId(usuarioDto.getPrefeitura().getId());
		usuario.setPrefeitura(prefeitura);
	}

	private void recuperaReferenciaPerfil(UsuarioDTO usuarioDto, Usuario usuario) {
		List<Perfil> perfisValidos = new ArrayList<>();
		for (PerfilDTO perfilDTO : usuarioDto.getCredencial().getListaPerfil()) {
			Perfil perfil = perfilService.buscarPorId(perfilDTO.getId());
			if (usuario.getCredencial().getListaPerfil() == null) {
				usuario.getCredencial().setListaPerfil(new ArrayList<>());
			}
			if (!usuario.getCredencial().getListaPerfil().contains(perfil)) {
				usuario.getCredencial().getListaPerfil().add(perfil);
			}
			perfisValidos.add(perfil);

		}

		for (int i = 0; i < usuario.getCredencial().getListaPerfil().size(); i++) {
			Perfil perfil = usuario.getCredencial().getListaPerfil().get(i);
			if (!perfisValidos.contains(perfil)) {
				usuario.getCredencial().getListaPerfil().remove(i);
				i--;
			}
		}

		if (usuario.getCredencial().getListaPerfil() == null || usuario.getCredencial().getListaPerfil().isEmpty()) {
			throw new ObjectNotFoundException("Perfil não encontrado!");
		}
	}

	public Usuario alterarSenha(String senha, String login, String novaSenha) {

		Credencial credencial = repositoryCredential.findByLoginIgnoreCaseAndSnExcluido(login, false);

		if (credencial == null) {
			logger.error("Erro em loadUserByUsername(): Usuário'" + login + "' não existe");
			throw new UsernameNotFoundException("Usuário '" + login + "' não existe!");
		}

		String existingPassword = SenhaUtil.criptografarSHA2(senha); // Senha que o usuario digitar
		String dbPassword = credencial.getSenha(); // Carregar senha do banco de dados

		if (existingPassword.equals(dbPassword)) {
			credencial.setSenha(SenhaUtil.criptografarSHA2(novaSenha));
			repositoryCredential.save(credencial);
		} else {
			throw new ObjectNotFoundException("Senha atual não confere com a senha informada!");
		}
		return credencial.getUsuario();
	}

	public Usuario cadastrarSenha(String login, String senha, String confirmacaoSenha) {
		Credencial credencial;

		try {
			credencial = repositoryCredential.findByLoginIgnoreCaseAndSnExcluido(login, false);
			if (credencial == null) {
				logger.error("Erro em loadUserByUsername(): Usuário'" + login + "' não existe");
				throw new UsernameNotFoundException("Usuário '" + login + "' não existe!");
			} else if (senha.equals(confirmacaoSenha)) {
				confirmacaoSenha = SenhaUtil.criptografarSHA2(senha);
				credencial.setSenha(confirmacaoSenha);
				repositoryCredential.save(credencial);
			} else {
				throw new ObjectNotFoundException(
						"A senha e a confirmação de senha estão divergentes. favor verificar!");
			}
		} catch (Exception ex) {
			return null;
		}
		emailTokenService.inativarTokenUsuarioPrefeitura(credencial.getUsuario());
		return credencial.getUsuario();
	}

	public Usuario criarNovaSenha(String senha, String login, String novaSenha) {

		Credencial credencial;
		List<String> emails = new ArrayList<String>();
		String frontEndUrlLogin = profileUtil.getProperty("profile.frontend") + "/login";

		try {

			credencial = repositoryCredential.findByLoginIgnoreCaseAndSnExcluido(login, false);
			String email = credencial.getUsuario().getEmail();

			if (credencial == null || credencial.getId() == null) {
				throw new UsernameNotFoundException("Usuário '" + login + "' não existe!");
			} else if (senha.equals(novaSenha)) {
				novaSenha = SenhaUtil.criptografarSHA2(senha);
				credencial.setSenha((novaSenha));
				repositoryCredential.save(credencial);

				emailTokenService.inativarTokenUsuarioSenha(credencial.getUsuario());
				
				emails.add(email);
				emailUtil.enviarEmailHTML(emails, "Programa Cidades Sustentáveis - Confirmação da mudança de senha",
						"<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>Olá, " + credencial.getUsuario().getNome() + "</p>"
								+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>Informamos que a sua senha foi alterada com sucesso!</p>"
								+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>Para acessar sua conta basta clicar no link abaixo:</p>" 
								+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>" + frontEndUrlLogin + "</p>");
			} else {
				throw new ObjectNotFoundException(
						"A senha e a confirmação de senha estão divergentes. favor verificar!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return credencial.getUsuario();
	}

	public boolean esqueciSenha(String email) throws EmailException {
		Usuario usuario = new Usuario();

		try {
			usuario = this.buscarPorEmail(email);
			EmailToken emailToken = EmailToken.builder().ativo(Boolean.TRUE)
					.funcionalidadeToken(FuncionalidadeToken.RECUPERACAO_SENHA)
					.hash(SenhaUtil.criptografarSHA2(usuario.getId() + "" + usuario.getNome() + "" + usuario.getEmail()
							+ "" + LocalDateTime.now().getNano()))
					.usuario(usuario).build();
			emailTokenService.salvar(emailToken);
			emailUsuarioRecuperacaoSenha(emailToken);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Usuario salvar(Usuario usuario) {
		Credencial existente = repositoryCredential.findByLoginIgnoreCaseAndSnExcluido(usuario.getEmail(), false);
		if(usuario.getId() == null && existente != null && existente.getSnExcluido().equals(Boolean.FALSE)) {
			if(existente.getUsuario().getPrefeitura() != null && usuario.getPrefeitura() != null
				&& !existente.getUsuario().getPrefeitura().getId().equals(usuario.getPrefeitura().getId())
				&& existente.getUsuario().getPrefeitura().getCidade().getId().equals(usuario.getPrefeitura().getCidade().getId())) {
				usuario.getCredencial().setSnBloqueado(true);
			} else {
				throw new PersistenceException("Já existe usuário vinculado ao e-mail: "+usuario.getEmail());
			}
			
		}
		if(usuario.getId() != null && existente != null && existente.getSnExcluido().equals(Boolean.FALSE)  
				&& usuario.getCredencial().getId() != existente.getId() )  {
			throw new PersistenceException("Já existe usuário vinculado ao e-mail: "+usuario.getEmail());
		}
		repository.save(usuario);
		return usuario;
	}
	
	public List<Usuario> usuariosComPerfilId(final Long id) {
	  return repository.findByCredencialPerfilId(id);
	}
	
	public Optional<Prefeitura> getPrefeitura(String email) {
		return repository.findPrefeituraByEmailGestor(email);
	}
	
	public Usuario editarUsuarioLogado(UsuarioDTO usuarioDto, Long id) {
		
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuarioLogado  = buscarPorEmailCredencial(user);
		if (id.longValue() != usuarioLogado.getId().longValue()) {
			throw new BusinessLogicErrorException("Usuário logado não possui permissão para editar esse usuário.");
		}
		
		Usuario userRef = buscarPorId(id);
		userRef.setNome(usuarioDto.getNome());
		userRef.setTelefone_fixo(usuarioDto.getTelefone_fixo());
		userRef.setTelefone(usuarioDto.getTelefone());
		userRef.setCidadeInteresse(usuarioDto.getCidadeInteresse());
		
		List<AreaInteresse> listaAreaInteresse = new ArrayList<>();
		for (AreaInteresse area : usuarioDto.getAreasInteresse()) {
			AreaInteresse areaRef = areaInteresseService.buscarPorId(area.getId());
			listaAreaInteresse.add(areaRef);
		}
		
		userRef.setOrganizacao(usuarioDto.getOrganizacao());
		userRef.setCargo(usuarioDto.getCargo());
		List<AreaAtuacao> listaAreaAtuacao = new ArrayList<>();
		for (AreaAtuacao area : usuarioDto.getAreasAtuacoes()) {
			AreaAtuacao areaRef = areaAtuacaoService.buscarPorId(area.getId());
			listaAreaAtuacao.add(areaRef);
		}
		userRef.setTipoInstituicao(usuarioDto.getTipoInstituicao());
		
				
		userRef.setAreasInteresse(listaAreaInteresse);
		userRef.setAreasAtuacao(listaAreaAtuacao);
		return salvar(userRef);
	}

	public List<String> listarEmailUsuarioComAreaDeInteresse(List<AreaInteresse> listaAreaInteresse){
		List<String> emails = repository.findByAreasInteresseIn(listaAreaInteresse);
		return emails;		
	}
	
	public List<String> listarEmailUsuarioComAreaDeInteresseId(List<Long> listaAreaInteresse){
		List<String> emails = repository.findByAreasInteresseIdIn(listaAreaInteresse);
		return emails;		
	}
	
	public List<String> listarEmailUsuarioComCidadeInteresseId(Long idCidadeInteres){
		List<String> emails = repository.listarEmailUsuarioComCidadeInteresseId(idCidadeInteres.toString());
		return emails;		
	}
	
	
	public List<Usuario> buscarComboBoxUsuario() {
		return repository.findComboBoxUsuario();
	}
	
	public List<Newsletter> buscarNewsletter() {
		List<UsuarioDTO> usuarios = repository.findUsuariosRecebeEmailTrueECredencialNaoExcluida();

		List<Newsletter> newsletters = usuarios.stream()
													.map((u) -> new Newsletter(u.getEmail(), new Date(), true))
												.collect(Collectors.toList());
		return newsletters;
	}
	
	public void emailUsuarioUpdateSenha(EmailToken emailToken, String senha) throws EmailException {
		String urlPCS = profileUtil.getProperty("profile.frontend");
		String urlFormularioSenha = urlPCS + "/recuperasenha?token=" + emailToken.getHash();
		List<String> emails = new ArrayList<String>();
		emails.add(emailToken.getUsuario().getEmail());
		
		emailUtil.enviarEmailHTML(emails, "PCS - Recuperação de senha!",
				"<h2 style='font-family:Arial, Helvetica, sans-serif; color:#000000;'>Olá, " + emailToken.getUsuario().getNome() + " " + senha + "</h2>"
						+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>Informamos que foi solicitado na Plataforma do Programa Cidades Sustentáveis um pedido de alteração de sua senha de acesso. </p>"
						+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>Clique no link abaixo para acessar a página de recadastramento de senha:</p>"
						+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>" + urlFormularioSenha + "</p>"
						+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>Caso você não tenha feito esta solicitação de alteração de senha, por favor desconsidere esta mensagem.</p>");	

	}

	public List<Usuario> getUsuariosPrefeitura(Long id) {
		List<Usuario> usuarios = repository.findByPrefeituraId(id);
		return usuarios;
	}
	
	public List<ItemComboDTO> buscarComboBoxUsuarioSemPrefeitura() {
		return repository.buscarComboBoxUsuarioSemPrefeitura();
	}
	
	public List<ItemComboDTO> buscarComboBoxUsuarioDePrefeitura() {
		return repository.buscarComboBoxUsuarioDePrefeitura();
	}	
	
	public List<UsuarioDTO> findUsuariosRecebeEmailTrue(){
		return repository.findUsuariosRecebeEmailTrue();
	}

	public List<UsuarioDTO> findUsuariosRecebeEmailTrueECredencialNaoExcluida(){
		return repository.findUsuariosRecebeEmailTrueECredencialNaoExcluida();
	}
	
	public List<UsuarioDTO> buscarPorPerfil(Long idPerfil){
		return repository.buscarPorPerfil(idPerfil);
	}
	
	public int countUsuariosPrefeitura(Long id) {
		List<UsuarioDTO> usuarios = repository.countUsuariosPrefeitura(id);
		return usuarios.size();
	}
	
	public List<UsuarioDTO> buscarListaUsuariosPorPrefeitura(Long idPrefeitura){
		return repository.buscarListaUsuariosPorPrefeitura(idPrefeitura);
	}
	
	public void criarUsuariosPrefeitosScript() {
		List<Prefeitura> listaPrefeiturasSig = prefeituraService.listarDadosCadastraisPrefeiturasSignatariasVigentes();
		
		List<UsuarioDTO> listaUsuariosDTO = new ArrayList<>();
		
		List<PerfilDTO> listaPerfilDTO = new ArrayList<>();
		List<Perfil> listaPerfis = perfilService.buscarPerfilGestaoPublica();
		
		for(Perfil perfil : listaPerfis) {
			if(perfil.getNome().equalsIgnoreCase("Responsável pela Prefeitura")) {
				listaPerfilDTO.add(new PerfilDTO(perfil));
			}
		}
		
		for(Prefeitura prefeitura : listaPrefeiturasSig) {
			UsuarioDTO usuarioDTO = new UsuarioDTO();
			CredencialDTO credencialDTO = new CredencialDTO();
			credencialDTO.setListaPerfil(listaPerfilDTO);
			
			usuarioDTO.setNome(prefeitura.getNome());
			usuarioDTO.setEmail(prefeitura.getEmail());
			usuarioDTO.setTelefone_fixo(prefeitura.getTelefone());
			usuarioDTO.setTelefone(prefeitura.getTelefone());
			usuarioDTO.setCargo(prefeitura.getCargo());
			usuarioDTO.setPrefeitura(new PrefeituraDTO(prefeitura));
			usuarioDTO.setRecebeEmail(true);
			usuarioDTO.setCredencial(credencialDTO);
			usuarioDTO.setCidadeInteresse(prefeitura.getCidade().getNome());
			
			listaUsuariosDTO.add(usuarioDTO);
		}
		
		try {
			this.inserirResponsaveisScript(listaUsuariosDTO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void inserirResponsaveisScript(List<UsuarioDTO> usuariosDto)  {

		Prefeitura prefeitura = null;
		for (UsuarioDTO usuarioDTO : usuariosDto) {
			Usuario usuario = usuarioDTO.toEntityInsert();
			recuperaReferenciaPerfil(usuarioDTO, usuario);
			recuperaReferenciaPrefeitura(usuarioDTO, usuario);
			usuario.getCredencial().setLogin(usuario.getEmail());
			
			try {
				salvar(usuario);
			
				prefeitura = usuario.getPrefeitura();
			

				EmailToken emailToken = EmailToken.builder().ativo(Boolean.TRUE)
					.funcionalidadeToken(FuncionalidadeToken.GESTAO_PUBLICA)
					.hash(SenhaUtil.criptografarSHA2(usuario.getId() + "" + usuario.getNome() + "" + usuario.getEmail()
							+ "" + LocalDateTime.now().getNano()))
					.usuario(usuario).build();
				
				emailTokenService.salvar(emailToken);
			
				emailUsuarioGestaoPublicaSCRIPT(emailToken);
			
			} catch (Exception e) {
				System.out.println(e.getMessage());		
			}
		}
		
		EmailToken token = emailTokenService.buscarPorPrefeitura(prefeitura);
		
		if(token != null && token.getAtivo() == true) {
			emailTokenService.inativarTokenAprovacaoPrefeituraPrefeitura(prefeitura);
		}
	}
	
	private void emailUsuarioGestaoPublicaSCRIPT(EmailToken emailToken) throws EmailException {
		String senhorA = emailToken.getUsuario().getCargo().equalsIgnoreCase("Prefeito") ? "Sr." : "Sra.";
		String bemVindoA = emailToken.getUsuario().getCargo().equalsIgnoreCase("Prefeito") ? "Bem-vindo" : "Bem-vinda";
		String urlPCS = profileUtil.getProperty("profile.frontend");
		String urlFormularioSenha = urlPCS + "/cadsenha?token=" + emailToken.getHash();
		List<String> emails = new ArrayList<String>();
		emails.add(emailToken.getUsuario().getEmail());
		emailUtil.enviarEmailHTML(emails, "PCS - Usuário cadastrado!",
				"<h2 style='font-family:Arial, Helvetica, sans-serif; color:#000000;'>Prezado " + senhorA + " "
						+ emailToken.getUsuario().getNome() + "</h2>"
						+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>" + bemVindoA + " ao Programa Cidades Sustentáveis!</p>"
						+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>O Programa Cidades Sustentáveis disponibiliza agora um usuário para que a Prefeitura possa gerenciar os responsáveis por cadastro de conteúdos na Plataforma. "
						+ "Para esta administração de usuários, o " + senhorA + " deverá cadastrar uma nova senha. </p> "
						+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>Utilize seu email: "
						+ emailToken.getUsuario().getEmail() + " para realizar login no sistema.</p>"
						+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>Clique <a href=\""
						+ urlFormularioSenha + "\">aqui</a> para cadastrar sua senha, ou no link abaixo:  </p>"
						+ "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'> <a href='"
						+ urlFormularioSenha + "'>Formulário de cadastro de senha</a></p>");
	}
}
