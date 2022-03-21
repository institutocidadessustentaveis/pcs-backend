package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoMapaDTO {

	private Long id;
	    
    @NotNull
    @NotEmpty
	private String tipo;
    
    @NotNull
    @NotEmpty
	private String nome;
	
    @NotNull
    @NotEmpty
	private String descricao;

    @NotNull
    @NotEmpty
	private LocalDate data;
	
    @NotNull
    @NotEmpty
	private String organizador;
	
    
    @NotNull
    @NotEmpty
	private Double latitude;
	
    @NotNull
    @NotEmpty
	private Double longitude;

	public EventoMapaDTO(Long id, @NotNull @NotEmpty String tipo, @NotNull @NotEmpty String nome) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.nome = nome;
	}
	
    
    
	
}
