package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.List;

import org.wololo.geojson.Feature;

import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ShapeFileMergedDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer ano;
	private String titulo;
	private List<Long> areasInteresse;
	private Long temaGeoespacial;
	private String instituicao;
	private String fonte;
	private String sistemaDeReferencia;
	private String tipoArquivo;
	private String nivelTerritorial;
	private boolean publicar;
	private List<Feature> features; 
	private String origemCadastro;
	private boolean exibirAuto;
	
}