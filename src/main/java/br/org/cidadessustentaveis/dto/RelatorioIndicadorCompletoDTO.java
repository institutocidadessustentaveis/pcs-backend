package br.org.cidadessustentaveis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelatorioIndicadorCompletoDTO {
	
	private Long codigoIBGE;
	
	private String cidade;
	
	private String estado;
	
	private String prefeito;
	
	private String partido;
	
	private Long populacao;
	
	private String porte;
	
	private boolean usuarioCadastrado;
	
	private Long qtdUsuarioCadastrado;
	
	private Long indicadoresMinimos;
	
	private Long qtdIndicadoresPreenchidos;
	
	private String porcentagemIndicadoresPreenchidos;
	
	private Integer numPorcentagemIndicadores;
}
