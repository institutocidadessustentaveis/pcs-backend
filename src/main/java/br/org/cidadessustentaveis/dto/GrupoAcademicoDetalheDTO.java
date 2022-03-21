package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.biblioteca.Biblioteca;
import br.org.cidadessustentaveis.model.contribuicoesAcademicas.GrupoAcademico;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class GrupoAcademicoDetalheDTO {

	 private Long id;
	 private String tipoCadastro;
	 private Long usuario;
	 private LocalDate dataCadastro;
	 private String continente;
	 private String pais;
	 private String estado;
	 private String cidade;
	 private List<String> areasInteresse;
	 private String nomeGrupo;
	 private String paginaOnline;
	 private String nomeContato;
	 private String emailContato;
	 private String telefoneContato; 
	 private String emailInstitucional;
	 private String telefoneInstitucional;
	 private String linkBaseDados;
	 private List<EixoDTO> eixos;
	 private List<String> ods;
	 private String observacoes;
	 private String descricaoInstituicao;
	 private String experienciasDesenvolvidas;
	 private String logradouro;
	 private String numero;
	 private String complemento;
	 private Double latitude;
	 private Double longitude;
	 private String nomeAcademia;
	 private Boolean participaApl;
	 private String nomeApl;
	 private List<String> setoresApl;
	 private List<String> cidadesApl;
	 private String descricaoApl;
	 private String porteEmpresa;
	 private String setorEconomico;
	 private Long quantidadeFuncionarios;
	 private Long receitaAnual;
	 private Boolean associadaEthos;
	 private Boolean atuaProjetoSustentabilidade;
	 private String vinculo;
	 private String tipo;
	 private Long quantidadeAlunos;
	 //private List<String> bibliotecas;
	 
	 public GrupoAcademicoDetalheDTO(GrupoAcademico grupoAcademico) {
		 this.id = grupoAcademico.getId();
		 this.numero = grupoAcademico.getNumero();
		 this.vinculo = grupoAcademico.getVinculo();
		 this.nomeApl = grupoAcademico.getNomeApl();
		 if(this.pais != null) {
			 this.pais = grupoAcademico.getPais().getNome(); 
		 }
		 this.latitude = grupoAcademico.getLatitude();
		 this.longitude = grupoAcademico.getLongitude();
		 this.nomeGrupo = grupoAcademico.getNomeGrupo();
		 this.logradouro = grupoAcademico.getLogradouro();
		 this.continente = grupoAcademico.getContinente();
		 this.complemento = grupoAcademico.getComplemento();
		 this.nomeContato = grupoAcademico.getNomeContato();
		 this.observacoes = grupoAcademico.getObservacoes();
		 this.participaApl = grupoAcademico.isParticipaApl();
		 this.tipoCadastro = grupoAcademico.getTipoCadastro();
		 this.receitaAnual = grupoAcademico.getReceitaAnual();
		 this.paginaOnline = grupoAcademico.getPaginaOnline();
		 this.emailContato = grupoAcademico.getEmailContato();
		 this.descricaoApl = grupoAcademico.getDescricaoApl();
		 this.porteEmpresa = grupoAcademico.getPorteEmpresa();
		 this.linkBaseDados = grupoAcademico.getLinkBaseDados();
		 this.associadaEthos = grupoAcademico.isAssociadaEthos();
		 this.setorEconomico = grupoAcademico.getSetorEconomico();
		 this.telefoneContato = grupoAcademico.getTelefoneContato();
		 this.emailInstitucional = grupoAcademico.getEmailInstitucional();
		 this.descricaoInstituicao = grupoAcademico.getDescricaoInstituicao();
		 this.telefoneInstitucional = grupoAcademico.getTelefoneInstitucional();
		 this.quantidadeFuncionarios = grupoAcademico.getQuantidadeFuncionarios();
		 this.experienciasDesenvolvidas = grupoAcademico.getExperienciasDesenvolvidas();
		 this.atuaProjetoSustentabilidade = grupoAcademico.isAtuaProjetoSustentabilidade();
		 this.emailInstitucional = grupoAcademico.getEmailInstitucional();
		 this.latitude = grupoAcademico.getLatitude();
		 this.longitude = grupoAcademico.getLongitude();
		 this.nomeGrupo = grupoAcademico.getNomeGrupo();
		 this.setoresApl = grupoAcademico.getSetoresApl();
		 if(grupoAcademico.getCidade() != null) {
			 this.cidade = grupoAcademico.getCidade().getNome(); 
		 }
		 if(grupoAcademico.getEstado() != null) {
			 this.estado = grupoAcademico.getEstado().getNome(); 
		 }
		 this.quantidadeAlunos = grupoAcademico.getQuantidadeAlunos();
		 this.nomeAcademia = grupoAcademico.getNomeAcademia();
		 this.tipo = grupoAcademico.getTipo();
		 this.eixos = grupoAcademico.getEixos() != null
					? grupoAcademico.getEixos().stream().map(eixo -> new EixoDTO(eixo)).collect(Collectors.toList())
					: null;
		 this.cidadesApl = grupoAcademico.getCidadesApl().stream().map(Cidade:: getNome).collect(Collectors.toList());
		 this.ods = grupoAcademico.getOds().stream().map(ObjetivoDesenvolvimentoSustentavel:: getTitulo).collect(Collectors.toList());
		 this.areasInteresse = grupoAcademico.getAreasInteresse().stream().map(AreaInteresse:: getNome).collect(Collectors.toList());
		 //this.bibliotecas = grupoAcademico.getBibliotecas().stream().map(Biblioteca:: get).collect(Collectors.toList());
	 } 
	
}
