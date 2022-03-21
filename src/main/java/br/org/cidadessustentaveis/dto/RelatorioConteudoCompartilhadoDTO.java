package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import br.org.cidadessustentaveis.model.sistema.RelatorioConteudoCompartilhado;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class RelatorioConteudoCompartilhadoDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
		
	private String nomeUsuario;

	private LocalDateTime dataHora;
		
	private String redeSocial;
	
	private String conteudoCompartilhado;
		
	private String usuarioLogado;
	
	private LocalDate dataInicio;
	
	private LocalDate dataFim;

	public RelatorioConteudoCompartilhadoDTO(RelatorioConteudoCompartilhado relatorioConteudo) {
		super();
		this.id = relatorioConteudo.getId();
		this.nomeUsuario = relatorioConteudo.getNomeUsuario();
		this.dataHora = relatorioConteudo.getDataHora();
		this.redeSocial = relatorioConteudo.getRedeSocial();
		this.conteudoCompartilhado = relatorioConteudo.getConteudoCompartilhado();
	}
	
	public RelatorioConteudoCompartilhado toEntityInsert() {
		return new RelatorioConteudoCompartilhado(null,this.nomeUsuario, this.dataHora, this.redeSocial, this.conteudoCompartilhado);	
	}
	
	public RelatorioConteudoCompartilhadoDTO(LocalDateTime dataHora, String nomeUsuario, String redeSocial, String conteudoCompartilhado) {
		this.dataHora = dataHora;
		this.nomeUsuario = nomeUsuario;
		this.redeSocial = redeSocial;
		this.conteudoCompartilhado = conteudoCompartilhado;
	}
}
