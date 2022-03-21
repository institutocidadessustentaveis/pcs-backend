package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.ComentarioDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Comentario;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.repository.ComentarioRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class ComentarioService {

	@Autowired
	private CidadeService cidadeService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired 
	private PrefeituraService prefeituraService;
	
	@Autowired
	private ComentarioRepository comentarioRepository;
	
	@Autowired
	private EntityManager em;
	
	public Comentario buscarPorId(Long id) {
		Optional<Comentario> obj = comentarioRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Comentario não encontrado!"));
	}
	
	public ComentarioDTO buscarComentarioPorId(Long id) {
		Comentario comentarioRef = buscarPorId(id);
		return new ComentarioDTO(comentarioRef);
	}
	
	public List<ComentarioDTO> buscarComentariosToList(Long idUsuario){
		Prefeitura prefeitura = prefeituraService.buscarPrefeituraPorIdUsuario(idUsuario);
		List<ComentarioDTO> comentario;
		
		if (prefeitura != null) {
			comentario = comentarioRepository.buscarComentariosToList(idUsuario);
		}
		else {
			comentario = comentarioRepository.buscarComentariosToListAdmin();
		}
		return comentario;
	}
	
	public List<ComentarioDTO> buscarComentariosToListPublica() {
		List<ComentarioDTO> comentario;
		comentario = comentarioRepository.buscarComentariosToListPublica();
		return comentario;
	}
	
	public Comentario inserir(ComentarioDTO comentarioDTO) {
		
		Comentario comentario = comentarioDTO.toEntityInsert(comentarioDTO);

		comentario.setCidade(comentarioDTO.getIdCidade() != null ? cidadeService.buscarPorId(comentarioDTO.getIdCidade()) : null);
		comentario.setPrefeitura(comentarioDTO.getIdPrefeitura() != null ? prefeituraService.buscarPorId(comentarioDTO.getIdPrefeitura()) : null);
		comentario.setUsuario(comentarioDTO.getUsuario() != null ? usuarioService.buscarPorId(comentarioDTO.getUsuario()): null);
		comentario.setHorarioPublicacao(LocalTime.now());
		comentario.setDataPublicacao(LocalDate.now());
		
		
		return comentarioRepository.save(comentario);
	}
	
	public Comentario alterar(ComentarioDTO comentarioDTO) throws Exception {
		if (comentarioDTO.getId() == null) {
			throw new Exception("Campo id divergente.");
		}
		Comentario comentario = comentarioDTO.toEntityUpdate(buscarPorId(comentarioDTO.getId()));
		comentario.setCidade(comentarioDTO.getIdCidade() != null ? cidadeService.buscarPorId(comentarioDTO.getIdCidade()) : null);
		comentario.setPrefeitura(comentarioDTO.getIdPrefeitura() != null ? prefeituraService.buscarPorId(comentarioDTO.getIdPrefeitura()) : null);
		comentario.setUsuario(comentarioDTO.getUsuario() != null ? usuarioService.buscarPorId(comentarioDTO.getUsuario()): null);
		
		return comentarioRepository.save(comentario);
	}
	
	public void deletar(Long id) {
		comentarioRepository.deleteById(id);
	}
	
	public List<ComentarioDTO> buscarComentarioFiltrado(String palavraChave, String dataInicial, String dataFinal, Long idCidade) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<ComentarioDTO> query = cb.createQuery(ComentarioDTO.class);
		
		Root<Comentario> comentario = query.from(Comentario.class);
		
		Join<Comentario, Cidade> joinCidade = comentario.join("cidade",JoinType.LEFT);
		Join<Comentario, Usuario> joinUsuario = comentario.join("usuario",JoinType.LEFT);
		
		
		query.multiselect(comentario.get("id"), joinUsuario.get("id"), comentario.get("comentario"), comentario.get("titulo"),  comentario.get("email"), comentario.get("telefone"), 
				joinCidade.get("id"), joinCidade.get("nome"), joinUsuario.get("nome"), comentario.get("dataPublicacao"), comentario.get("horarioPublicacao"));
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		if (idCidade != null) {
			Path<Long> cidadeComentario = comentario.get("cidade");
			predicateList.add(cb.equal(cidadeComentario, idCidade));
		}		
		
		if (palavraChave != null && !palavraChave.equals("")) {
			Path<String> tituloComentario = comentario.get("titulo");
			Path<String> comentarioTexto = comentario.get("comentario");
			Predicate predicateForTitulo = cb.like(cb.lower(tituloComentario), "%" + palavraChave.toLowerCase() + "%");
			Predicate predicateForSubtitulo = cb.like(cb.lower(comentarioTexto), "%" + palavraChave.toLowerCase() + "%");
			predicateList.add(cb.or(predicateForTitulo, predicateForSubtitulo));
		}
		
		if(dataInicial != null && !dataInicial.equals("")) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, comentario.get("dataPublicacao"));
			LocalDate dataInicialFormatada = LocalDate.parse(dataInicial, df);
			predicateList.add(cb.greaterThanOrEqualTo(campoDataHora, dataInicialFormatada));
		}
		
		if(dataFinal != null && !dataFinal.equals("")) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, comentario.get("dataPublicacao"));
			LocalDate dataFinalFormatada = LocalDate.parse(dataFinal, df);
			predicateList.add(cb.lessThanOrEqualTo(campoDataHora, dataFinalFormatada));
		}

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<ComentarioDTO> typedQuery = em.createQuery(query);
		List<ComentarioDTO> lista = typedQuery.getResultList();

		return lista;
	}
}
