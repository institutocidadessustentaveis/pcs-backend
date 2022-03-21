package br.org.cidadessustentaveis.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.HistoricoCertificadoDTO;
import br.org.cidadessustentaveis.model.capacitacao.HistoricoCertificado;
import br.org.cidadessustentaveis.repository.HistoricoCertificadoRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class HistoricoCertificadoService {

	@Autowired
	private HistoricoCertificadoRepository repository;
	
	public HistoricoCertificado inserir(HistoricoCertificadoDTO historicoCertificadoDTO) {

		HistoricoCertificado historicoCertificado = historicoCertificadoDTO.toEntityInsert(historicoCertificadoDTO);
		
		repository.save(historicoCertificado);
		return historicoCertificado;
	}
	
	public HistoricoCertificado buscarPorId(Long id) {
		Optional<HistoricoCertificado> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Histórico de Certificado não encontrado!"));
	}
	
	public HistoricoCertificadoDTO buscarHistoricoCertificadoPorId(Long id) {
		HistoricoCertificado historicoCertificadoRef = buscarPorId(id);
		return new HistoricoCertificadoDTO(historicoCertificadoRef);
	}
	
	public List<HistoricoCertificadoDTO> buscarHistoricoCertificadoToList() {
		return repository.buscarHistoricoCertificadoToList();
	}

	public void deletar(Long id) {
		repository.deleteById(id);
	}

}
