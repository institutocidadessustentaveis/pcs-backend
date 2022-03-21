package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.administracao.TipoSubdivisao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TipoSubdivisaoDTO {
	
	private Long id;
	
	private String nome;
	
	private Long nivel;
	
	private Long tipoPai;
	
	private PrefeituraDTO prefeitura;
	
	
	public TipoSubdivisaoDTO(TipoSubdivisao obj) {
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.nivel = obj.getNivel();
		this.tipoPai = obj.getTipoPai();
		this.prefeitura = obj.getPrefeitura() != null ? new PrefeituraDTO(obj.getPrefeitura()) : null;
	}
	
	public TipoSubdivisao toEntityInsert(TipoSubdivisaoDTO tipoSubdivisaoDTO) {
		return new TipoSubdivisao(null, nome, nivel, tipoPai, null);
	}
	
	public TipoSubdivisao toEntityUpdate(TipoSubdivisao tipoSubdivisao) {
		//Não incluido: Prefeitura
		tipoSubdivisao.setId(this.id);
		tipoSubdivisao.setNome(this.nome);
		tipoSubdivisao.setNivel(this.nivel);
		tipoSubdivisao.setTipoPai(this.tipoPai);
		
		return tipoSubdivisao;
	} 
}
