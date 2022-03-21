package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.biblioteca.Biblioteca;
import br.org.cidadessustentaveis.model.contribuicoesAcademicas.GrupoAcademico;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class GrupoAcademicoDTO {

	 private Long id;
	 
	 private String tipoCadastro;
	 
	 private Long usuario;
	 
	 private LocalDate dataCadastro;
	 
	 private String continente;
	 
	 private Long pais;
	 
	 private Long estado;
	 
	 private String nomeEstado;
	 	
	 private Long cidade;
	 
	 private String nomeCidade;
	 
	 private String tipo;
	 
	 private List<Long> areasInteresse;
	 
	 private String nomeGrupo;
	 
	 private String paginaOnline;
	 
	 private String nomeContato;
 
	 private String emailContato;
	 
	 private String telefoneContato;
	 
	 private String emailInstitucional;
	 
	 private String telefoneInstitucional;
	 
	 private String linkBaseDados;
	 
	 private List<Long> eixos;
	 
	 private List<Long> ods;
	 
	 private String observacoes;
	 
	 private String descricaoInstituicao;
	 
	 private boolean possuiExperiencias;
	 
	 private String experienciasDesenvolvidas;
	 
	 private String logradouro;
	 
	 private String numero;
	 
	 private String complemento;
	 
	 private Double latitude;
		
	 private Double longitude;
	
	 private Long quantidadeAlunos;
	 
	 private String nomeAcademia;
	 
	 private boolean participaApl;
	
	 private String nomeApl;
	 
	 private List<String> setoresApl;
	 
	 private Long paisApl;
	 
	 private Long estadoApl;
	 
	 private List<Long> cidadesApl;
	 
	 private String descricaoApl;

	 private String porteEmpresa;
	 
	 private String setorEconomico;
	 
	 private Long quantidadeFuncionarios;
	 
	 private Long receitaAnual;
	 
	 private boolean associadaEthos;
	 
	 private boolean atuaProjetoSustentabilidade;
	 
	 private String vinculo;
	 
	 private String tipoFundacao;
	 
	 private List<Long> bibliotecas;
	 
	 public GrupoAcademicoDTO (GrupoAcademico grupoAcademico) {
		 this.id = grupoAcademico.getId();
		 this.tipo = grupoAcademico.getTipo();
		 this.numero = grupoAcademico.getNumero();
		 this.vinculo = grupoAcademico.getVinculo();
		 this.nomeApl = grupoAcademico.getNomeApl();
		 this.pais = grupoAcademico.getPais().getId();
		 this.latitude = grupoAcademico.getLatitude();
		 this.longitude = grupoAcademico.getLongitude();
		 this.nomeGrupo = grupoAcademico.getNomeGrupo();
		 this.logradouro = grupoAcademico.getLogradouro();
		 this.continente = grupoAcademico.getContinente();
		 this.estado = grupoAcademico.getEstado().getId();
		 this.cidade = grupoAcademico.getCidade().getId();
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
		 this.quantidadeAlunos = grupoAcademico.getQuantidadeAlunos();
		 this.emailInstitucional = grupoAcademico.getEmailInstitucional();
		 this.descricaoInstituicao = grupoAcademico.getDescricaoInstituicao();
		 this.telefoneInstitucional = grupoAcademico.getTelefoneInstitucional();
		 this.quantidadeFuncionarios = grupoAcademico.getQuantidadeFuncionarios();
		 this.experienciasDesenvolvidas = grupoAcademico.getExperienciasDesenvolvidas();
		 this.atuaProjetoSustentabilidade = grupoAcademico.isAtuaProjetoSustentabilidade();
		 this.eixos = grupoAcademico.getEixos().stream().map(Eixo:: getId).collect(Collectors.toList());
		 this.paisApl = grupoAcademico.getPaisApl() != null ?  grupoAcademico.getPaisApl().getId() : null;
		 this.estadoApl = grupoAcademico.getEstadoApl() != null ? grupoAcademico.getEstadoApl().getId() : null;
		 this.cidadesApl = grupoAcademico.getCidadesApl().stream().map(Cidade:: getId).collect(Collectors.toList());
		 this.cidadesApl = grupoAcademico.getCidadesApl().stream().map(Cidade:: getId).collect(Collectors.toList());
		 this.bibliotecas = grupoAcademico.getBibliotecas().stream().map(Biblioteca:: getId).collect(Collectors.toList());
		 this.ods = grupoAcademico.getOds().stream().map(ObjetivoDesenvolvimentoSustentavel:: getId).collect(Collectors.toList());
		 this.areasInteresse = grupoAcademico.getAreasInteresse().stream().map(AreaInteresse:: getId).collect(Collectors.toList());
	 }
	 
	 public GrupoAcademico toEntityInsert(GrupoAcademicoDTO grupoAcademicoDTO) {
		 return new GrupoAcademico(
				 				   null, tipoCadastro, null, dataCadastro, continente, null, null, null, tipo, null, 
				 				   nomeGrupo, paginaOnline, nomeContato, emailContato, telefoneContato, emailInstitucional,
				 				   telefoneInstitucional, linkBaseDados, null, null, observacoes, descricaoInstituicao,
				 				   possuiExperiencias, experienciasDesenvolvidas, logradouro, numero, complemento, latitude, longitude, quantidadeAlunos, nomeAcademia, participaApl, nomeApl, setoresApl, null, null, null, descricaoApl, porteEmpresa, setorEconomico, quantidadeFuncionarios, receitaAnual, associadaEthos, atuaProjetoSustentabilidade, vinculo, tipoFundacao, null
				 				   );
	 }
	 
	 public GrupoAcademico toEntityUpdate(GrupoAcademico grupoAcademico) {
		 grupoAcademico.setTipo(this.tipo);
		 grupoAcademico.setNumero(this.numero);
		 grupoAcademico.setVinculo(this.vinculo);
		 grupoAcademico.setVinculo(this.vinculo);
		 grupoAcademico.setNomeApl(this.nomeApl);
		 grupoAcademico.setLatitude(this.latitude);
		 grupoAcademico.setLongitude(this.longitude);
		 grupoAcademico.setNomeGrupo(this.nomeGrupo);
		 grupoAcademico.setLogradouro(this.logradouro);
		 grupoAcademico.setSetoresApl(this.setoresApl);
		 grupoAcademico.setContinente(this.continente);
		 grupoAcademico.setNomeContato(this.nomeContato);
		 grupoAcademico.setComplemento(this.complemento);
		 grupoAcademico.setObservacoes(this.observacoes);
		 grupoAcademico.setTipoCadastro(this.tipoCadastro);
		 grupoAcademico.setNomeAcademia(this.nomeAcademia);
		 grupoAcademico.setDescricaoApl(this.descricaoApl);
		 grupoAcademico.setPorteEmpresa(this.porteEmpresa);
		 grupoAcademico.setReceitaAnual(this.receitaAnual);
		 grupoAcademico.setTipoFundacao(this.tipoFundacao);
		 grupoAcademico.setPaginaOnline(this.paginaOnline);
		 grupoAcademico.setEmailContato(this.emailContato);
		 grupoAcademico.setParticipaApl(this.participaApl);
		 grupoAcademico.setLinkBaseDados(this.linkBaseDados);
		 grupoAcademico.setAssociadaEthos(this.associadaEthos);
		 grupoAcademico.setSetorEconomico(this.setorEconomico);
		 grupoAcademico.setAssociadaEthos(this.associadaEthos);
		 grupoAcademico.setTelefoneContato(this.telefoneContato);
		 grupoAcademico.setQuantidadeAlunos(this.quantidadeAlunos);
		 grupoAcademico.setEmailInstitucional(this.emailInstitucional);
		 grupoAcademico.setDescricaoInstituicao(this.descricaoInstituicao);
		 grupoAcademico.setTelefoneInstitucional(this.telefoneInstitucional);
		 grupoAcademico.setQuantidadeFuncionarios(this.quantidadeFuncionarios);
		 grupoAcademico.setExperienciasDesenvolvidas(this.experienciasDesenvolvidas);
		 grupoAcademico.setAtuaProjetoSustentabilidade(this.atuaProjetoSustentabilidade);

		 
		 return grupoAcademico;
	 }
	 
	 public GrupoAcademicoDTO(Long id, String nomeGrupo, GrupoAcademico ga, String tipo) {
		 this.id = id;
		 if(ga.getCidade() != null) {
			 this.cidade = ga.getCidade().getId();
			 this.nomeCidade = ga.getCidade().getNome();
		 }
		 if(ga.getEstado() != null) {		 
			 this.estado = ga.getEstado().getId();
			 this.nomeEstado = ga.getEstado().getNome(); 
		 }

		 this.nomeGrupo = nomeGrupo;
		 this.tipo = tipo;
	 }
	 
	 public GrupoAcademicoDTO(Long id, String nomeGrupo, Double latitude, Double longitude) {
		 this.id = id;
		 this.latitude = latitude;
		 this.nomeGrupo = nomeGrupo;
		 this.longitude = longitude;
	 }
}
