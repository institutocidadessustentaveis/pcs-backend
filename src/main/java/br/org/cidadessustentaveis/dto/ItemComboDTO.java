package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.administracao.DadosDownload;
import br.org.cidadessustentaveis.model.administracao.DownloadsExportacoes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @NoArgsConstructor @AllArgsConstructor @Builder @Getter @Setter
public class ItemComboDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String label;
	
	public ItemComboDTO(Long id, String label1, String label2) {
		this.id = id;
		this.label = label1 + " - " + label2;
	}
	
	public ItemComboDTO(Long id, Integer label1, String label2) {
		this.id = id;
		this.label = label1 + " - " + label2;
	}

	public ItemComboDTO(DownloadsExportacoes obj) {
		this.id = obj.getId();
		this.label = obj.getNomeArquivo();
	} 
	
	public ItemComboDTO(DadosDownloadDTO obj) {
		this.id = obj.getId();
		if(null != obj.getAcao() ) {
			this.label = obj.getAcao();
		}
		if(null != obj.getPagina()) {
			this.label = obj.getPagina();
		}
		if(null != obj.getNomeCidade()) {
			this.label = obj.getNomeCidade();
		}
	}  
	
	public ItemComboDTO(String obj) {
		this.label = obj;
	}   
	
	public ItemComboDTO(Long id, String nomeCidade, String nomeEstado, String nomePais) {
		this.id = id;
		this.label = nomeCidade + " - " + nomeEstado + " - " + nomePais;
	}
}              
