package br.org.cidadessustentaveis.dto;

import lombok.Data;

@Data
public class AlterarSenhaDTO {
	private String usuario, novaSenha, senha, confirmarNovaSenha;
}
