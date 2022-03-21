package br.org.cidadessustentaveis.model.home;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TermVector;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="home_barra")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeBarra implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(generator = "home_barra_id_seq")
	@SequenceGenerator(name = "home_barra_id_seq", sequenceName = "home_barra_id_seq", allocationSize = 1)
	@Column(name="id", nullable = false)
	private Long id;
	
	@Column(name="indice")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private Long indice;
	
	@Column(name = "div1_texto")
	private String div1Texto;
	@Column(name = "div1_link")
	private String div1Link;
	@Column(name = "div1_cor_fundo")
	private String div1CorFundo;
	@Column(name = "div1_cor_texto")
	private String div1CorTexto;
	
	@Column(name = "div2_texto")
	private String div2Texto;
	@Column(name = "div2_link")
	private String div2Link;
	@Column(name = "div2_cor_fundo")
	private String div2CorFundo;
	@Column(name = "div2_cor_texto")
	private String div2CorTexto;
	
	@Column(name = "div3_texto")
	private String div3Texto;
	@Column(name = "div3_link")
	private String div3Link;
	@Column(name = "div3_cor_fundo")
	private String div3CorFundo;
	@Column(name = "div3_cor_texto")
	private String div3CorTexto;
	
	@Column(name = "div4_texto")
	private String div4Texto;
	@Column(name = "div4_link")
	private String div4Link;
	@Column(name = "div4_cor_fundo")
	private String div4CorFundo;
	@Column(name = "div4_cor_texto")
	private String div4CorTexto;
	
	@Column(name = "div5_texto")
	private String div5Texto;
	@Column(name = "div5_link")
	private String div5Link;
	@Column(name = "div5_cor_fundo")
	private String div5CorFundo;
	@Column(name = "div5_cor_texto")
	private String div5CorTexto;
	
	@Column(name = "exibir")
	private Boolean exibir;
}
