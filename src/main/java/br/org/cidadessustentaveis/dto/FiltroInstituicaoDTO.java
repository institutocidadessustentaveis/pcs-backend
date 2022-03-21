package br.org.cidadessustentaveis.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @Getter @Setter @NoArgsConstructor
public class FiltroInstituicaoDTO {
		private Long idInstituicao;
		
		public FiltroInstituicaoDTO(Long idInstituicao) {
			this.idInstituicao = idInstituicao;
		}
}
