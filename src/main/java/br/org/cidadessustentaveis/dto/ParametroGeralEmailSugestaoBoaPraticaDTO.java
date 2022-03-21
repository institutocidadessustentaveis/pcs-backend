package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.sistema.ParametroGeral;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ParametroGeralEmailSugestaoBoaPraticaDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String emailSugestaoBoaPratica;
	
	
	public ParametroGeralEmailSugestaoBoaPraticaDTO(ParametroGeral parametroGeral) {
		this.id = parametroGeral.getId();
		this.emailSugestaoBoaPratica = parametroGeral.getEmailSugestaoBoaPratica();
	}
	
}
