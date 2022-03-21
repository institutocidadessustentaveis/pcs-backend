package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.boaspraticas.FonteReferenciaBoaPratica;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FonteReferenciaBoaPraticaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String nomeArquivo;
	
	private String extensao;
	
	private String conteudo;
	
	public FonteReferenciaBoaPraticaDTO(FonteReferenciaBoaPratica objRef) {
		this.id = objRef.getId();
		this.nomeArquivo = objRef.getNomeArquivo();
		this.extensao = objRef.getExtensao();
	}
	
	public FonteReferenciaBoaPratica toEntityInsert() {
		return new FonteReferenciaBoaPratica(null, this.nomeArquivo, this.extensao, this.conteudo, null);
	}
}
