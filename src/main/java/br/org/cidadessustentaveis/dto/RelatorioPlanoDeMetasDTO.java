package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.org.cidadessustentaveis.model.sistema.RelatorioPlanoDeMetas;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RelatorioPlanoDeMetasDTO {

	private String nomeUsuario;
	
	private LocalDateTime dataHora;
	
	private String estadoSigla;
	
	private String estado;
	
	private String cidade;
	
	private Long codigoIBGE;
	
	private String usuarioLogado;
	
	private Long id;
	
	private LocalDate dataInicio;
	
	private LocalDate dataFim;
	
	private LocalDate inicioMandato;
	
	private LocalDate fimMandato;
	
	private Long idPlanoMetas;
	
	private Long idCidade;
	
	public RelatorioPlanoDeMetasDTO(Long id, String usuario, LocalDateTime dataHora,  String cidade, String estado, Long idPlanoMeta, Long idCidade) {
		this.nomeUsuario = usuario;
		this.dataHora = dataHora;
		this.cidade = cidade;
		this.id = id;
		this.idPlanoMetas = idPlanoMeta;
		this.idCidade = idCidade;
	}
	
	public RelatorioPlanoDeMetasDTO(RelatorioPlanoDeMetas relatorioRef) {
		this.nomeUsuario = relatorioRef.getNomeUsuario();
		this.dataHora = relatorioRef.getDataHora();
		this.cidade = relatorioRef.getCidade();
		this.id = relatorioRef.getIdPlanoDeMetas();
	}
}