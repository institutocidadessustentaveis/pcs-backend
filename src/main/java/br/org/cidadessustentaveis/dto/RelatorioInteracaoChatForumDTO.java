package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class RelatorioInteracaoChatForumDTO {

	private String nomeDoUsuario;
	
	private LocalDate dataHora;
	
	private String ferramenta;
	
	private String tipoInteracao;
	
	private String usuarioLogado;
}
