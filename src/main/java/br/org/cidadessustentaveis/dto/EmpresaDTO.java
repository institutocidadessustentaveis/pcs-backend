package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.contribuicoesAcademicas.GrupoAcademico;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class EmpresaDTO {
	private String nomeGrupo;
	
	private Long estado;
	
	private Long cidade;
	
	public EmpresaDTO (GrupoAcademico empresa) {
		this.nomeGrupo = empresa.getNomeGrupo();
		this.estado = empresa.getEstado().getId();
		this.cidade = empresa.getCidade().getId();
	}
}
