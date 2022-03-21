package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.administracao.Alerta;
import br.org.cidadessustentaveis.model.administracao.LinkRodape;
import br.org.cidadessustentaveis.model.enums.TipoAlerta;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class LinkRodapeDTO {

	private Long id;

	private Integer ordem;

	private String titulo;

	private String url;

	private Boolean abrirNovaJanela;

	public LinkRodapeDTO(LinkRodape link) {
		this.id = link.getId();
		this.ordem = link.getOrdem();
		this.titulo = link.getTitulo();
		this.url = link.getUrl();
		this.abrirNovaJanela = link.getAbrirNovaJanela();
	}

	public LinkRodape createEntity(){
		return new LinkRodape(this.id, this.ordem, this.titulo, this.url, this.abrirNovaJanela);
	}

}
