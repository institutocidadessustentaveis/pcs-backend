package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.ComentarioDiscussaoDTO;
import br.org.cidadessustentaveis.model.administracao.ComentarioDiscussao;
import br.org.cidadessustentaveis.repository.ComentarioDiscussaoRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class ComentarioDiscussaoService {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ForumDiscussaoService forumDiscussaoService;
	
	@Autowired
	private ComentarioDiscussaoRepository repository;
	
	public ComentarioDiscussao buscarPorId(Long id) {
		Optional<ComentarioDiscussao> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Comentario não encontrado!"));
	}
	
	public ComentarioDiscussaoDTO buscarComentarioDiscussaoPorId(Long id) {
		ComentarioDiscussao comentarioDiscussaoRef = buscarPorId(id);
		return new ComentarioDiscussaoDTO(comentarioDiscussaoRef);
	}
	
	public List<ComentarioDiscussaoDTO> buscarComentariosDiscussaoPorIdDiscussao(Long id) {
		List<ComentarioDiscussaoDTO> comentarioDiscussao;
		comentarioDiscussao = repository.buscarComentariosDiscussaoByIdDiscussao(id);
		return comentarioDiscussao;
	}
	
	public ComentarioDiscussao inserir(ComentarioDiscussaoDTO comentarioDiscussaoDTO) {
		ComentarioDiscussao comentarioDiscussao = comentarioDiscussaoDTO.toEntityInsert(comentarioDiscussaoDTO);
		comentarioDiscussao.setUsuario(comentarioDiscussaoDTO.getUsuario() != null ? usuarioService.buscarPorId(comentarioDiscussaoDTO.getUsuario()): null);
		comentarioDiscussao.setNomeUsuario(comentarioDiscussao.getUsuario() != null ? comentarioDiscussao.getUsuario().getNome(): null);

		comentarioDiscussao.setForumDiscussao(comentarioDiscussaoDTO.getDiscussao() != null ? forumDiscussaoService.buscarPorId(comentarioDiscussaoDTO.getDiscussao()): null);
		comentarioDiscussao.setDataPublicacao(LocalDate.now());
		comentarioDiscussao.setHorarioPublicacao(LocalTime.now());
		
		return repository.save(comentarioDiscussao);
	}
	
	public ComentarioDiscussao alterar(ComentarioDiscussaoDTO comentarioDiscussaoDTO) throws Exception {
		if (comentarioDiscussaoDTO.getId() == null) {
			throw new Exception("Campo id divergente.");
		}
		ComentarioDiscussao comentarioDiscussao = comentarioDiscussaoDTO.toEntityUpdate(buscarPorId(comentarioDiscussaoDTO.getId()));
		comentarioDiscussao.setUsuario(comentarioDiscussaoDTO.getUsuario() != null ? usuarioService.buscarPorId(comentarioDiscussaoDTO.getUsuario()): null);
		
		return repository.save(comentarioDiscussao);
	}
	
	public void deletar(Long id) {
		repository.deleteById(id);
	}
	
	public Long quantidadeComentarios() {
		return repository.count();
	}
	
	public List<ComentarioDiscussao> buscarDadosRelatorio() {
		return repository.findAll();
	}
}
