package br.org.cidadessustentaveis.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.PremiacaoDTO;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.Premiacao;
import br.org.cidadessustentaveis.model.administracao.PremiacaoPrefeitura;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.enums.StatusPremiacao;
import br.org.cidadessustentaveis.model.institucional.Arquivo;
import br.org.cidadessustentaveis.repository.PremiacaoRepository;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
	
@Service
public class PremiacaoService {
	@Autowired
	private PremiacaoRepository repository;
	
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ArquivoService arquivoService;

		
	public List<Premiacao> buscar(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest).getContent();
	}
	
	public Premiacao inserirPremiacao(PremiacaoDTO premiacaoDTO) {
		Premiacao premiacaoInsert = premiacaoDTO.toEntityInsert();
		
		Arquivo arquivoAux = premiacaoDTO.getBannerPremiacao().toEntityInsert();
		premiacaoInsert.setBannerPremiacao(arquivoAux);

		premiacaoInsert = repository.save(premiacaoInsert);
		return premiacaoInsert;
	}
	
	public Premiacao buscarPorId(Long id) {
		Optional<Premiacao> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Premiação não encontrada!"));
	}
	
	public Premiacao editar(PremiacaoDTO premiacaoDTO, Long id) {
		Premiacao premiacaoRef = buscarPorId(id);
		premiacaoRef = premiacaoDTO.toEntityUpdate(premiacaoRef);
		
		if(premiacaoDTO.getBannerPremiacao() != null && premiacaoDTO.getBannerPremiacao().getId() == null) {
			Arquivo arquivoAux = premiacaoDTO.getBannerPremiacao().toEntityInsert();
			premiacaoRef.setBannerPremiacao(arquivoAux);
		}
		return repository.save(premiacaoRef);
	}
		
	public void deletar(Long id) {
		buscarPorId(id);
		try {
			repository.deleteById(id);
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("O registro está relacionado com outra entidade");
		}	
	}

	public Long count() {
		return repository.count();
	}

	public void deletarArquivo(final Long idArquivo) {
		arquivoService.buscarPorId(idArquivo);
		arquivoService.deletarArquivoPorId(idArquivo);
	}
	
	public List<Premiacao> buscarPorDescricaoLike(String descricao) {
		return repository.findByDescricaoLike(descricao);
	}
	
	public List<Premiacao> buscarPremiacoesPorPrefeitura() {
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmailCredencial(user);
		Prefeitura prefeitura = new Prefeitura();
		if(usuario.getPrefeitura() != null) {
			prefeitura = usuario.getPrefeitura();
			return repository.buscarPremiacoesPorPrefeitura(prefeitura.getId());
		}
		return new ArrayList<>();
	}
	
	public List<Premiacao> buscarPremiacoesEmAvaliacaoPorPrefeitura(){
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmailCredencial(user);
		Prefeitura prefeitura = usuario.getPrefeitura();
		List<PremiacaoPrefeitura> lista = null;
		List<Premiacao> listaPremiacao = new ArrayList<>();
		if(prefeitura != null) {
			lista = repository.buscarPremiacoesEmAvaliacaoPorPrefeitura(prefeitura.getId(),StatusPremiacao.EM_ANDAMENTO);
			listaPremiacao = lista.stream().map(PremiacaoPrefeitura::getPremiacao).collect(Collectors.toList());
		}
		return listaPremiacao;
	}
	
	public List<Premiacao> buscarPremiacoesEmInscricoesAbertas(){
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmailCredencial(user);
		Prefeitura prefeitura = usuario.getPrefeitura();
		List<Premiacao> listaPremiacao = new ArrayList<>();
		if(prefeitura != null) {
			listaPremiacao = repository.buscarPremiacoesEmInscricoesAbertas(StatusPremiacao.INSCRICOES_ABERTAS);
		}
		return listaPremiacao;
	}

}
