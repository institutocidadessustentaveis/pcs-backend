package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Table(name="dados_download") 

public class DadosDownload implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="cidade")
	private Cidade cidade;
	
	@Column(name="email")
	private String email;
	
	@Column(name="nome")
	private String nome;
	
	@Column(name="organizacao")
	private String organizacao;
	
	@Column(name="boletim")
	private boolean boletim;
	
	@Column(name="arquivo")
	private String arquivo;
	
	@Column(name="data_download")
	private LocalDate dataDownload;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="usuario")
	private Usuario usuario;
	
	@Column(name="nome_cidade")
	private String nomeCidade;
	
	@Column(name="acao")
	private String acao;
	
	@Column(name="pagina")
	private String pagina;
	
	public DadosDownload(DadosDownload dadosDownload) {
		this.id = dadosDownload.getId();
		this.email = dadosDownload.getEmail();
		this.nome = dadosDownload.getNome();
		this.organizacao = dadosDownload.getOrganizacao();
		this.boletim = dadosDownload.isBoletim();
		this.arquivo = dadosDownload.getArquivo();
		this.dataDownload = dadosDownload.getDataDownload();
		this.nomeCidade = dadosDownload.getNomeCidade();
		this.acao = dadosDownload.getAcao();
		this.pagina = dadosDownload.getPagina();
	}
	
}


