package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.indicadores.VariavelPreenchida;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class VariavelPreenchidaMunicipioDTO {

	private Long idVariavel;
	private Short ano;
	private Double valor;
	private Long idCidade;
	private String nomeCidade;
	
	public VariavelPreenchidaMunicipioDTO(VariavelPreenchida preenchida) {
		this.idVariavel = preenchida.getVariavel().getId();
		this.ano = preenchida.getAno();
		this.valor = preenchida.getValor();
		this.idCidade = preenchida.getPrefeitura().getCidade().getId();
		this.nomeCidade = preenchida.getPrefeitura().getCidade().getNome() +" - "+preenchida.getPrefeitura().getCidade().getProvinciaEstado().getSigla();
		
	}
	
}
