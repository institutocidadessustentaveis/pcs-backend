package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.institucional.Arquivo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArquivoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nomeArquivo;
	private String extensao;
	private String conteudo;
	
	public ArquivoDTO(Arquivo arquivoRef) {
		this.id = arquivoRef.getId();
		this.nomeArquivo = arquivoRef.getNomeArquivo();
		this.extensao = arquivoRef.getExtensao();
		this.conteudo = arquivoRef.getConteudo();
	}
	
	public Arquivo toEntityInsert() {
		return new Arquivo(null, this.nomeArquivo, this.extensao, this.conteudo);
	}
}
