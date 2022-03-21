package br.org.cidadessustentaveis.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vividsolutions.jts.io.ParseException;

import br.org.cidadessustentaveis.dto.ComentarioDiscussaoDTO;
import br.org.cidadessustentaveis.dto.ForumDiscussaoDTO;
import br.org.cidadessustentaveis.dto.ForumDiscussaoListDTO;
import br.org.cidadessustentaveis.dto.ForumDiscussaoPrincipalDTO;
import br.org.cidadessustentaveis.model.administracao.Perfil;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.biblioteca.TemaForum;
import br.org.cidadessustentaveis.model.participacaoCidada.DiscussaoPerfil;
import br.org.cidadessustentaveis.model.participacaoCidada.ForumDiscussao;
import br.org.cidadessustentaveis.repository.ForumDiscussaoRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;

@Service
public class ForumDiscussaoService {
	
	@Autowired
	private ForumDiscussaoRepository repository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PrefeituraService prefeituraService;
	
	@Autowired
	private UsuarioContextUtil usuarioUtil;
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private ComentarioDiscussaoService comentarioDiscussaoService;
	
	public ForumDiscussao buscarPorId(Long id) {
		Optional<ForumDiscussao> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Discussão não encontrada!"));
	}
	
	public boolean usuarioTemPermissao(ForumDiscussao discussaoRef, String autorizacao) {
		try {
			Usuario usuarioLogado = usuarioUtil.getUsuario();
			List<Perfil> perfisUsuarioLogado = usuarioUtil.getUsuario().getCredencial().getListaPerfil();
			List<DiscussaoPerfil> perfis = discussaoRef.getDiscussaoPerfis();

			if (discussaoRef.getUsuarioCadastrouDiscussao() != null && discussaoRef.getUsuarioCadastrouDiscussao().equals(usuarioLogado.getId())) {
				return true;
			}

			if (!perfis.isEmpty()) {	
					for(DiscussaoPerfil perfil: perfis) {
						for(Perfil perfilUsuarioLogado: perfisUsuarioLogado) {
							if (perfil.getAutorizacao().contains(autorizacao)) {
								if (perfil.getPerfil().getId() == perfilUsuarioLogado.getId() || perfilUsuarioLogado.getId() == 1) {									
									 return true;
								}
							}
						}
					}
					return false;
			} else {
				return false;
			}
		}
		catch(Exception e) {	
			return false;
		}
	}

	public ForumDiscussaoDTO buscarDiscussaoPorId(Long id) throws Exception  {
		ForumDiscussao discussaoRef = buscarPorId(id);	

			if (discussaoRef.getPublico() == true) {			
				return new ForumDiscussaoDTO(discussaoRef);
			}
			else {
				if (usuarioTemPermissao(discussaoRef, "acessar")) {					
					return new ForumDiscussaoDTO(discussaoRef);
				}
				else {					
					ForumDiscussaoDTO discussao = new ForumDiscussaoDTO();
					discussao.setId(null);
					return discussao;
				}
				
			}
	}

	public ForumDiscussao salvar(ForumDiscussaoDTO dto) throws Exception {
		if (dto.getPrefeituraId() != null) {
			Prefeitura prefeitura = prefeituraService.buscarPorId(dto.getPrefeituraId());
			dto.setPrefeitura(prefeitura);
		}
		
		ForumDiscussao forum = dto.toEntityInsert();
		
		return repository.save(forum);
	}
	
	public ForumDiscussao atualizarDiscussao(ForumDiscussaoDTO forumDiscussaoDTO) throws Exception {
		if (forumDiscussaoDTO.getId() == null) {
			throw new Exception("Campo id divergente.");
		}
		List<ComentarioDiscussaoDTO> comentariosDiscussao = comentarioDiscussaoService.buscarComentariosDiscussaoPorIdDiscussao(forumDiscussaoDTO.getId());
		forumDiscussaoDTO.setNumeroDeRespostas(comentariosDiscussao != null ? (long)comentariosDiscussao.size() : (long)0);
		
		ForumDiscussao forumDiscussao = forumDiscussaoDTO.toEntityUpdate(buscarPorId(forumDiscussaoDTO.getId()));
		forumDiscussao.setUsuarioUltimaPostagem(forumDiscussaoDTO.getUsuarioUltimaPostagem() != null ? usuarioService.buscarPorId(forumDiscussaoDTO.getUsuarioUltimaPostagem().getId()) : null);
		forumDiscussao.setNumeroDeRespostas(forumDiscussaoDTO.getNumeroDeRespostas());
		return repository.save(forumDiscussao);
	}
	
	public ForumDiscussao atualizarVisualizacao(ForumDiscussaoDTO forumDiscussaoDTO) throws Exception {
		if (forumDiscussaoDTO.getId() == null) {
			throw new Exception("Campo id divergente.");
		}
		ForumDiscussao forumDiscussao = buscarPorId(forumDiscussaoDTO.getId());
		forumDiscussao.setNumeroDeVisualizacao(forumDiscussao.getNumeroDeVisualizacao() != null ? forumDiscussao.getNumeroDeVisualizacao() + 1 : 1);
		return repository.save(forumDiscussao);
	}
	
	public List<ForumDiscussaoPrincipalDTO> buscarDiscussoesToList() {
		List<ForumDiscussaoPrincipalDTO> listaCompleta = repository.buscarDiscussoesToList();
		List<ForumDiscussaoPrincipalDTO> listaPublica = new ArrayList<ForumDiscussaoPrincipalDTO>();
		for (ForumDiscussaoPrincipalDTO forumDiscussaoPrincipalDTO : listaCompleta) {
			if(forumDiscussaoPrincipalDTO.getPublico()) {
				listaPublica.add(forumDiscussaoPrincipalDTO);
			}
		}
		return listaPublica;
	}
	
	public List<ForumDiscussaoPrincipalDTO> buscarDiscussoesFiltradas(String palavraChave, List<Long> idsTemas) throws ParseException {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<ForumDiscussaoPrincipalDTO> query = cb.createQuery(ForumDiscussaoPrincipalDTO.class);
		
		Root<ForumDiscussao> forumDiscussao = query.from(ForumDiscussao.class);
		
		Join<ForumDiscussao, TemaForum> joinTemaForum = forumDiscussao.join("temasForum", JoinType.LEFT);
		Join<ForumDiscussao, Usuario> joinUsuarioUltimaPostagem = forumDiscussao.join("usuarioUltimaPostagem", JoinType.LEFT);
		
		query.multiselect(forumDiscussao.get("id"), forumDiscussao.get("ativo"), forumDiscussao.get("dataHoraCriacao"), forumDiscussao.get("titulo"), forumDiscussao.get("numeroDeRespostas"),
				forumDiscussao.get("numeroDeVisualizacao"), joinUsuarioUltimaPostagem, forumDiscussao.get("publico")).distinct(true);
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		if(idsTemas != null && !idsTemas.isEmpty()) {
			In<Long> inClause = cb.in(joinTemaForum.get("id"));
			
			idsTemas.forEach(idTema -> {				
				inClause.value(idTema);
				predicateList.add(inClause);
			});		
		}
		
		if (palavraChave != null && !palavraChave.equals("")) {
			Path<String> tituloForumDiscussao = forumDiscussao.get("titulo");
			Path<String> descricaoForumDiscussao = forumDiscussao.get("descricao");
			Predicate predicateForTitulo = cb.like(cb.lower(tituloForumDiscussao), "%" + palavraChave.toLowerCase() + "%");
			Predicate predicateForSubtitulo = cb.like(cb.lower(descricaoForumDiscussao), "%" + palavraChave.toLowerCase() + "%");
			predicateList.add(cb.or(predicateForTitulo, predicateForSubtitulo));
		}
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.desc(forumDiscussao.get("id")));
		query.orderBy(orderList);
		
		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		TypedQuery<ForumDiscussaoPrincipalDTO> typedQuery = em.createQuery(query);
		List<ForumDiscussaoPrincipalDTO> lista = typedQuery.getResultList();
		
		return lista;		
	}

	public void excluir(Long id) {
		repository.deleteById(id);
	}
	

	public ForumDiscussao alterar( ForumDiscussaoDTO forumDiscussaoDTO) throws Exception {
		if (forumDiscussaoDTO.getId() == null) {
			throw new Exception("Campo id divergente.");
		}
	    
		ForumDiscussao forumDiscussao = forumDiscussaoDTO.toEntityUpdate(buscarPorId(forumDiscussaoDTO.getId()));
	    		
		
		repository.save(forumDiscussao);
		
		return forumDiscussao;
	}
	
	
	public ForumDiscussaoDTO buscarForumDiscussaoEditar(Long id) {
		ForumDiscussao forumDiscussao = buscarPorId(id);		
		ForumDiscussaoDTO forumDiscussaoDTO = new ForumDiscussaoDTO(forumDiscussao);
		return forumDiscussaoDTO;	
	}
	
	public List<ForumDiscussaoListDTO> buscarListaDiscussoes() {
		List<ForumDiscussaoListDTO> lista = repository.buscarListaDiscussoes();
		return lista;
	}
	
	public List<ForumDiscussaoListDTO> buscarListaDiscussoesPorIdPrefeitura(Long idPrefeitura) {
		List<ForumDiscussaoListDTO> lista = repository.buscarListaDiscussoesPorIdPrefeitura(idPrefeitura);
		return lista;
	}
	
	public ForumDiscussao atualizarUsuarioUltimaPostagem(ForumDiscussaoDTO forumDiscussaoDTO) throws Exception {
		if (forumDiscussaoDTO.getId() == null) {
			throw new Exception("Campo id divergente.");
		}
		ForumDiscussao forumDiscussao = buscarPorId(forumDiscussaoDTO.getId());
		forumDiscussao.setUsuarioUltimaPostagem(forumDiscussaoDTO.getUsuarioUltimaPostagem() != null ? forumDiscussaoDTO.getUsuarioUltimaPostagem().toEntityUpdateUltimoComentario(): null);
		return repository.save(forumDiscussao);
	}
}
