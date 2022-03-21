package br.org.cidadessustentaveis.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.HistoricoRelatorioGeradoDTO;
import br.org.cidadessustentaveis.dto.RelatorioInteracaoChatForumDTO;
import br.org.cidadessustentaveis.model.administracao.ComentarioDiscussao;

@Service
public class RelatorioInteracaoChatForumService {
	
	@Autowired
	private HistoricoRelatorioGeradoService historicoRelatorioGeradoService;
	
	@Autowired
	private ComentarioDiscussaoService comentarioService;
	
	@Autowired
	private UsuarioService usuarioService;

	public List<RelatorioInteracaoChatForumDTO> buscar(RelatorioInteracaoChatForumDTO filtro){
		List<RelatorioInteracaoChatForumDTO> lista = new ArrayList<>();
		
		List<ComentarioDiscussao> dadosRelatorio = comentarioService.buscarDadosRelatorio();
		
		for (int i = 0; i < dadosRelatorio.size(); i++) {	
			RelatorioInteracaoChatForumDTO relatorio = new RelatorioInteracaoChatForumDTO();
			relatorio.setNomeDoUsuario(usuarioService.buscarPorId(dadosRelatorio.get(i).getUsuario().getId()).getNome());
			relatorio.setDataHora(dadosRelatorio.get(i).getDataPublicacao());
			relatorio.setFerramenta("Fórum");
			lista.add(relatorio);
		}
		
		lista = lista.stream()
				.filter(filtro.getNomeDoUsuario() != null && !filtro.getNomeDoUsuario().isEmpty() ? registro -> registro.getNomeDoUsuario().contains(filtro.getNomeDoUsuario()) : registro -> true)
				.filter(filtro.getFerramenta() != null && !filtro.getFerramenta().isEmpty() ? registro -> filtro.getFerramenta().compareTo(registro.getFerramenta()) == 0 : registro -> true)
				.filter(filtro.getDataHora() != null ? registro -> filtro.getDataHora() == registro.getDataHora() : registro -> true)
				.collect(Collectors.toList());

		return lista;
	}

	public void gravarLog(String usuarioLogado, String tipoRelatorio){
		HistoricoRelatorioGeradoDTO objeto = new HistoricoRelatorioGeradoDTO();
		objeto.setNomeUsuario(usuarioLogado);
		objeto.setDataHora(LocalDateTime.now());
		objeto.setNomeRelatorio(tipoRelatorio);

		historicoRelatorioGeradoService.insert(objeto);
	}

}
