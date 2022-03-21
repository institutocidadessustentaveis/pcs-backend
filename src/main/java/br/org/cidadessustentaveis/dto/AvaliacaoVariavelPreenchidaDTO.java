package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data @Getter @Setter @NoArgsConstructor
public class AvaliacaoVariavelPreenchidaDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String prefeitura;
	private String prefeito;
	private String nomeIndicador;
	private String anoIndicador;
	private List<AvaliacaoVariavelPreenchidaDetalhesDTO> avaliacaoVariavelPreenchidaDetalhesDTO; 

	public AvaliacaoVariavelPreenchidaDTO(Long id, String prefeitura, String prefeito, String nomeIndicador, String anoIndicador, List<AvaliacaoVariavelPreenchidaDetalhesDTO> avaliacaoVariavelPreenchidaDetalhesDTO) {
		super();
		this.id = id;
		this.prefeitura = prefeitura;
		this.prefeito = prefeito;
		this.nomeIndicador = nomeIndicador;
		this.anoIndicador = anoIndicador;
		this.avaliacaoVariavelPreenchidaDetalhesDTO = avaliacaoVariavelPreenchidaDetalhesDTO;
	}

}