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
import br.org.cidadessustentaveis.dto.RespostaDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Pergunta implements Serializable {
	private static final long serialVersionUID = -8198329493018588463L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer ordem;
	private String pergunta;
	private String tipo; //Sim ou Não, Multiplas Opções, Texto Live, Multiplas Opções com campo "outro";
	private Boolean multiplaSelecao; 
	@ManyToOne
    @JoinColumn(name = "secao_formulario")
	private SecaoFormulario secao;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="pergunta")
	private List<Resposta> respostas;


	public void atualizar(PerguntaDTO dto) {
		this.ordem = dto.getOrdem();
		this.pergunta = dto.getPergunta();
		this.tipo = dto.getTipo();
		this.multiplaSelecao = dto.getMultiplaSelecao();
		
		if(dto.getRespostas() != null) {
			dto.getRespostas().forEach(respostaDTO -> {
				if(respostaDTO.getId() == null || respostaDTO.getId() <= 0) {
					Resposta resposta = respostaDTO.toEntity();
					resposta.setPergunta(this);
					getRespostas().add(resposta);
				} else {
					List<Resposta> listaRespostas = getRespostas().stream().filter(fs -> fs.getId() == respostaDTO.getId()).collect(Collectors.toList());
					if(!listaRespostas.isEmpty()) {
						Resposta resposta =  listaRespostas.get(0);
						resposta.atualizar(respostaDTO);
					}
				}
			});
		}
		for(int i = 0; i < respostas.size(); i++ ) {
			Resposta resposta = respostas.get(i);
			List<RespostaDTO> listaRespostaDTO = dto.getRespostas().stream().filter(f -> f.getId() == resposta.getId()).collect(Collectors.toList());
			if(listaRespostaDTO != null && listaRespostaDTO.isEmpty()) {
				respostas.remove(i);
				i--;
			}
		}
	}
	
}
