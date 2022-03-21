package br.org.cidadessustentaveis.dto;

import java.util.List;

import org.wololo.geojson.Feature;

import br.org.cidadessustentaveis.model.administracao.SubdivisaoCidade;
import br.org.cidadessustentaveis.model.administracao.TipoSubdivisao;
import lombok.Data;

@Data	
public class SubdivisaoDTO {
    private Long id;	
    private Long cidade;
    private String nome;
    private String uf;
    private String nomeCidade;
    private String nomeEstado;
    private SubdivisaoDTO subdivisaoPai;
    private TipoSubdivisaoDTO tipoSubdivisao;
    private List<Feature> features; 
    
    public SubdivisaoDTO() {
    	super();
    }
    
	public SubdivisaoDTO(SubdivisaoCidade obj) {
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.cidade = obj.getCidade().getId();
		this.nomeCidade = obj.getCidade().getNome();
		this.uf = obj.getCidade().getProvinciaEstado().getSigla();
		this.nomeEstado = obj.getCidade().getProvinciaEstado().getNome();
	    this.tipoSubdivisao = obj.getTipoSubdivisao() != null ? new TipoSubdivisaoDTO(obj.getTipoSubdivisao()) : null;
	    this.subdivisaoPai = obj.getSubdivisaoPai() != null ? new SubdivisaoDTO(obj.getSubdivisaoPai()) : null;
	}
	
	public SubdivisaoDTO(Long id, Long cidade, String nome , SubdivisaoCidade s) {
		this.id = id;
		this.cidade = cidade;
		this.nome = nome;
		SubdivisaoDTO subdivisaoPaiDTO = new SubdivisaoDTO();
		if(s.getSubdivisaoPai() != null) {
			subdivisaoPaiDTO.setId(s.getSubdivisaoPai().getId());
			subdivisaoPaiDTO.setNome(s.getSubdivisaoPai().getNome());
		}
		this.subdivisaoPai = subdivisaoPaiDTO;
		
		TipoSubdivisaoDTO tipoSubdivisaoDTO = new TipoSubdivisaoDTO();
		if(s.getTipoSubdivisao() != null) {
			tipoSubdivisaoDTO.setId(s.getTipoSubdivisao().getId());
			tipoSubdivisaoDTO.setNome(s.getTipoSubdivisao().getNome());	
		}
		this.tipoSubdivisao = tipoSubdivisaoDTO;
		 
	}
	
	public SubdivisaoDTO(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}
	

	public SubdivisaoCidade toEntityInsert() {
		return new SubdivisaoCidade(null, null, nome, null, null);
	}
	
	public SubdivisaoCidade toEntityUpdate(SubdivisaoCidade subdivisao) {
		subdivisao.setId(this.id);
		subdivisao.setNome(this.nome);
		
		return subdivisao;
	} 
}
