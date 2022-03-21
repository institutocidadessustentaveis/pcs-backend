package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.administracao.DownloadsExportacoes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class DownloadsExportacoesDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;

	private String nomeUsuario;

	private LocalDateTime dataHora;
	
	@NotNull(message="Campo nome arquivo não pode ser nulo")
	private String nomeArquivo;
	
	private String usuarioLogado;
	
	private LocalDate dataInicio;
	
	private LocalDate dataFim;

	public DownloadsExportacoesDTO(DownloadsExportacoes downloadsExportacoes) {
		super();
		this.id = downloadsExportacoes.getId();
		this.nomeUsuario = downloadsExportacoes.getNomeUsuario();
		this.dataHora = downloadsExportacoes.getDataHora();
		this.nomeArquivo = downloadsExportacoes.getNomeArquivo();
		this.usuarioLogado = downloadsExportacoes.getUsuarioLogado();
	}
	
	public DownloadsExportacoes toEntityInsert() {
		return new DownloadsExportacoes(null,this.nomeUsuario, this.dataHora, this.nomeArquivo, this.usuarioLogado);	
	}
	
	public DownloadsExportacoes toEntityUpdate(DownloadsExportacoes objRef) {
		objRef.setNomeUsuario(nomeUsuario);
		objRef.setDataHora(dataHora);
		objRef.setNomeArquivo(nomeArquivo);
		return objRef;
	}
	
	public DownloadsExportacoesDTO(LocalDateTime dataHora, String nomeUsuario, String nomeArquivo) {
		this.dataHora = dataHora;
		this.nomeUsuario = nomeUsuario;
		this.nomeArquivo = nomeArquivo;
		
		
	}
}
