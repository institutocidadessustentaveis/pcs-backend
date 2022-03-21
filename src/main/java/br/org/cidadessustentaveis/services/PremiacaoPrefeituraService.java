package br.org.cidadessustentaveis.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.Premiacao;
import br.org.cidadessustentaveis.model.administracao.PremiacaoPrefeitura;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.repository.PremiacaoPrefeituraRepository;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
	
@Service
public class PremiacaoPrefeituraService {
	
	@Autowired
	private PremiacaoPrefeituraRepository repository;
	
	@Autowired
	private PremiacaoService service;		
	
	@Autowired
	private UsuarioService usuarioService;	
	
	
	public List<PremiacaoPrefeitura> buscar() {
		return repository.findAll();
	}
	
	public PremiacaoPrefeitura buscarPorId(Long id) {
		Optional<PremiacaoPrefeitura> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("PremiaçãoPrefeitura não encontrada!"));
	}	

	public List<PremiacaoPrefeitura> buscarCidadesInscritas(Long id) {
		List<PremiacaoPrefeitura> obj = repository.buscarCidadesInscritas(id);
		//return obj.orElseThrow(() -> new ObjectNotFoundException("Cidade não encontrada!"));
		return obj;
	}	

	public Premiacao participarPremiacao(Long id) {
			
		Premiacao premiacao = service.buscarPorId(id);
		
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmailCredencial(user);
		Prefeitura prefeitura = usuario.getPrefeitura();
		
		List<PremiacaoPrefeitura> lista = repository.findByPrefeituraIdAndPremiacaoId(prefeitura.getId(), id);
		
		if(lista != null && !lista.isEmpty()) {
			throw new DataIntegrityException("Não foi possível participar. Prefeitura já esta participando desta premiação");
		}

		PremiacaoPrefeitura pp = new PremiacaoPrefeitura();
		pp.setPrefeitura(prefeitura); 
		pp.setPremiacao(premiacao); 
		
		repository.save(pp);
		
		return premiacao;
	}
	
	public void cancelarInscricao(Long idPremiacao) {
		
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmailCredencial(user);
		Prefeitura prefeitura = usuario.getPrefeitura();
		
		List<PremiacaoPrefeitura> lista = repository.findByPrefeituraIdAndPremiacaoId(prefeitura.getId(), idPremiacao);
		
		if(lista != null && !lista.isEmpty()) {
			repository.delete(lista.get(0));
		}else {
			throw new DataIntegrityException("Não foi possível cancelar participar.");
		}
		
	}

}
