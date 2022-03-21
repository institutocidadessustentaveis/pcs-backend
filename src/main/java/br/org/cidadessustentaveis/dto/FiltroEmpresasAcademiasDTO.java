package br.org.cidadessustentaveis.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter 
public class FiltroEmpresasAcademiasDTO {
	String tipoCadastro;
	List<Long> idAreaInteresse;
	List<Long> idEixo;
	Long idOds;
	Long idCidade;
	Long idProvinciaEstado;
	Long idPais;
	String palavraChave;
	String vinculo;
	String nomeGrupo;
	List<String> setoresApl;
	List<Long> cidadesApl;
	Long receitaAnual;
	boolean participaApl;
	boolean associadaEthos;
	String setorEconomico;
	Long quantidadeFuncionarios;
	boolean atuaProjetoSustentabilidade;
	
	public FiltroEmpresasAcademiasDTO(GrupoAcademicoDTO grupoAcademico) {
		this.tipoCadastro = grupoAcademico.getTipoCadastro();
		this.idAreaInteresse = grupoAcademico.getAreasInteresse();
		this.idEixo = grupoAcademico.getEixos();
		this.idCidade = grupoAcademico.getCidade();
		this.idProvinciaEstado = grupoAcademico.getEstado();
		this.idPais = grupoAcademico.getPais();
		this.vinculo = grupoAcademico.getVinculo();
		this.nomeGrupo = grupoAcademico.getNomeGrupo();
		this.setoresApl = grupoAcademico.getSetoresApl();
		this.cidadesApl = grupoAcademico.getCidadesApl();
		this.receitaAnual = grupoAcademico.getReceitaAnual();
		this.participaApl = grupoAcademico.isParticipaApl();
		this.associadaEthos = grupoAcademico.isParticipaApl();
		this.setorEconomico = grupoAcademico.getSetorEconomico();
		this.quantidadeFuncionarios = grupoAcademico.getQuantidadeFuncionarios();
		this.atuaProjetoSustentabilidade = grupoAcademico.isAtuaProjetoSustentabilidade();
		
		
	}
}
