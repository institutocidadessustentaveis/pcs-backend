package br.org.cidadessustentaveis.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.AjusteGeralDTO;
import br.org.cidadessustentaveis.model.administracao.AjusteGeral;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.repository.AjusteGeralRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class AjusteGeralService {
	
	@Autowired
	AjusteGeralRepository repository;
	
	public AjusteGeral inserirAjuste(AjusteGeralDTO objRef) {
		
		if(!objRef.getLocalAplicacao().equals("ALERTA-EMAIL-NOVA-CIDADE") && 
		   !objRef.getLocalAplicacao().equals("EMAIL-ALERTA-PLANO-METAS")) {
			AjusteGeralDTO ajusteDTO = this.buscarAjustePorLocalApp(objRef.getLocalAplicacao());
			
			if(ajusteDTO != null) {
			  repository.deleteAjustePorLocalApp(objRef.getLocalAplicacao());
			}
		}
		
		AjusteGeral ajuste = new AjusteGeral();
		
		ajuste.setConteudo(objRef.getConteudo());
		ajuste.setLocalAplicacao(objRef.getLocalAplicacao());
		
		return repository.save(ajuste);
	}
	
	public AjusteGeralDTO buscarAjustePorLocalApp(String localApp) {
		return repository.buscarAjustePorLocalApp(localApp);
	}
	
	public List<AjusteGeralDTO> buscarListaAjustes(String localApp) {
		return repository.buscarListaAjustes(localApp);
	}
	
	public AjusteGeral buscarPorId(Long id) {
		Optional<AjusteGeral> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Ajuste Geral não encontrado!"));
	}
	
	public void deletar(Long id) {
		AjusteGeral ajusteGeral = this.buscarPorId(id);
		repository.delete(ajusteGeral);
	}

}
