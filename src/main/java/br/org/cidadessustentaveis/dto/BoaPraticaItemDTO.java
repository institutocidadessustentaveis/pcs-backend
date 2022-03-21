package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @NoArgsConstructor @Getter @Setter
public class BoaPraticaItemDTO implements Serializable {

	private static final long serialVersionUID = 1L;	

	private Long idBoaPratica;
	private String titulo;
	private String nomeCidade;
	private String nomeProvinciaEstado;
	private String nomePais;
	private String objetivoGeral;
	private String imagemPrincipal;
	private String subtitulo;
	private String autorImagemPrincipal;
	
	public BoaPraticaItemDTO(Long idBoaPratica, String titulo, String subtitulo, String objetivoGeral,
			String nomeCidade, String nomeProvinciaEstado, String nomePais, String autorImagemPrincipal) {
		this.idBoaPratica = idBoaPratica;
		this.titulo = titulo;
		this.nomeCidade = nomeCidade;
		this.nomeProvinciaEstado = nomeProvinciaEstado;
		this.nomePais = nomePais;
		this.objetivoGeral = objetivoGeral;
		this.subtitulo = subtitulo;
		this.autorImagemPrincipal = autorImagemPrincipal;
	}
	
	public BoaPraticaItemDTO(Long idBoaPratica, String titulo, String subtitulo, String objetivoGeral,
			String nomeCidade, String nomeProvinciaEstado, String nomePais) {
		this.idBoaPratica = idBoaPratica;
		this.titulo = titulo;
		this.nomeCidade = nomeCidade;
		this.nomeProvinciaEstado = nomeProvinciaEstado;
		this.nomePais = nomePais;
		this.objetivoGeral = objetivoGeral;
		this.subtitulo = subtitulo;
	}	
	
	public BoaPraticaItemDTO(BoaPratica boaPratica) {
		this.idBoaPratica = boaPratica.getId();
		this.titulo = boaPratica.getTitulo();
		this.subtitulo = boaPratica.getSubtitulo();
	}	
}

