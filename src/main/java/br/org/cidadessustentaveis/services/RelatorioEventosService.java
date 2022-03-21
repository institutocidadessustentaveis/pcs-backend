package br.org.cidadessustentaveis.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.RelatorioEventoDTO;

@Service
public class RelatorioEventosService {
	
	@Autowired
	private HistoricoRelatorioGeradoService historicoRelatorioGeradoService;

	public List<RelatorioEventoDTO> buscar(RelatorioEventoDTO filtro){
		List<RelatorioEventoDTO> lista = new ArrayList<>();	
				for (int i = 1; i < 12; i++) {
					RelatorioEventoDTO dto = new RelatorioEventoDTO();
					dto.setId(Long.parseLong(i + ""));
					dto.setTitulo("Titulo" + i);
					dto.setNpessoasAdicionaram(i);
					dto.setNpessoasCadastradas(i * 2);
					dto.setNpessoasSeguiram(i * 3);
					dto.setNpessoasVisualizaram(i * i);
					dto.setData(i + "/04/2019");
					lista.add(dto);
				}
		return lista;
	}

	public void gravarLog(){
	
	}

}
