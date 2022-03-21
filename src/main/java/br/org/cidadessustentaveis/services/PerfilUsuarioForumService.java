package br.org.cidadessustentaveis.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.PerfilUsuarioForumDTO;
import br.org.cidadessustentaveis.model.administracao.PerfilUsuarioForum;
import br.org.cidadessustentaveis.repository.PerfilUsuarioForumRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class PerfilUsuarioForumService {

		@Autowired
		private  PerfilUsuarioForumRepository repository;
		
		@Autowired
		private CidadeService cidadeService;

		@Autowired
		private UsuarioService usuarioService;
		
		public PerfilUsuarioForum buscarPorId(Long id) {
			Optional<PerfilUsuarioForum> obj = repository.findById(id);
			return obj.orElseThrow(() -> new ObjectNotFoundException("Perfil não encontrado!"));
		}
		
		public PerfilUsuarioForumDTO buscarPerfilPorId(Long id) {
			PerfilUsuarioForum perfilRef = buscarPorId(id);
			return new PerfilUsuarioForumDTO(perfilRef);
		}
		
		public PerfilUsuarioForumDTO buscarPerfilPorIdUsuario(Long id) {
			return repository.buscarPerfilPorIdUsuario(id);
		}

		public PerfilUsuarioForum inserir(PerfilUsuarioForumDTO perfilUsuarioForumDTO) {		
			PerfilUsuarioForum perfilUsuarioForum = perfilUsuarioForumDTO.toEntityInsert(perfilUsuarioForumDTO);			
			perfilUsuarioForum.setCidade(perfilUsuarioForumDTO.getIdCidade() != null ? cidadeService.buscarPorId(perfilUsuarioForumDTO.getIdCidade()) : null);
	    	perfilUsuarioForum.setUsuario(perfilUsuarioForumDTO.getIdUsuario() != null ? usuarioService.buscarPorId(perfilUsuarioForumDTO.getIdUsuario()): null);
	  
			return repository.save(perfilUsuarioForum);
		}
		
		public PerfilUsuarioForum alterar(PerfilUsuarioForumDTO perfilUsuarioForumDTO) throws Exception {
			if (perfilUsuarioForumDTO.getId() == null) {
				throw new Exception("Campo id divergente.");
			}
			PerfilUsuarioForum perfilUsuarioForum = perfilUsuarioForumDTO.toEntityUpdate(buscarPorId(perfilUsuarioForumDTO.getId()));
			perfilUsuarioForum.setCidade(perfilUsuarioForumDTO.getIdCidade() != null ? cidadeService.buscarPorId(perfilUsuarioForumDTO.getIdCidade()) : null);
			perfilUsuarioForum.setUsuario(perfilUsuarioForumDTO.getIdUsuario() != null ? usuarioService.buscarPorId(perfilUsuarioForumDTO.getIdUsuario()): null);
			
			return repository.save(perfilUsuarioForum);
		}
		
		public void deletar(Long id) {
			repository.deleteById(id);
		}
}
