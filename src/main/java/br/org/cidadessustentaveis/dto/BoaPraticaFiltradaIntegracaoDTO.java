package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @NoArgsConstructor @Getter @Setter
public class BoaPraticaFiltradaIntegracaoDTO implements Serializable {

	private static final long serialVersionUID = 1L;	

	private Long id;
	private String titulo;
	private String subtitulo;
	private String objetivoGeral;
	private String nomeCidade;
	private String nomeProvinciaEstado;
	private String nomePais;

	public BoaPraticaFiltradaIntegracaoDTO(Long id, String titulo, String subtitulo, String objetivoGeral,
			String nomeCidade, String nomeProvinciaEstado, String nomePais) {
		this.id = id;
		this.titulo = titulo;
		this.subtitulo = subtitulo;
		this.objetivoGeral = objetivoGeral != null ? objetivoGeral.replaceAll("\\<.*?>","").replaceAll("&nbsp;", "").replaceAll("\n", "") : null;
		this.nomeCidade = nomeCidade;
		this.nomeProvinciaEstado = nomeProvinciaEstado;
		this.nomePais = nomePais;
	}
	
	
}

