package br.org.cidadessustentaveis.model.noticias;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.institucional.Arquivo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="boletim_template_01") 
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
@EntityListeners(ListenerAuditoria.class)
public class BoletimTemplate01 implements Serializable {

	private static final long serialVersionUID = -7772100905407481090L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="titulo")
	private String titulo;
	
	@Column(name="texto_introducao")
	private String textoIntroducao;
	
	@Column(name="titulo_primeiro_banner")
	private String tituloPrimeiroBanner;
	
	@Column(name="texto_primeiro_banner")
	private String textoPrimeiroBanner;
	
	@Column(name="texto_botao_01")
	private String textoBotao01;
	
	@ManyToOne(fetch= FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name="imagem_primeiro_banner", nullable = false)
	private Arquivo imagemPrimeiroBanner;
	

	@Column(name="titulo_chamada_01")
	private String tituloChamada01;
	
	@Column(name="subtitulo_chamada_01")
	private String subtituloChamada01;
	
	@Column(name="texto_chamada_01")
	private String textoChamada01;
	
	@Column(name="titulo_chamada_02")
	private String tituloChamada02;
	
	@Column(name="subtitulo_chamada_02")
	private String subtituloChamada02;
	
	@Column(name="texto_chamada_02")
	private String textoChamada02;
	
	
	@ManyToOne(fetch= FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name="imagem_principal", nullable = false)
	private Arquivo imagemPrincipal;
	
	@Column(name="titulo_imagem_principal")
	private String tituloImagemPrincipal;
	
	@Column(name="legenda_imagem_principal")
	private String legendaImagemPrincipal;
	
	
	@Column(name="titulo_chamada_03")
	private String tituloChamada03;
	
	@Column(name="subtitulo_chamada_03")
	private String subtituloChamada03;
	
	@Column(name="texto_chamada_03")
	private String textoChamada03;
	
	@Column(name="titulo_chamada_04")
	private String tituloChamada04;
	
	@Column(name="subtitulo_chamada_04")
	private String subtituloChamada04;
	
	@Column(name="texto_chamada_04")
	private String textoChamada04;
	
	
	@ManyToOne(fetch= FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name="imagem_segundo_banner", nullable = false)
	private Arquivo imagemSegundoBanner;
	
	@Column(name="titulo_segundo_banner")
	private String tituloSegundoBanner;
	
	@Column(name="texto_segundo_banner")
	private String textoSegundoBanner;
	
	@Column(name="texto_botao_02")
	private String textoBotao02;
	
	
	@Column(name="texto_final")
	private String textoFinal;
}
