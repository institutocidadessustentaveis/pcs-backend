package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.DadosDownload;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.biblioteca.Biblioteca;
import br.org.cidadessustentaveis.model.contribuicoesAcademicas.GrupoAcademico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor 
public class DadosDownloadDTO {
	private Long id;
	private Long cidade;
	private String email;
	private String nome;
	private String organizacao;
	private boolean boletim;
	private String arquivo;
	private LocalDate dataDownload;
	private Long usuario;
	private String usuarioNome;
	private String nomeCidade;
	private String acao;
	private String pagina;
	
	//Campos para filtro
	private LocalDate dataInicio;
	private LocalDate dataFim;
	
	public DadosDownloadDTO(DadosDownload dadosDownload) {
		this.id = dadosDownload.getId();
		this.cidade = dadosDownload.getCidade().getId();
		this.email = dadosDownload.getEmail();
		this.nome = dadosDownload.getNome();
		this.organizacao = dadosDownload.getOrganizacao();
		this.boletim = dadosDownload.isBoletim();
		this.arquivo = dadosDownload.getArquivo();
		this.dataDownload = dadosDownload.getDataDownload();
		this.usuario = dadosDownload.getUsuario().getId();
		this.usuarioNome = dadosDownload.getUsuario().getNome();
		this.nomeCidade = dadosDownload.getNomeCidade();
		this.acao = dadosDownload.getAcao();
		this.pagina = dadosDownload.getPagina();
		
	}
	
	public DadosDownloadDTO(Long id, String email, String nome, String organizacao, boolean boletim, String arquivo,
			LocalDate dataDownload, String usuarioNome, String nomeCidade, String acao, String pagina) {
		this.id = id;
		this.email = email;
		this.nome = nome;
		this.organizacao = organizacao;
		this.boletim = boletim;
		this.arquivo = arquivo;
		this.dataDownload = dataDownload;
		this.usuarioNome = usuarioNome;
		this.nomeCidade = nomeCidade;
		this.acao = acao;
		this.pagina = pagina;
	}
	
	public DadosDownloadDTO(Long id, String acao) {
		this.id = id;
		this.acao = acao;
	}
	
	public DadosDownloadDTO(String pagina, Long id) {
		this.id = id;
		this.pagina = pagina;
	}
	
	public DadosDownloadDTO(Long id, String nomeCidade, Long cidade) {
		this.id = id;
		this.cidade = cidade;
		this.nomeCidade = nomeCidade;
	}
	
	 public DadosDownload toEntityInsert() {
		return new DadosDownload( id, null, email, nome, organizacao, boletim, arquivo, dataDownload, null, nomeCidade, acao, pagina);
		 }

	 public DadosDownload toEntityUpdate(DadosDownload dadosDownload) {
		dadosDownload.setId(this.id);
		dadosDownload.setEmail(this.email);
		dadosDownload.setNome(this.nome);
		dadosDownload.setOrganizacao(this.organizacao);
		dadosDownload.setBoletim(this.boletim);
		dadosDownload.setArquivo(this.arquivo);
		dadosDownload.setDataDownload(this.dataDownload);
		
		return dadosDownload;
		
	 }
}