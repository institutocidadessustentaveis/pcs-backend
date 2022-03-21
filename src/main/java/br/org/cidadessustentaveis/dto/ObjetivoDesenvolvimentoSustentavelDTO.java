package br.org.cidadessustentaveis.dto;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ObjetivoDesenvolvimentoSustentavelDTO {

	private Long id;
	
	@NotNull(message = "Obrigatório preenchimento do número do ODS.")
	private int numero;
	
	@NotBlank(message = "Obrigatório preenchimento do titulo.")
	private String titulo;
	
	@NotBlank(message = "Obrigatório preenchimento do subtítulo.")
	private String subtitulo;
	
	@NotBlank(message = "Obrigatório preenchimento da descrição.")
	private String descricao;
	@NotBlank(message = "Obrigatório preenchimento do ícone.")
	private String icone;
	@NotBlank(message = "Obrigatório preenchimento do ícone reduzido.")
	private String iconeReduzido;
	
	private Set<MetaObjetivoDesenvolvimentoSustentavel> metas;
	
	public ObjetivoDesenvolvimentoSustentavelDTO(ObjetivoDesenvolvimentoSustentavel ods) {
		super();
		this.id = ods.getId();
		this.numero = ods.getNumero();
		this.titulo = ods.getTitulo();
		this.subtitulo = ods.getSubtitulo();
		this.descricao = ods.getDescricao();
		this.icone = ods.getIcone();
		this.iconeReduzido = ods.getIconeReduzido();
	}


	public ObjetivoDesenvolvimentoSustentavel toEntityInsert() {
		return ObjetivoDesenvolvimentoSustentavel.builder()
					.numero(this.numero)
					.titulo(this.titulo)
					.subtitulo(this.subtitulo)
					.descricao(this.descricao)
					.icone(this.getIcone())
					.iconeReduzido(this.getIconeReduzido())
					.metas(this.getMetas())
					.build();
	}
	
	public ObjetivoDesenvolvimentoSustentavel toEntityUpdate(ObjetivoDesenvolvimentoSustentavel ods) {
		ods.setNumero(this.numero);
		ods.setTitulo(this.titulo);
		ods.setSubtitulo(this.subtitulo);
		ods.setDescricao(this.descricao);
		ods.setIcone(this.icone);
		ods.setIconeReduzido(this.iconeReduzido);
		ods.setMetas(this.metas);
		return ods;
	}
	
}
