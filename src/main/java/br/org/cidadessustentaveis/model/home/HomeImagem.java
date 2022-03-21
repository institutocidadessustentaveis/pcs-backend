package br.org.cidadessustentaveis.model.home;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="home_imagem")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeImagem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(generator = "home_imagem_id_seq")
	@SequenceGenerator(name = "home_imagem_id_seq", sequenceName = "home_imagem_id_seq", allocationSize = 1)
	@Column(name="id", nullable = false)
	private Long id;
	
	@Column(name = "extensao")
	private String extensao;
	
	@Column(name = "conteudo")
	private String conteudo;
	
	@Column(name = "tipo")
	private String tipo;
	
	@Column(name = "autor")
	private String nomeAutor;
	
	@Column(name = "titulo")
	private String titulo;
	
	@Column(name = "subtitulo")
	private String subtitulo;
	
	@Column(name = "link")
	private String link;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="home")
	@JsonBackReference
	private Home home;
	
	@Column(name="indice")
	private Long indice;
	
	@Column(name="exibir_busca")
	private Boolean exibirBusca;
}
