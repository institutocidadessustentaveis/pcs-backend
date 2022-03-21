package br.org.cidadessustentaveis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ShapeListagemMapaDTO {

    private Long id;

    private String nome;
    
    private String cidade;
    
    private Boolean exibirAuto;
    
    private Boolean camadaPrefeitura;

	public ShapeListagemMapaDTO(Long id, String nome, String cidade, Boolean exibirAuto) {
		super();
		this.id = id;
		this.nome = nome;
		this.cidade = cidade;
		this.exibirAuto = exibirAuto;
		this.camadaPrefeitura = cidade != null ? true : false;
	}
	
}
