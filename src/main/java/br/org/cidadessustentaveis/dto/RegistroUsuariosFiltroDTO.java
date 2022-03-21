package br.org.cidadessustentaveis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor 
public class RegistroUsuariosFiltroDTO {

	private Long tipoUsuario;
	private String instituicao;
	private String usuarioLogado;
}
