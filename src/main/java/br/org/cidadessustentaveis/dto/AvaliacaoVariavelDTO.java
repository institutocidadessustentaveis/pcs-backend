package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Getter @Setter @NoArgsConstructor
public class AvaliacaoVariavelDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String cidade;
	
	private long qtdVariaveis;
	
	private Date dataPreenchimento;
	
	private Date dataAvaliacao;
	
	private String status;

	public AvaliacaoVariavelDTO(Long id, String cidade, long qtdVariaveis) {
		super();
		this.id = id;
		this.cidade = cidade;
		this.qtdVariaveis = qtdVariaveis;
	}

	public AvaliacaoVariavelDTO(Long id, Date dataPreenchimento, Date dataAvaliacao) {
		super();
		this.id = id;
		this.dataPreenchimento = dataPreenchimento;
		this.dataAvaliacao = dataAvaliacao;
	}
}




