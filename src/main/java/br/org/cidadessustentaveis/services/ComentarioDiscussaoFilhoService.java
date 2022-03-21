package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.ComentarioDiscussaoDTO;
import br.org.cidadessustentaveis.dto.ComentarioDiscussaoFilhoDTO;
import br.org.cidadessustentaveis.model.administracao.Alerta;
import br.org.cidadessustentaveis.model.administracao.ComentarioDiscussao;
import br.org.cidadessustentaveis.model.administracao.ComentarioDiscussaoFilho;
import br.org.cidadessustentaveis.model.enums.TipoAlerta;
import br.org.cidadessustentaveis.model.participacaoCidada.ForumDiscussao;
import br.org.cidadessustentaveis.repository.ComentarioDiscussaoFilhoRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class ComentarioDiscussaoFilhoService {
	
	@Autowired
	ForumDiscussaoService forumDiscussaoService;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	ComentarioDiscussaoFilhoRepository repository;
	
	@Autowired
	AlertaService alertaService;
	
	@Autowired
	ComentarioDiscussaoService comentarioDiscussaoService;
	
	public ComentarioDiscussaoFilho inserir(ComentarioDiscussaoFilhoDTO comentarioDiscussaoDTO) {
		
		ForumDiscussao forumDiscussao = comentarioDiscussaoDTO.getDiscussao() != null ? forumDiscussaoService.buscarPorId(comentarioDiscussaoDTO.getDiscussao()) : null;
		
		ComentarioDiscussaoFilho comentarioDiscussao = comentarioDiscussaoDTO.toEntityInsert(comentarioDiscussaoDTO);
		comentarioDiscussao.setUsuario(comentarioDiscussaoDTO.getUsuario() != null ? usuarioService.buscarPorId(comentarioDiscussaoDTO.getUsuario()): null);
		comentarioDiscussao.setComentarioPai(comentarioDiscussaoDTO.getComentarioPai() != null ? comentarioDiscussaoService.buscarPorId(comentarioDiscussaoDTO.getComentarioPai()) : null);
		comentarioDiscussao.setNomeUsuario(comentarioDiscussao.getUsuario() != null ? comentarioDiscussao.getUsuario().getNome(): null);

		comentarioDiscussao.setForumDiscussao(forumDiscussao);
		comentarioDiscussao.setDataPublicacao(LocalDate.now());
		comentarioDiscussao.setHorarioPublicacao(LocalTime.now());
		
		alertaService.salvar(Alerta.builder()
				.mensagem("Seu comentario no fórum '" + forumDiscussao.getTitulo() + "' foi respondido." )
					.link("/participacao-cidada/forum/discussao/" + forumDiscussao.getId())
					.tipoAlerta(TipoAlerta.RESPOSTA_COMENTARIO_FORUM)
					.data(LocalDateTime.now())
					.build());
		
		return repository.save(comentarioDiscussao);
	}
	
	public List<ComentarioDiscussaoFilhoDTO> buscarComentariosFilhosPorIdComentarioPai(Long idComentarioPai) {
		return repository.buscarComentariosFilhosPorIdComentarioPai(idComentarioPai);
	}
	
	public ComentarioDiscussaoFilho alterar(ComentarioDiscussaoFilhoDTO comentarioDiscussaoDTO) throws Exception {
		if (comentarioDiscussaoDTO.getId() == null) {
			throw new Exception("Campo id divergente.");
		}
		ComentarioDiscussaoFilho comentarioDiscussao = comentarioDiscussaoDTO.toEntityUpdate(buscarPorId(comentarioDiscussaoDTO.getId()));
		comentarioDiscussao.setUsuario(comentarioDiscussaoDTO.getUsuario() != null ? usuarioService.buscarPorId(comentarioDiscussaoDTO.getUsuario()): null);
		
		return repository.save(comentarioDiscussao);
	}
	
	public ComentarioDiscussaoFilho buscarPorId(Long id) {
		Optional<ComentarioDiscussaoFilho> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Comentario não encontrado!"));
	}
	
	public void deletar(Long id) {
		repository.deleteById(id);
	}

}
