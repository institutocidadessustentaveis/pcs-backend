package br.org.cidadessustentaveis.services;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.EixoDTO;
import br.org.cidadessustentaveis.dto.EixoListagemDTO;
import br.org.cidadessustentaveis.dto.EixoParaComboDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.repository.EixoRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class EixoService {

	@Autowired
	private EixoRepository eixoRepository;
	
	public List<Eixo> listar() {
		return eixoRepository.findAllByOrderByNomeAsc();
	}
	
	public Eixo listarById(final Long id) {
	  Optional<Eixo> eixo = eixoRepository.findById(id);
	  return eixo.orElseThrow(() -> new ObjectNotFoundException("Eixo não encontrado!"));
	}
	
	public List<Eixo> buscarEixosPorIds(List<Long> ids) {
		return eixoRepository.buscarEixosPorIds(ids);
	}
	
	public Eixo inserir(EixoDTO eixoDTO) {
		return eixoRepository.save(eixoDTO.toEntityInsert());
	}
	
	public Eixo alterar(final Long id, final EixoDTO eixoDTO) throws Exception {
		if (!(id == eixoDTO.getId())) {
			throw new Exception("Campo id divergente.");
		}
		Eixo eixo = listarById(id);
		return eixoRepository.saveAndFlush(eixoDTO.toEntityUpdate(eixo));
	}
	
	public void deletar(final Long id) throws Exception {
		listarById(id);
		try {
			eixoRepository.deleteById(id);			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public List<EixoParaComboDTO> buscarEixosCombo(Boolean comListaIndicadores) {
		List<Eixo> listaEixo = listar();
		List<EixoParaComboDTO> listaEixoDto = listaEixo.stream().map(eixo -> new EixoParaComboDTO(eixo, comListaIndicadores)).collect(Collectors.toList());
		listaEixoDto = listaEixoDto.stream().sorted(Comparator.comparing(EixoParaComboDTO::getNome)).collect(Collectors.toList());
		return listaEixoDto;
	}
	
	public List<ItemComboDTO> buscarEixosParaComboBox() {
		return eixoRepository.buscarEixosParaComboBox();
	}

	public List<EixoDTO> buscarEixosDto() {
		return eixoRepository.buscarEixosDto();
	}
	
	public List<EixoListagemDTO> buscarEixosList() {
		return eixoRepository.buscarEixosList();
	}
	
}
