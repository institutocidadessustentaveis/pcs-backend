package br.org.cidadessustentaveis.dto;

import static java.time.Duration.between;
import static org.apache.commons.lang.time.DurationFormatUtils.formatDuration;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import br.org.cidadessustentaveis.model.sistema.HistoricoSessaoUsuario;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class RelatorioSessaoUsuarioDTO implements Serializable {

	private static final String SESSAO_ATIVA = "Sessão ativa";

	private static final long serialVersionUID = 4636501757658898623L;
	
	private Long id;
	
	private String nomeUsuario;
	
	private LocalDateTime inicioSessao;
	
	private LocalDateTime fimSessao;
	
	private String duracao = SESSAO_ATIVA;
	
	private String usuarioLogado;
	
	private LocalDate dataInicio;
	
	private LocalDate dataFim;
	
	private String cidadePrefeitura;
	
	public RelatorioSessaoUsuarioDTO(String nomeUsuario, LocalDateTime inicioSessao, LocalDateTime fimSessao, String nomeCidade) {
		this.nomeUsuario = nomeUsuario;
		this.inicioSessao = inicioSessao;
		this.fimSessao = fimSessao;
		if (fimSessao != null) {
			this.duracao = formatDuration(between(inicioSessao, fimSessao).toMillis(), "HH:mm:ss");
		}
		if(nomeCidade != null) {
			this.cidadePrefeitura = nomeCidade;
		}
	}
	
	public RelatorioSessaoUsuarioDTO(HistoricoSessaoUsuario sessao) {
		super();
		this.id = sessao.getId();
		this.nomeUsuario = sessao.getUsuario().getNome();
		this.inicioSessao = sessao.getInicioSessao();
		this.fimSessao = sessao.getFimSessao();
		if (sessao.getFimSessao() != null) {
			this.duracao = formatDuration(between(sessao.getInicioSessao(), sessao.getFimSessao()).toMillis(), "HH:mm:ss");
		}
		
		
	}
}
