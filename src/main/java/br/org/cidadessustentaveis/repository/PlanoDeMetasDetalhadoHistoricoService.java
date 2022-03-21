package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.PlanoDeMetaHistoricoAnosDTO;

@Service
public class PlanoDeMetasDetalhadoHistoricoService {
	@Autowired
	private PlanoDeMetasDetalhadoHistoricoRepository repository;
	
	public List<PlanoDeMetaHistoricoAnosDTO> buscarHistoricoAnos(Long idPlano, Long idIndicador) {
		List<PlanoDeMetaHistoricoAnosDTO> planoDeMetaHistoricoAnosDTO;
		planoDeMetaHistoricoAnosDTO = repository.buscarHistoricoAnos(idPlano, idIndicador);
		return planoDeMetaHistoricoAnosDTO;
	}
}
