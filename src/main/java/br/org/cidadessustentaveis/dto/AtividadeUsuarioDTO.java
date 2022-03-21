package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.administracao.AtividadeUsuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class AtividadeUsuarioDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String nomeUsuario;

	private LocalDateTime dataHora;
	
	private String acao;
	
	private String modulo;
	
	private String usuarioLogado;
	
	private LocalDate dataInicio;
	
	private LocalDate dataFim;
	
	public AtividadeUsuarioDTO(String nomeUsuario, LocalDateTime dataHora, String acao, String modulo) {
		this.nomeUsuario = nomeUsuario;
		this.dataHora = dataHora;
		this.acao = acao;
		this.modulo = modulo;
	}

	public AtividadeUsuarioDTO(AtividadeUsuario atividadeUsuario) {
		super();
		this.id = atividadeUsuario.getId();
		this.nomeUsuario = atividadeUsuario.getNomeUsuario();
		this.dataHora = atividadeUsuario.getDataHora();
		this.acao = atividadeUsuario.getAcao();
		this.modulo = atividadeUsuario.getModulo();
		this.usuarioLogado = atividadeUsuario.getUsuarioLogado();
	}
	
	public AtividadeUsuario toEntityInsert() {
		return new AtividadeUsuario(null,this.nomeUsuario, this.dataHora, this.acao, this.modulo, this.usuarioLogado);	
	}
	
	public AtividadeUsuario toEntityUpdate(AtividadeUsuario objRef) {
		objRef.setNomeUsuario(nomeUsuario);
		objRef.setDataHora(dataHora);
		objRef.setAcao(acao);
		objRef.setModulo(modulo);
		return objRef;
	}
}
