package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.administracao.EmailToken;
import br.org.cidadessustentaveis.model.enums.FuncionalidadeToken;
import lombok.Data;

@Data
public class EmailTokenDTO implements Serializable{
	private static final long serialVersionUID = -5752171686074283374L;

	private Boolean ativo;
	private String hash;
	private FuncionalidadeToken funcionalidade;
	private AprovacaoPrefeituraDTO aprovacaoPrefeitura;
	private UsuarioDTO usuario;
	
	public EmailTokenDTO(EmailToken emailToken) {
		super();
		this.ativo = emailToken.getAtivo();
		this.hash = emailToken.getHash();
		this.funcionalidade = emailToken.getFuncionalidadeToken();
		if(emailToken.getAprovacaoPrefeitura() != null) {
			this.aprovacaoPrefeitura  = new AprovacaoPrefeituraDTO(emailToken.getAprovacaoPrefeitura());
		}
		if(emailToken.getUsuario() != null) {
			this.usuario  = new UsuarioDTO(emailToken.getUsuario());
		}
	}
	
	public EmailToken toEntityInsert() {
		return EmailToken.builder()
				.funcionalidadeToken(this.funcionalidade)
				.ativo(this.ativo)
				.hash(this.hash)
				.build();	
	}
	
	public EmailToken toEntityUpdate(EmailToken objRef) {
		objRef.setHash(this.hash);
		objRef.setAtivo(this.ativo);
		objRef.setFuncionalidadeToken(this.funcionalidade);
		return objRef;
	}
}
