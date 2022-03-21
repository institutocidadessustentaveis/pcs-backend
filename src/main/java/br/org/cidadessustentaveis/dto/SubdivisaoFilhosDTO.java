package br.org.cidadessustentaveis.dto;

import java.util.ArrayList;
import java.util.List;


import br.org.cidadessustentaveis.model.administracao.SubdivisaoCidade;
import lombok.Data;

@Data	
public class SubdivisaoFilhosDTO {
    private Long id;	
    private Long cidade;
	private String nome;
	private Long nivel;
	private String nomeTipo;
	private List<SubdivisaoFilhosDTO> filhos = new ArrayList<SubdivisaoFilhosDTO>();

	public SubdivisaoFilhosDTO(SubdivisaoCidade subdivisao){
		this.id = subdivisao.getId();
		this.cidade = subdivisao.getCidade().getId();
		this.nome = subdivisao.getNome();
		this.nivel = subdivisao.getTipoSubdivisao().getNivel();
		this.nomeTipo= subdivisao.getTipoSubdivisao().getNome();
	}
    
}
