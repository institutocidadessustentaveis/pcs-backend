package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.Set;

import br.org.cidadessustentaveis.model.indicadores.ValorReferencia;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data @Getter @Setter @NoArgsConstructor

public class AvaliacaoVariavelPreenchidaDetalhesDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long idIndicadorPreenchido;
	private Long idVariavelPreenchida;
	private Long idVariavel;
	private String nomeVariavel;
	private String descricaoVariavel;
	private String valorPrefeitura;
	private String valorResposta;
	private Set<ValorReferencia> referencia;
	
	public AvaliacaoVariavelPreenchidaDetalhesDTO(Long idIndicadorPreenchido, Long idVariavelPreenchida, Long idVariavel, String nomeVariavel, String descricaoVariavel,
			String valorPrefeitura, String valorResposta, Set<ValorReferencia> referencia) {
		super();
		this.idIndicadorPreenchido = idIndicadorPreenchido;
		this.idVariavelPreenchida = idVariavelPreenchida;
		this.idVariavel = idVariavel;
		this.nomeVariavel = nomeVariavel;
		this.descricaoVariavel = descricaoVariavel;
		this.valorPrefeitura = valorPrefeitura;
		this.valorResposta = valorResposta;
		this.referencia = referencia;
	}
}
