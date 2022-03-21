package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.administracao.Responsavel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter @NoArgsConstructor
public class ResponsavelDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String nome;
	
	private String email;
	
	private String telefoneFixo;
	
	private String celular;
	
	private String cargo;
	
	private Boolean recebeEmail;
	

	
	public Responsavel toEntityInsertResponsavel() {
		Responsavel responsavel = new Responsavel();
		
		responsavel.setId(null);
		responsavel.setNome(this.nome);
		responsavel.setEmail(this.email);
		responsavel.setTelefoneFixo(this.telefoneFixo);
		responsavel.setCelular(this.celular);
		responsavel.setCargo(this.cargo);
		responsavel.setRecebeEmail(this.recebeEmail);
		
		return new Responsavel();
	}
	
	
	public ResponsavelDTO(Responsavel adicionarResponsavel) {
		this.id = adicionarResponsavel.getId();
		this.nome = adicionarResponsavel.getNome();
		this.email = adicionarResponsavel.getEmail();
		this.telefoneFixo = adicionarResponsavel.getTelefoneFixo();
		this.celular = adicionarResponsavel.getCelular();
		this.cargo =  adicionarResponsavel.getCargo();
		this.recebeEmail = adicionarResponsavel.getRecebeEmail();

	}

}
