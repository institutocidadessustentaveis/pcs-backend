package br.org.cidadessustentaveis.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.ExibirProvinciaEstadoDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.ProvinciaEstadoDTO;
import br.org.cidadessustentaveis.dto.ProviniciaEstadoBuscaDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.repository.ProvinciaEstadoRepository;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class ProvinciaEstadoService {
	
	@Autowired
	private ProvinciaEstadoRepository repository;
	
	@Autowired
	private PaisService paisService;
	
	public ProvinciaEstado inserirProvinciaEstado(ProvinciaEstadoDTO provinciaEstadoDto) {
		ProvinciaEstado provinciaEstado = provinciaEstadoDto.toEntityInsert();
		for (Cidade cidade: provinciaEstado.getCidades()) {
		  cidade.setProvinciaEstado(provinciaEstado);
		}
		return repository.save(provinciaEstado);
	}
	
	public List<ExibirProvinciaEstadoDTO> buscar() {
		List<ProvinciaEstado> lista = repository.findAll();
		List<ExibirProvinciaEstadoDTO> provinciaEstadoDto = lista.stream().map(obj -> new ExibirProvinciaEstadoDTO(obj))
				.collect(Collectors.toList());
		return provinciaEstadoDto;
	}
	
	public List<ProvinciaEstado> buscarEstadosDoBrasil() {
		return repository.findAllByPaisNomeOrderByNomeAsc("brasil");
	}
	
	public List<ProvinciaEstado> buscarComboBox() {
		return repository.findComboBoxProvinciaEstado();
	}
	
	public Page<ProvinciaEstado> buscarComPaginacao(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}
	
	public ProvinciaEstado buscarPorId(Long id) {
		Optional<ProvinciaEstado> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Provincia/Estado não encontrado!"));
	}

	public List<ProvinciaEstado> buscarPorNomeLike(String nome, int page, int linesPerPage) {
		return repository.findByNomeLike(nome.toLowerCase(), 
											PageRequest.of(page, linesPerPage)).getContent();
	}
	
	public ProviniciaEstadoBuscaDTO buscarIdPorNome(String nome) {
		ProviniciaEstadoBuscaDTO obj = repository.buscarIdPorNome(nome);
		return obj;
	}

	
	public List<ProvinciaEstado> buscarPorPais(Pais pais) {
		return repository.findByPaisOrderByNomeAsc(pais);
	}

	public ProvinciaEstado editar(ProvinciaEstadoDTO provinciaEstadoDto, Long id) {
		ProvinciaEstado provinciaEstado = buscarPorId(id);
		provinciaEstado.setSigla(provinciaEstadoDto.getSigla());
		provinciaEstado.getPais().getEstados().add(provinciaEstado);
		for (Cidade cidade: provinciaEstado.getCidades()) {
			cidade.setProvinciaEstado(provinciaEstado);
		}
		provinciaEstado= provinciaEstadoDto.toEntityUpdate(provinciaEstado);

		return repository.save(provinciaEstado);
	}

	public void deletar(Long id) {
		buscarPorId(id);
		try {
			repository.deleteById(id);
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("O registro está relacionado com outra entidade");
		}	
	}

	public Optional<ProvinciaEstado> buscarPorNome(final String nome) {
		return repository.findByNome(nome);
	}

	public Long contar() {
		return repository.count();
	}

	public List<ProvinciaEstado> buscarPorPais(Long idPais) {
		Pais paisParam = paisService.buscarPorId(idPais);
		repository.findByPaisOrderByNomeAsc(paisParam);
		return repository.findByPaisOrderByNomeAsc(paisParam);
	}
	
	public List<ItemComboDTO> buscarPorPaisResumido(Long idPais){		
		List<ProvinciaEstadoDTO> estados = repository.buscarPorPaisResumido(idPais);		
		List<ItemComboDTO> provinciaEstadoDto = estados.stream().map(obj -> new ItemComboDTO(obj.getId(), obj.getNome()))
				.collect(Collectors.toList());
		
		return provinciaEstadoDto;
	}
	
	public List<String> buscarPorPaisAutoComplete(Long idPais) {
		List<String> nomesProvinciaEstado = repository.buscarNomeProvinciaEstadoPorIdPais(idPais);
		return nomesProvinciaEstado;
	}
	
	public List<ProvinciaEstado> buscarComboBoxEstadoBrasil() {
		return repository.findComboBoxProvinciaEstadoBrasil();
	}

}
