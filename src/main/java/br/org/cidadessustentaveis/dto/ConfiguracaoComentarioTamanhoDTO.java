package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.participacaoCidada.ConfiguracaoComentario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ConfiguracaoComentarioTamanhoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Long tamanhoComentario;
	
	
	public ConfiguracaoComentarioTamanhoDTO(ConfiguracaoComentario configuracaoComentario) {
		this.id = configuracaoComentario.getId();
		this.tamanhoComentario = configuracaoComentario.getTamanhoComentario();
	}
	
}
