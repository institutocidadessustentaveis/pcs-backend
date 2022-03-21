package br.org.cidadessustentaveis.dto;


import java.util.ArrayList;
import java.util.List;

import br.org.cidadessustentaveis.model.participacaoCidada.Pergunta;
import br.org.cidadessustentaveis.model.participacaoCidada.SecaoFormulario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class SecaoFormularioDTO {
	private Long id;
	private String nome;
	private Integer ordem;
	private List<PerguntaDTO> perguntas;
	
	public SecaoFormulario toEntity() {
		if(id != null && id <= 0) {
			id = null;
		}
		List<Pergunta> listaPerguntas = new ArrayList<>();
		if (perguntas != null ) {
			this.perguntas.forEach(p -> {
				listaPerguntas.add(p.toEntity());
			});
		}
		SecaoFormulario secaoFormulario = new SecaoFormulario(id, nome, ordem, null,listaPerguntas);
		listaPerguntas.forEach(p -> p.setSecao(secaoFormulario));
		return secaoFormulario;
	}

	public SecaoFormularioDTO(SecaoFormulario s) {
		this.id = s.getId();
		this.nome = s.getNome();
		this.ordem = s.getOrdem();
		this.perguntas = new ArrayList<PerguntaDTO>();
		if(s.getPerguntas() != null) {
			s.getPerguntas().forEach(p -> this.perguntas.add(new PerguntaDTO(p)));
		}
	}
}
