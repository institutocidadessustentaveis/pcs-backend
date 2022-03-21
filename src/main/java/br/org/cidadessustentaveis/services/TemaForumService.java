package br.org.cidadessustentaveis.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.TemaForumDTO;
import br.org.cidadessustentaveis.model.biblioteca.TemaForum;
import br.org.cidadessustentaveis.repository.TemaForumRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class TemaForumService {

	@Autowired
	private TemaForumRepository temaForumRepository;
	
	public TemaForum buscarPorId(Long id) {
		Optional<TemaForum> obj = temaForumRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Tema de fórum não encontrado!"));
	}
	
	public void cadastrar(TemaForumDTO temaForumDTO) {
		
		TemaForum temaForum = temaForumDTO.toEntityInsert();
		
		temaForumRepository.save(temaForum);
	}
	
	public TemaForum editar(TemaForumDTO temaForumDTO) throws Exception {
		if (temaForumDTO.getId() == null) {
			throw new Exception("Campo id divergente.");
		}
	    
		TemaForum temaForum = temaForumDTO.toEntityUpdate(buscarPorId(temaForumDTO.getId()));
	    		
		temaForumRepository.save(temaForum);
		
		return temaForum;
	}
	
	public List<TemaForumDTO> buscarListaTemaForum() {
		List<TemaForumDTO> listaTemaForum = temaForumRepository.buscarListaTemaForum();
		return listaTemaForum;
	}
	
	public void excluir(Long id) {
		temaForumRepository.deleteById(id);
	}
	
	public TemaForumDTO buscarTemaForumParaEditar(Long id) {
		TemaForum temaForum = buscarPorId(id);
		TemaForumDTO temaForumDTO = new TemaForumDTO(temaForum);
		
		return temaForumDTO;
	}
	
	
}
