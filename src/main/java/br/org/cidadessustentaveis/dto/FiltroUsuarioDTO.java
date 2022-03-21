package br.org.cidadessustentaveis.dto;

import java.util.Iterator;

import br.org.cidadessustentaveis.model.administracao.Credencial;
import br.org.cidadessustentaveis.model.administracao.Perfil;
import lombok.Data;

@Data
public class FiltroUsuarioDTO {

		private Long id;
		private String uf;
		private String nome;
		private String email;
		private String cidade;
		private String telefone;
		private String nomePerfil;
		private String organizacao;
		
		public FiltroUsuarioDTO (Long id, String nome, String telefone, String email, String cidade, String uf, String organizacao, Credencial credencial) {
			this.id = id;
			this.uf = uf != null ? uf : "";
			this.nome = nome != null ? nome : "";
			this.email = email != null ? email : "";
			this.cidade = cidade != null ? cidade : "";
			this.telefone = telefone != null ? telefone : "";
			this.organizacao = organizacao != null ? organizacao : "";
			
			if(credencial != null) {
				Iterator<Perfil> it = credencial.getListaPerfil().iterator();
				StringBuilder sb = new StringBuilder();
				while(it.hasNext()) {
					sb.append(it.next().getNome());
					if(it.hasNext()) {
						sb.append(" | ");
					}
				}
				this.nomePerfil = sb.toString() != null ? sb.toString() : "";
			}
		}
}