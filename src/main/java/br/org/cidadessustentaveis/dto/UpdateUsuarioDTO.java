package br.org.cidadessustentaveis.dto;

import lombok.Data;

@Data
public class UpdateUsuarioDTO {
	String id;
	
	String nome;
	
	String cargo;
	
	String email;
	
	String telefone_fixo;
	
	String telefone;
	
	String responsavelPcs;
	
	String responsavelIndicadores;
	
	String responsavelBoasPratica;
	
	String idPrefeitura;
	
	String senha;
	
	String senhaSemCriptografia;
}
