package br.org.cidadessustentaveis.dto;

import java.util.ArrayList;
import java.util.List;

import br.org.cidadessustentaveis.model.planjementoIntegrado.TemaGeoespacial;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class TemaGeoespacialDTO {
	private Long id;
	private String nome;
	private String descricao;
	private List<Long> areasInteresse = new ArrayList<>();
	private List<Long> eixos = new ArrayList<>();
	private List<Long> ods = new ArrayList<>();
	private List<Long> metas = new ArrayList<>();
	
	public TemaGeoespacialDTO(TemaGeoespacial tg) {
		this.id = tg.getId();
		this.nome = tg.getNome();
		this.descricao = tg.getDescricao();
		
		if(tg.getAreasInteresse() != null) {			
			tg.getAreasInteresse().forEach(it ->{
				areasInteresse.add(it.getId());
			});
		}
		if(tg.getEixos() != null) {			
			tg.getEixos().forEach(it ->{
				eixos.add(it.getId());
			});
		}
		if(tg.getOds() != null) {			
			tg.getOds().forEach(it ->{
				ods.add(it.getId());
			});
		}
		if(tg.getMetas() != null) {			
			tg.getMetas().forEach(it ->{
				metas.add(it.getId());
			});
		}
	}
	
	
	
}
