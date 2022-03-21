package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.model.participacaoCidada.FormularioPreenchido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormularioPreenchidoDTO {

	private Long id;
	private LocalDate dataPreenchimento;
	private LocalTime horario;
	private String usuarioNome;
	private String logado;
	private FormularioDTO formulario;
	private List<FormularioPreenchidoRespostaDTO> respostas = new ArrayList<FormularioPreenchidoRespostaDTO>();
	private String formularioNome;
	
	public FormularioPreenchidoDTO(FormularioPreenchido formulario) {
		this.id = formulario.getId();
		this.dataPreenchimento = formulario.getDataPreenchimento();
		this.horario = formulario.getHorario();
		this.usuarioNome = formulario.getUsuario() != null ? formulario.getUsuario().getNome() : "";
		this.logado = formulario.getEstavaLogado() == true ? "Sim" : "Não";
		this.formulario = formulario.getFormulario() != null ? new FormularioDTO(formulario.getFormulario()) : null;
		this.respostas = formulario.getRespostas() != null ? formulario.getRespostas().stream().map(resposta -> new FormularioPreenchidoRespostaDTO(resposta)).collect(Collectors.toList()) : null;
		this.formularioNome = formulario.getFormulario() != null ?  formulario.getFormulario().getNome() : null;
	}
	
}
