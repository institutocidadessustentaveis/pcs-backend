package br.org.cidadessustentaveis.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.EventoDTO;
import br.org.cidadessustentaveis.dto.PrefeituraDTO;
import br.org.cidadessustentaveis.dto.TipoSubdivisaoDTO;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.TipoSubdivisao;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.eventos.Evento;
import br.org.cidadessustentaveis.repository.TipoSubdivisaoRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;

@Service
public class TipoSubdivisaoService {
	
	@Autowired
	private UsuarioContextUtil usuarioUtil;
	
	@Autowired
	private TipoSubdivisaoRepository tipoSubdivisaoRepository;
	
	@Autowired
	private PrefeituraService prefeituraService;

	public List<TipoSubdivisaoDTO> buscarTodosPorPrefeituraId(Long idPrefeitura) throws Exception {
		Usuario usuario = usuarioUtil.getUsuario();

		return tipoSubdivisaoRepository.buscarTodosPorPrefeituraId(usuario.getPrefeitura().getId());
	}
	
	public List<TipoSubdivisao> buscarPorCidadeId(Long idCidade) throws Exception {
		return tipoSubdivisaoRepository.findByPrefeituraCidadeId(idCidade);
	}
	
	public void deletar(Long id) {		
		tipoSubdivisaoRepository.deleteById(id);
	}
	
	public TipoSubdivisao inserir(TipoSubdivisaoDTO tipoSubdivisaoDTO) throws Exception {
		Usuario usuario = usuarioUtil.getUsuario();
		
		TipoSubdivisao tipoSubdivisao = tipoSubdivisaoDTO.toEntityInsert(tipoSubdivisaoDTO);
		
//		Prefeitura prefeitura = prefeituraService.buscarPorId(usuario.getPrefeitura().getId());
		
		tipoSubdivisao.setPrefeitura(prefeituraService.buscarPorId(usuario.getPrefeitura().getId()));
		
		tipoSubdivisaoRepository.save(tipoSubdivisao);
		
		return tipoSubdivisao;
	} 
	
	public TipoSubdivisaoDTO alterar(TipoSubdivisaoDTO tipoSubdivisaoDTO) throws Exception {
		if (tipoSubdivisaoDTO.getId() == null) {
			throw new Exception("Campo id divergente.");
		}
	    
		TipoSubdivisao tipoSubdivisao = tipoSubdivisaoDTO.toEntityUpdate(buscarPorId(tipoSubdivisaoDTO.getId()));
		
		tipoSubdivisao.setNivel(tipoSubdivisaoDTO.getNivel());
		tipoSubdivisao.setNome(tipoSubdivisaoDTO.getNome());
		tipoSubdivisao.setTipoPai(tipoSubdivisaoDTO.getTipoPai());

		tipoSubdivisaoRepository.save(tipoSubdivisao);
		
		return new TipoSubdivisaoDTO(tipoSubdivisao);
	}
	
	public TipoSubdivisao buscarPorId(Long id) {
		Optional<TipoSubdivisao> obj = tipoSubdivisaoRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Evento não encontrado!"));
	}
}
