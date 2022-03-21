package br.org.cidadessustentaveis.model.institucional;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="arquivo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Arquivo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(generator = "arquivo_id_seq")
	@SequenceGenerator(name = "arquivo_id_seq", sequenceName = "arquivo_id_seq", allocationSize = 1)
	@Column(name="id", nullable = false)
	private Long id;

	@Column(name = "nome_arquivo")
	private String nomeArquivo;
	
	@Column(name = "extensao")
	private String extensao;
	
	@Column(name = "conteudo")
	private String conteudo;
}
