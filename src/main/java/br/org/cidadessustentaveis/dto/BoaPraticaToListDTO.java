package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BoaPraticaToListDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private PaisDTO pais;
	private ProvinciaEstadoDTO estado;
	private CidadeDTO municipio;
	private String titulo;
	private String tipo;
	private Long idPrefeituraCadastro;
	
	public BoaPraticaToListDTO(BoaPratica objRef) {
		this.id = objRef.getId();
		this.pais = new PaisDTO(objRef);
		this.estado = new ProvinciaEstadoDTO(objRef);
		this.municipio = new CidadeDTO(objRef);
		this.titulo = objRef.getTitulo();
		this.tipo = objRef.getTipo();
		this.idPrefeituraCadastro = objRef.getPrefeitura() != null ? objRef.getPrefeitura().getId() : null;
	}	
}