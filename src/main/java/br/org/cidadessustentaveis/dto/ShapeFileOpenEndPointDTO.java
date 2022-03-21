package br.org.cidadessustentaveis.dto;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.org.cidadessustentaveis.model.administracao.Cidade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ShapeFileOpenEndPointDTO{
	
	@JsonIgnore
	private Long id;
	
	private String camada;
	
	private String origem;
	
	private LocalDateTime anoDeReferencia;
	
	private String url;
	
	public ShapeFileOpenEndPointDTO(Long id, String camada, Cidade cidade, LocalDateTime anoDeReferencia) {
		this.id = id;
		this.camada = camada;
		this.origem = cidade != null ? cidade.getNome() + " - " + cidade.getProvinciaEstado().getSigla() : "PCS";
		this.anoDeReferencia = anoDeReferencia;
	}
	
}
