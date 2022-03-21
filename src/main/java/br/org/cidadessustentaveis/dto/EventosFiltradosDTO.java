package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.eventos.Evento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EventosFiltradosDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    
    @NotNull
    @NotEmpty
    private LocalDate dataEvento;
    
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
    private String organizador;
    
    @NotNull
    @NotEmpty
    private Double latitude;
    
    @NotNull
    @NotEmpty
    private Double longitude;
    
    @NotNull
    @NotEmpty
    private boolean publicado;
    
    @NotNull
    private LocalTime horario;
    
    private String site;
    
    private String endereco;
    
    @NotNull
    private LocalDateTime dataHorario;
    
    private String linkExterno;
    
    public EventosFiltradosDTO (Evento objRef) {
        this.id = objRef.getId();    
        this.tipo = objRef.getTipo();
        this.nome = objRef.getNome();
        this.descricao = objRef.getDescricao();
        this.dataEvento = objRef.getDataEvento();
        this.organizador = objRef.getOrganizador();
        this.latitude = objRef.getLatitude();
        this.longitude = objRef.getLongitude();
        this.publicado = objRef.isPublicado();
        this.horario = objRef.getHorario();
        this.dataHorario = LocalDateTime.of(this.dataEvento, this.horario);
        this.site = objRef.getSite();
        this.endereco = objRef.getEndereco();
        this.linkExterno = objRef.getLinkExterno();
    }

	public EventosFiltradosDTO(Long id, @NotNull @NotEmpty LocalDate dataEvento, @NotNull @NotEmpty String tipo,
			@NotNull @NotEmpty String nome, @NotNull @NotEmpty String descricao, @NotNull @NotEmpty String organizador,
			@NotNull @NotEmpty Double latitude, @NotNull @NotEmpty Double longitude,
			@NotNull @NotEmpty boolean publicado, @NotNull LocalTime horario, String site, String endereco, String linkExterno) {
		super();
		this.id = id;
		this.dataEvento = dataEvento;
		this.tipo = tipo;
		this.nome = nome;
		this.descricao = descricao;
		this.organizador = organizador;
		this.latitude = latitude;
		this.longitude = longitude;
		this.publicado = publicado;
		this.horario = horario;
		this.site = site;
		this.endereco = endereco;
		this.dataHorario = LocalDateTime.of(this.dataEvento, this.horario);
		this.linkExterno = linkExterno;
	}   
    
}
