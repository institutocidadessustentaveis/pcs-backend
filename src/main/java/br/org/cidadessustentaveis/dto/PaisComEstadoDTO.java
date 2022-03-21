package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import br.org.cidadessustentaveis.model.administracao.Pais;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class PaisComEstadoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	@NotBlank(message = "Pais é obrigatório")
	private String nome;
	
	private List<ProvinciaEstadoPaisDTO> provinciaEstado;
	

	public PaisComEstadoDTO(Pais obj) {
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.provinciaEstado = new ArrayList<ProvinciaEstadoPaisDTO>();
		obj.getEstados().forEach(itemProvinciaEstado -> this.provinciaEstado.add(ProvinciaEstadoPaisDTO.builder()
				.id(itemProvinciaEstado.getId())
				.nome(itemProvinciaEstado.getNome()).build()));
	}	
}
