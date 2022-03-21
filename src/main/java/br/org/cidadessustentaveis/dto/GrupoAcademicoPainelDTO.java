package br.org.cidadessustentaveis.dto;

import java.util.ArrayList;
import java.util.List;

import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.biblioteca.Biblioteca;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GrupoAcademicoPainelDTO {
	private Long id;
	private String nomeCidade;
	private String nomeEstado;
	private String nomePais;
	private String nomeGrupo;
	private String tipo;
	private String paginaOnline;
	private String nomeContato;
	private String emailContato;
	private String telefoneContato;
	private String emailInstitucional;
	private String telefoneInstitucional;
	private String linkBaseDados;
	private String observacoes;
	private String descricaoInstituicao;
	private String experienciasDesenvolvidas;
	private String logradouro;
	private String numero;
	private String complemento;
	private Long quantidadeAlunos;
	private String nomeAcademia;
	private String nomeApl;
	private String descricaoApl;
	private String areasInteresse;
	private String eixos;
	private String ods;
	private List<GrupoAcademicoBibliotecaPainelDTO> bibliotecas;

	public GrupoAcademicoPainelDTO(Long id, String nomeGrupo, String nomePais, String nomeEstado, String nomeCidade, String tipo, String paginaOnline, 
			String nomeContato, String emailContato, String telefoneContato, String emailInstitucional, String telefoneInstitucional, String linkBaseDados, 
			String observacoes, String descricaoInstituicao, String experienciasDesenvolvidas, String logradouro, String numero, String complemento, 
			Long quantidadeAlunos, String nomeAcademia, String nomeApl, String descricaoApl) {
		this.id = id;
		this.nomeGrupo = nomeGrupo;
		this.nomeCidade = nomeCidade;
		this.nomeEstado = nomeEstado;
		this.nomePais = nomePais;
		this.tipo = tipo;
		this.paginaOnline = paginaOnline;
		this.nomeContato = nomeContato;
		this.emailContato = emailContato;
		this.telefoneContato = telefoneContato;
		this.emailInstitucional = emailInstitucional;
		this.telefoneInstitucional = telefoneInstitucional;
		this.linkBaseDados = linkBaseDados;
		this.observacoes =  observacoes != null ? observacoes.replaceAll("\\<.*?>","").replace("&nbsp;","") : null;
		this.descricaoInstituicao = descricaoInstituicao != null ? descricaoInstituicao.replaceAll("\\<.*?>","").replace("&nbsp;","") : null;
		this.experienciasDesenvolvidas = experienciasDesenvolvidas != null ? experienciasDesenvolvidas.replaceAll("\\<.*?>","").replace("&nbsp;","") : null;
		this.logradouro = logradouro;
		this.numero = numero;
		this.complemento = complemento;
		this.quantidadeAlunos = quantidadeAlunos;
		this.nomeAcademia = nomeAcademia;
		this.quantidadeAlunos = quantidadeAlunos;
		this.nomeAcademia = nomeAcademia;
		this.nomeApl = nomeApl;
		this.descricaoApl = descricaoApl != null ? descricaoApl.replaceAll("\\<.*?>","").replace("&nbsp;","") : null;
	}
	
    public void listarAreasInteresse(List<AreaInteresse> listaAreaInteresse) {
    	this.areasInteresse = "";
    	for(AreaInteresse elemento: listaAreaInteresse) {
    		this.areasInteresse = this.areasInteresse + elemento.getNome() + " ";
    	}
    }
	
    public void listarEixos(List<Eixo> listaEixos) {
    	this.eixos = "";
    	for(Eixo elemento: listaEixos) {
    		this.eixos = this.eixos + elemento.getNome() + " ";
    	}
    }
	
    public void listarOds(List<ObjetivoDesenvolvimentoSustentavel> listaOds) {
    	this.ods = "";
    	for(ObjetivoDesenvolvimentoSustentavel elemento: listaOds) {
    		this.ods = this.ods + elemento.getTitulo() + " ";
    	}
    }
    
    public void listarBibliotecas(List<Biblioteca> listaBibliotecas) {
    	List<GrupoAcademicoBibliotecaPainelDTO> bibliotecasGrupos = new ArrayList<GrupoAcademicoBibliotecaPainelDTO>();
    	for(Biblioteca elemento: listaBibliotecas) {
    		GrupoAcademicoBibliotecaPainelDTO bibliotecaGrupo = new GrupoAcademicoBibliotecaPainelDTO();
    		bibliotecaGrupo.setId(elemento.getId() != null ? elemento.getId() : null);
    		bibliotecaGrupo.setTituloBiblioteca(elemento.getTituloPublicacao() != null ? elemento.getTituloPublicacao() : null);
    		bibliotecasGrupos.add(bibliotecaGrupo);
    	}
    	this.bibliotecas = bibliotecasGrupos.size() >= 1 ? bibliotecasGrupos : null;
    }
}
