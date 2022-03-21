package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.sistema.AtividadeGestorMunicipal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class AtividadeGestorMunicipalDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String nomeUsuario;

	private LocalDateTime dataHora;
	
	private String estado;
	
	private String cidade;
	
	private String acao;
	
	private LocalDate dataInicio;
	
	private LocalDate dataFim;
	
	
	private String usuarioLogado;
	
	public AtividadeGestorMunicipalDTO(LocalDateTime dataHora, String nomeUsuario, String cidade ,String acao) {
		this.dataHora = dataHora;
		this.nomeUsuario = nomeUsuario;
		this.acao = acao;
		this.cidade = cidade;
	}
	
	public AtividadeGestorMunicipalDTO(AtividadeGestorMunicipal atividadeGestorMunicipal) {
		super();
		this.id = atividadeGestorMunicipal.getId();
		this.nomeUsuario = atividadeGestorMunicipal.getNomeUsuario();
		this.dataHora = atividadeGestorMunicipal.getDataHora();
		this.acao = atividadeGestorMunicipal.getAcao();
		this.cidade = atividadeGestorMunicipal.getCidade();
		this.usuarioLogado = atividadeGestorMunicipal.getUsuarioLogado();
		
	}
	
	public AtividadeGestorMunicipal toEntityInsert() {
		return new AtividadeGestorMunicipal(this.nomeUsuario, this.dataHora, this.acao, this.cidade, this.estado, this.usuarioLogado);	
	}
	
	public AtividadeGestorMunicipal toEntityUpdate(AtividadeGestorMunicipal objRef) {
		objRef.setNomeUsuario(nomeUsuario);
		objRef.setDataHora(dataHora);
		objRef.setCidade(cidade);
		objRef.setAcao(acao);
		return objRef;
	}
	
}
