package br.org.cidadessustentaveis.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.PartidoPoliticoDTO;
import br.org.cidadessustentaveis.model.administracao.PartidoPolitico;
import br.org.cidadessustentaveis.repository.PartidoPoliticoRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class PartidoPoliticoService {

	@Autowired
	private PartidoPoliticoRepository partidoRepository;

	public List<PartidoPolitico> listar() {
		return partidoRepository.findAllByOrderBySiglaPartidoAsc();
	}

	public PartidoPolitico listarPorId(final Long id) {
		Optional<PartidoPolitico> partido = partidoRepository.findById(id);
		return partido.orElseThrow(() -> new ObjectNotFoundException("Partido não encontrado!"));
	}

	public PartidoPolitico inserir(final PartidoPoliticoDTO partidoDTO) {
		return partidoRepository.save(partidoDTO.toEntityInsert());
	}

	public PartidoPolitico alterar(final Long id, final PartidoPoliticoDTO partidoDTO) throws Exception {
		if (!(id == partidoDTO.getId())) {
			throw new Exception("Campo id divergente.");
		}
		PartidoPolitico partido = listarPorId(id);
		return partidoRepository.saveAndFlush(partidoDTO.toEntityUpdate(partido));
	}

	public void deletar(final Long id) {
		listarPorId(id);
		partidoRepository.deleteById(id);
	}

	@CachePut("comboPartido")
	public List<ItemComboDTO> buscarItemCombo() {
		return partidoRepository.buscarItemCombo();
	}

	public PartidoPolitico findBySigla(String sigla){
		PartidoPolitico partido = partidoRepository.findBySiglaPartido(sigla);
		return partido;
	}

}
