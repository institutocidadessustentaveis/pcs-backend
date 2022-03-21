package br.org.cidadessustentaveis.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.boaspraticas.SugestaoBoasPraticas;
import br.org.cidadessustentaveis.repository.SugestaoBoasPraticasRepository;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;

@Service
public class SugestaoBoasPraticasService {
	@Autowired
	private SugestaoBoasPraticasRepository repository;
	

	@Autowired
	private UsuarioContextUtil usuarioContextUtil;
	
	//INSERE A BOA PRÁTICA NO BANCO
	public SugestaoBoasPraticas inserirSugestaoBoasPraticas(SugestaoBoasPraticas sugestaoBoasPraticas) throws Exception {
		sugestaoBoasPraticas.setUsuario(usuarioContextUtil.getUsuario());
		repository.save(sugestaoBoasPraticas);
		return sugestaoBoasPraticas;
	}
	
	//BUSCA TODAS AS SUGESTÕES NO BANCO
	public Page<SugestaoBoasPraticas> buscar(Integer page, Integer linesPerPage) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage);
		return repository.findAllSugestaoBoasPraticas(pageRequest);
	}
	
	//BUSCA SUGESTÃO BOA PRÁTICA POR ID
	public Optional<SugestaoBoasPraticas> buscarPorId(Long id) {
		return repository.findById(id);
	}
	
	//DELETA SUGESTÃO BOA PRÁTICA POR ID
	public void deletar(Long id) {
		buscarPorId(id);
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Erro ao deletar o registro");
		}
	}
	
	
//	
//	public Page<Pais> buscarComPaginacao(Integer page, Integer linesPerPage, String orderBy, String direction){
//		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
//		return repository.findAll(pageRequest);
//	}
//
//	public Pais buscarPorId(Long id) {
//		Optional<Pais> obj = repository.findById(id);
//		return obj.orElseThrow(() -> new ObjectNotFoundException("País não encontrado!"));
//	}
//	
//	public Optional<Pais> buscarPorNomePais(final String paisNome) {
//	  return repository.findByNome(paisNome);
//	}
//
//	public List<Pais> buscarPorNomeLike(String nome, int page, int linesPerPage) {
//		return repository.findByNomeLike(nome, PageRequest.of(page, linesPerPage)).getContent();
//	}
//
//	public Pais editar(PaisDTO paisDto, Long id) {
//		Pais userRef = buscarPorId(id);
//		userRef = paisDto.toEntityUpdate(userRef);
//		return repository.save(userRef);
//	}
//	
//	public void deletar(Long id) {
//		buscarPorId(id);
//		try {
//			repository.deleteById(id);
//		}catch (DataIntegrityViolationException e) {
//			throw new DataIntegrityException("O registro está relacionado com outra entidade");
//		}	
//	}
//
//	public Long contar() {
//		return repository.count();
//	}

}
