package br.org.cidadessustentaveis.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.PaisDTO;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.repository.PaisRepository;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class PaisService {
	@Autowired
	private PaisRepository repository;
	
	public Pais inserirPais(PaisDTO paisDto) {
		Pais pais = paisDto.toEntityInsert();
		pais = repository.save(pais);
		return pais;
	}
	
	public List<Pais> buscar() {
		return repository.findAll();
	}
	
	public Page<Pais> buscarComPaginacao(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}

	public Pais buscarPorId(Long id) {
		Optional<Pais> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("País não encontrado!"));
	}	
	
	public Optional<Pais> buscarPorNomePais(final String paisNome) {
	  return repository.findByNome(paisNome);
	}

	public List<Pais> buscarPorNomeLike(String nome, int page, int linesPerPage) {
		return repository.findByNomeLike(nome, PageRequest.of(page, linesPerPage)).getContent();
	}

	public Pais editar(PaisDTO paisDto, Long id) {
		Pais userRef = buscarPorId(id);
		userRef = paisDto.toEntityUpdate(userRef);
		return repository.save(userRef);
	}
	
	public void deletar(Long id) {
		buscarPorId(id);
		try {
			repository.deleteById(id);
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("O registro está relacionado com outra entidade");
		}	
	}

	public Long contar() {
		return repository.count();
	}

	public List<Pais> buscarPaisesPorContinente(String continente) {		
		return repository.findByContinente(continente);
	}
	
	public List<ItemComboDTO> buscarPaisesCombo(){
		return repository.buscarPaisesCombo();
	}

}
