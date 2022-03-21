package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.org.cidadessustentaveis.model.administracao.HistoricoPlanoMetasPrestacaoContas;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RelatorioPlanoDeMetasPrestacaoDeContasDTO {
	
	private String usuario;
	
	private String estado;
	
	private String estadoSigla;
	
	private String cidade;
	
	private Long codigoIBGE;
	
	private String mandato;
	
	private String planoDeMetas;
	
	private LocalDateTime dataHoraUploadPlano;
	
	private String prestacaoDeContas;
	
	private LocalDateTime dataHoraUploadPrestacao;
	
	private String nomeUsuario;

	public RelatorioPlanoDeMetasPrestacaoDeContasDTO(String estado, String estadoSigla, String cidade, Long codigoIBGE, LocalDate inicioMandato,
			LocalDate fimMandato,  String planoDeMetas,	LocalDateTime dataHoraUploadPlano, 
			String prestacaoDeContas, LocalDateTime dataHoraUploadPrestacao, HistoricoPlanoMetasPrestacaoContas historico) {
		this.estado = estado;
		this.estadoSigla = estadoSigla;
		this.cidade = cidade;
		this.codigoIBGE = codigoIBGE;
		this.mandato = inicioMandato.getYear() + " - " + fimMandato.getYear();
		this.planoDeMetas = planoDeMetas;
		this.dataHoraUploadPlano = dataHoraUploadPlano;
		this.prestacaoDeContas = prestacaoDeContas;
		this.dataHoraUploadPrestacao = dataHoraUploadPrestacao;
		this.nomeUsuario = historico.getUsuario() != null ? historico.getUsuario().getNome() : null;
	}
	
}