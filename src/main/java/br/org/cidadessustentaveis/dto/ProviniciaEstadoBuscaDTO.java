package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ProviniciaEstadoBuscaDTO {

	private Long id;

	private String nome;
	
	private PaisDTOBusca pais;
	
	private String sigla;

	private String populacao;
	
	private String nomePais;
	
	public ProviniciaEstadoBuscaDTO(Long id) {
		this.id = id;
	}


	public ProviniciaEstadoBuscaDTO(ProvinciaEstado obj) {
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.pais = new PaisDTOBusca(obj.getPais());
		this.populacao = obj.getPopulacao();
		if(obj.getPais() != null) {
			this.nomePais = obj.getPais().getNome();
		}
	}
}
