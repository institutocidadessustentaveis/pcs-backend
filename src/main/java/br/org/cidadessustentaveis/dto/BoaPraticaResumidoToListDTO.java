package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @Builder
public class BoaPraticaResumidoToListDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String municipioNome;
	private String titulo;
	private String nomeResponsavel;
	private LocalDate dataPublicacao;
	private String tipo;
	private Long idPrefeituraCadastro;
	
	public BoaPraticaResumidoToListDTO(Long id, String municipioNome, String titulo, String nomeResponsavel, LocalDate dataPublicacao, String tipo, Long idPrefeituraCadastro) {
		this.id = id;
		this.municipioNome = municipioNome;
		this.titulo = titulo;
		this.nomeResponsavel = nomeResponsavel;
		this.dataPublicacao = dataPublicacao;
		this.tipo = tipo;
		this.idPrefeituraCadastro = idPrefeituraCadastro;
	}	
}