package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.SolicitacoesBoasPraticas;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class SolicitacoesBoasPraticasDTO {
	private Long id;
	
    @NotNull
	private String solicitacao;
	
    @NotNull
	private String nomeUsuario;
    
	private Long idCidade;
	
    @NotNull
	private String nomeCidade;
    
    private String nomeEstado;
    
    private String nomePais;
    
    @NotNull
    private LocalDate dataPublicacao;
    
    @NotNull
    private LocalTime horarioPublicacao;
    
    @NotNull
    private LocalDateTime dataHorarioPublicacao;
    
    public SolicitacoesBoasPraticas toEntityInsert(SolicitacoesBoasPraticasDTO solicitacoesBoasPraticasDTO) {
    	return new SolicitacoesBoasPraticas(null, solicitacao, nomeUsuario, null, dataPublicacao, horarioPublicacao);
    }
    
    public SolicitacoesBoasPraticas toEntityUpdate(SolicitacoesBoasPraticas solicitacoesBoasPraticas) {
    	this.solicitacao = solicitacoesBoasPraticas.getSolicitacao();
    	return solicitacoesBoasPraticas;
    }
    
    //Service - buscarSolicitacaoPorId
    public SolicitacoesBoasPraticasDTO(SolicitacoesBoasPraticas solicitacoesBoasPraticas) {
    	this.id = solicitacoesBoasPraticas.getId();
    	this.solicitacao = solicitacoesBoasPraticas.getSolicitacao();
    	this.idCidade =  solicitacoesBoasPraticas.getCidade() != null ? solicitacoesBoasPraticas.getCidade().getId(): null;
    	this.nomeCidade =  solicitacoesBoasPraticas.getCidade() != null ? solicitacoesBoasPraticas.getCidade().getNome(): null;
    	this.nomeEstado = solicitacoesBoasPraticas.getCidade() != null ? solicitacoesBoasPraticas.getCidade().getProvinciaEstado().getNome() : null;
    	this.nomePais = solicitacoesBoasPraticas.getCidade() != null ? solicitacoesBoasPraticas.getCidade().getProvinciaEstado().getPais().getNome() : null;
    	this.dataPublicacao = solicitacoesBoasPraticas.getDataPublicacao();
    	this.horarioPublicacao = solicitacoesBoasPraticas.getHorarioPublicacao();
    	this.nomeUsuario = solicitacoesBoasPraticas.getNomeUsuario();
    	this.dataPublicacao = solicitacoesBoasPraticas.getDataPublicacao();
    	this.horarioPublicacao = solicitacoesBoasPraticas.getHorarioPublicacao();
    }
    
    //Query BuscarSolicitacoesBoasPraticasToList()
    public SolicitacoesBoasPraticasDTO(Long id, String solicitacao, String nomeUsuario, LocalDate dataPublicacao, LocalTime horarioPublicacao, Cidade cidade) {
    	this.id = id;
    	this.solicitacao = solicitacao;
    	this.nomeUsuario = nomeUsuario;
    	this.dataPublicacao = dataPublicacao;
    	this.horarioPublicacao = horarioPublicacao;
    	if (this.dataPublicacao != null && this.horarioPublicacao != null) {
			this.dataHorarioPublicacao = LocalDateTime.of(dataPublicacao, horarioPublicacao);
    	}
    	if (cidade != null) {
    		this.nomeCidade = cidade.getNome();
    		this.nomeEstado = cidade.getProvinciaEstado().getNome();
    		this.nomePais = cidade.getProvinciaEstado().getPais().getNome();
    		this.idCidade = cidade.getId();
    	}
    }
}
