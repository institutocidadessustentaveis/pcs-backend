package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import br.org.cidadessustentaveis.model.boaspraticas.SolucaoBoaPratica;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SolucaoBoaPraticaDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String _id;
	
	private BoaPraticaDTO boaPratica;
	
	private String tema;
	
	private String nome;
	
	private String caracterizacaoSolucao;
	
	public SolucaoBoaPraticaDTO(SolucaoBoaPratica solucao) {
		this._id = solucao.get_id();
		this.id = solucao.getId();
		this.tema = solucao.getTema();	
		this.nome = solucao.getNome();
		this.caracterizacaoSolucao = solucao.getCaracterizacaoSolucao();
		}
	
	public SolucaoBoaPratica toEntityInsert(BoaPratica boaPratica) {
		SolucaoBoaPratica solucaoRef = new SolucaoBoaPratica();
		solucaoRef.set_id(this._id);
		solucaoRef.setId(this.id);
		solucaoRef.setBoaPratica(boaPratica);
		solucaoRef.setTema(this.tema);
		solucaoRef.setNome(this.nome);
		solucaoRef.setCaracterizacaoSolucao(this.caracterizacaoSolucao);
		
		return solucaoRef;
	}
	
	
}