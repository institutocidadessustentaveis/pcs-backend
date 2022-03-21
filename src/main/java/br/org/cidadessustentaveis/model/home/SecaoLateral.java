package br.org.cidadessustentaveis.model.home;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.search.annotations.Indexed;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="home_secao_lateral")	
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@EntityListeners(ListenerAuditoria.class)
@Indexed
public class SecaoLateral implements Serializable{


	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "home_secao_lateral_id_seq")
	@SequenceGenerator(name = "home_secao_lateral_id_seq", sequenceName = "home_secao_lateral_id_seq", allocationSize = 1)
	@Column(name="id",nullable=false)
	private Long id;
	
	@Column(name="indice")
	private Long indice;
	
	@Column(name="primeiro_titulo_principal")
	private String primeiroTituloPrincipal;
	
	@Column(name = "primeiro_titulo_principal_cor")
	private String primeiroTituloPrincipalCor;
	
	@Column(name = "primeiro_titulo_principal_link")
	private String primeiroTituloPrincipalLink;
	
	@Column(name="primeira_imagem")
	private String primeiraImagem;
	
	@Column(name = "primeira_imagem_link")
	private String primeiraImagemLink;
	
	@Column(name = "primeira_imagem_tooltip")
	private String primeiraImagemTooltip;
	
	
	@Column(name="primeiro_titulo")
	private String primeiroTitulo;
	
	@Column(name = "primeiro_titulo_cor")
	private String primeiroTituloCor;

	@Column(name = "primeiro_titulo_link")
	private String primeiroTituloLink;
	
	@Column(name = "primeiro_titulo_principal_exibir")
	private Boolean primeiroTituloPrincipalExibir;
	
	
	@Column(name="primeiro_texto")
	private String primeiroTexto;
	
	@Column(name = "primeiro_texto_cor")
	private String primeiroTextoCor;
	
	@Column(name = "primeiro_texto_link")
	private String primeiroTextoLink;
	
	@Column(name="segunda_imagem")
	private String segundaImagem;
	
	@Column(name = "segunda_imagem_link")
	private String segundaImagemLink;
	
	@Column(name = "segunda_imagem_tooltip")
	private String segundaImagemTooltip;
	
	
	@Column(name="segundo_titulo")
	private String segundoTitulo;
	
	@Column(name = "segundo_titulo_cor")
	private String segundoTituloCor;

	@Column(name = "segundo_titulo_link")
	private String segundoTituloLink;
	
	@Column(name = "segundo_titulo_principal_exibir")
	private Boolean segundoTituloPrincipalExibir;
	
	@Column(name="segundo_texto")
	private String segundoTexto;
	
	@Column(name = "segundo_texto_cor")
	private String segundoTextoCor;
	
	@Column(name = "segundo_texto_link")
	private String segundoTextoLink;
	
	
	@Column(name="segundo_titulo_principal")
	private String segundoTituloPrincipal;
	
	@Column(name = "segundo_titulo_principal_cor")
	private String segundoTituloPrincipalCor;
	
	@Column(name = "segundo_titulo_principal_link")
	private String segundoTituloPrincipalLink;
	
	
	@Column(name = "item_titulo_cor")
	private String itemTituloCor;
	
	@Column(name = "item_texto_cor")
	private String itemTextoCor;
	

	@Column(name = "primeiro_item_titulo")
	private String primeiroItemTitulo;
	
	@Column(name = "primeiro_item_texto")
	private String primeiroItemTexto;
	
	@Column(name = "primeiro_item_link")
	private String primeiroItemLink;
	
	@Column(name = "segundo_item_titulo")
	private String segundoItemTitulo;
	
	@Column(name = "segundo_item_texto")
	private String segundoItemTexto;
	
	@Column(name = "segundo_item_link")
	private String segundoItemLink;

	@Column(name = "terceiro_item_titulo")
	private String terceiroItemTitulo;
	
	@Column(name = "terceiro_item_texto")
	private String terceiroItemTexto;
	
	@Column(name = "terceiro_item_link")
	private String terceiroItemLink;
	
	@Column(name = "quarto_item_titulo")
	private String quartoItemTitulo;
	
	@Column(name = "quarto_item_texto")
	private String quartoItemTexto;
	
	@Column(name = "quarto_item_link")
	private String quartoItemLink;

	
	@Column(name = "terceira_imagem")
	private String terceiraImagem;
	
	@Column(name = "terceira_imagem_link")
	private String terceiraImagemLink;
	
	@Column(name = "terceira_imagem_tooltip")
	private String terceiraImagemTooltip;

	
	@Column(name="terceiro_titulo")
	private String terceiroTitulo;
	
	@Column(name = "terceiro_titulo_cor")
	private String terceiroTituloCor;

	@Column(name = "terceiro_titulo_link")
	private String terceiroTituloLink;
	
	
	@Column(name="terceiro_texto")
	private String terceiroTexto;
	
	@Column(name = "terceiro_texto_cor")
	private String terceiroTextoCor;

	@Column(name = "terceiro_texto_link")
	private String terceiroTextoLink;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="home")
	@JsonBackReference
	private Home home;
	
	
	@Column(name = "tipo")
	private String tipo;
	

	@Column(name = "exibir")
	private Boolean exibir;
	
	@Column(name = "exibir_banner1")
	private Boolean exibirBanner1;
	
	@Column(name = "exibir_banner2")
	private Boolean exibirBanner2;
	
	@Column(name = "exibir_banner3")
	private Boolean exibirBanner3;
	
	@Column(name = "exibir_banner4")
	private Boolean exibirBanner4;
	
	
}
