package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.SolicitacoesBoasPraticasDTO;
import br.org.cidadessustentaveis.model.administracao.SolicitacoesBoasPraticas;
import br.org.cidadessustentaveis.repository.SolicitacoesBoasPraticasRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class SolicitacoesBoasPraticasService {

	@Autowired
	private CidadeService cidadeService;
	
	@Autowired
	private SolicitacoesBoasPraticasRepository repository;
	
	public SolicitacoesBoasPraticas buscarPorId(Long id) {
		Optional<SolicitacoesBoasPraticas> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Solicitação não encontrado!"));
	}
	
	public SolicitacoesBoasPraticasDTO buscarSolicitacaoPorId(Long id) {
		SolicitacoesBoasPraticas solicitacaoRef = buscarPorId(id);
		return new SolicitacoesBoasPraticasDTO(solicitacaoRef);
	}
	
	public List<SolicitacoesBoasPraticasDTO> buscarSolicitacoesBoasPraticasToList() {
		return repository.BuscarSolicitacoesBoasPraticasToList();
	}
	
	public SolicitacoesBoasPraticas inserir(SolicitacoesBoasPraticasDTO solicitacoesBoasPraticasDTO) {
		SolicitacoesBoasPraticas solicitacao = solicitacoesBoasPraticasDTO.toEntityInsert(solicitacoesBoasPraticasDTO);
		solicitacao.setCidade( solicitacoesBoasPraticasDTO.getIdCidade() != null ? this.cidadeService.buscarPorId(solicitacoesBoasPraticasDTO.getIdCidade()): null);
		solicitacao.setDataPublicacao(LocalDate.now());
		solicitacao.setHorarioPublicacao(LocalTime.now());
		return repository.save(solicitacao);
	}
	
}
