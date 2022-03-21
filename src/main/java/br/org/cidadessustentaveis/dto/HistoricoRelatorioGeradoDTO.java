package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.sistema.HistoricoRelatorioGerado;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class HistoricoRelatorioGeradoDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotNull(message="Campo nome usuário não pode ser nulo")
	private String nomeUsuario;

	private LocalDateTime dataHora;
	
	@NotNull(message="Campo nome relatorio não pode ser nulo")
	private String nomeRelatorio;
	
	private String usuarioLogado;
	
	private LocalDate dataInicio;
	
	private LocalDate dataFim;

	public HistoricoRelatorioGeradoDTO(HistoricoRelatorioGerado historicoRelatorioGerado) {
		super();
		this.id = historicoRelatorioGerado.getId();
		this.nomeUsuario = historicoRelatorioGerado.getNomeUsuario();
		this.dataHora = historicoRelatorioGerado.getDataHora();
		this.nomeRelatorio = historicoRelatorioGerado.getNomeRelatorio();
		this.usuarioLogado = historicoRelatorioGerado.getUsuarioLogado();
	}
	
	public HistoricoRelatorioGerado toEntityInsert() {
		return new HistoricoRelatorioGerado(null,this.nomeUsuario, this.dataHora, this.nomeRelatorio, this.usuarioLogado);	
	}
	
	public HistoricoRelatorioGerado toEntityUpdate(HistoricoRelatorioGerado objRef) {
		objRef.setNomeUsuario(nomeUsuario);
		objRef.setDataHora(dataHora);
		objRef.setNomeRelatorio(nomeRelatorio);
		return objRef;
	}
	
	public HistoricoRelatorioGeradoDTO(String nomeUsuario, LocalDateTime dataRelGerado, String nomeRelatorio) {
		this.setNomeUsuario(nomeUsuario);
		this.setDataHora(dataRelGerado);
		this.setNomeRelatorio(nomeRelatorio);
	}
}
