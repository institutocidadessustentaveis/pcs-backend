package br.org.cidadessustentaveis.model.participacaoCidada;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import br.org.cidadessustentaveis.dto.PerguntaDTO;
import br.org.cidadessustentaveis.dto.SecaoFormularioDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SecaoFormulario implements Serializable{

	private static final long serialVersionUID = -7865621967950749100L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private Integer ordem;
	@ManyToOne
    @JoinColumn(name = "formulario")
	private Formulario formulario; 
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="secao_formulario")
	private List<Pergunta> perguntas;

	public void atualizar(SecaoFormularioDTO dto) {
		this.setNome(this.getNome());
		this.setOrdem(this.getOrdem());
		
		if(dto.getPerguntas() != null) {
			dto.getPerguntas().forEach(perguntaDTO -> {
				if(perguntaDTO.getId() == null || perguntaDTO.getId() <= 0) {
					Pergunta pergunta = perguntaDTO.toEntity();
					pergunta.setSecao(this);
					getPerguntas().add(pergunta);
				} else {
					List<Pergunta> listaPerguntas = getPerguntas().stream().filter(fs -> fs.getId() == perguntaDTO.getId()).collect(Collectors.toList());
					if(!listaPerguntas.isEmpty()) {
						Pergunta pergunta =  listaPerguntas.get(0);
						pergunta.atualizar(perguntaDTO);
					}
				}
			});
			for(int i = 0; i < perguntas.size(); i++ ) {
				Pergunta pergunta = perguntas.get(i);
				List<PerguntaDTO> listaPerguntaDTO = dto.getPerguntas().stream().filter(f -> f.getId() == pergunta.getId()).collect(Collectors.toList());
				if(listaPerguntaDTO != null && listaPerguntaDTO.isEmpty()) {
					perguntas.remove(i);
					i--;
				}
			}
		}
	}
}
