package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.administracao.Cidade;
import lombok.AllArgsConstructor;
import lombok.Data;
	
@Data @AllArgsConstructor
public class CidadeDetalheDTO implements Serializable{
	
	private static final long serialVersionUID = 5036244855993591196L;
	
	private Long id;
    private String nome;
    private Long populacao;
    private Long anoDaPopulacao;
    private Long idEstado;
    private String nomeEstado;
    private String siglaEstado;
    private String nomePais;
    private String nomeContinente;
	private Double latitude;
	private Double longitude;
	
	public CidadeDetalheDTO(Cidade cidade) {
		super();
		setId(cidade.getId());
		this.setNome(cidade.getNome());
		this.setPopulacao(cidade.getPopulacao());
		this.setAnoDaPopulacao(cidade.getAnoDaPopulacao());
		this.setIdEstado(cidade.getProvinciaEstado().getId());
		this.setNomeEstado(cidade.getProvinciaEstado().getNome());
		this.setSiglaEstado(cidade.getProvinciaEstado().getSigla());
		this.setNomePais(cidade.getProvinciaEstado().getPais().getNome());
		this.setNomeContinente(cidade.getProvinciaEstado().getPais().getContinente());
		this.setLatitude(cidade.getLatitude());
		this.setLongitude(cidade.getLongitude());
		
	}
	
	
		
}