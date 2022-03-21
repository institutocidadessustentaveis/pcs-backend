package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.wololo.geojson.Feature;

import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.administracao.SubDivisao;
import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CidadeDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	@NotNull(message = "Obrigatório preenchimento do nome da cidade.")
	private String nome;

	private Long codigoIbge;
	
	private Long populacao;
	
	private Long anoDaPopulacao;
	
	private String enderecoDaPrefeitura;
	
	private Boolean isSignataria;
	
	private List<SubDivisao> subdivisoes;
	
	private ProvinciaEstado provinciaEstado;
	
	private String estadoNome;
	
	private String estadoSigla;

	private Double latitude;

	private Double longitude;
	
	private String site;
	private String area;
	private String densidadeDemografica;
	private String salarioMedioMensal;
	private String populacaoOcupada;
	private String pibPerCapita;
	private String idhM;
	private String textoCidade;
	private String fotoCidade;
	
	private Long idPrefeitura;
	
	private ArquivoDTO arquivoPlanoMetas;
	private ArquivoDTO arquivoRelatorioContas;
	
	//Ponto Focal
	private String nomeContato;
	private String telMovelContato;
	private String telFixoContato;
	private String emailContato;
	private String campoObservacao;
	
	private List<Feature> shapeZoneamento;
	
	public CidadeDTO(Cidade cidade) {
		this.id = cidade.getId();
		this.nome = cidade.getNome();
		this.codigoIbge = cidade.getCodigoIbge();
		this.provinciaEstado = cidade.getProvinciaEstado();
		this.populacao = cidade.getPopulacao();
		this.anoDaPopulacao = cidade.getAnoDaPopulacao();
		this.enderecoDaPrefeitura = cidade.getEnderecoDaPrefeitura();
		this.isSignataria = cidade.getIsSignataria();
		this.subdivisoes = cidade.getSubdivisoes();
	}
	
	public CidadeDTO (final Long id, final String nome) {
		this.id = id;
		this.nome = nome;		
	}
	
	public CidadeDTO (final Long id) {
		this.id = id;
	}
	
	public CidadeDTO (final Long id, final String nome, ProvinciaEstado provinciaEstado) {
		this.id = id;
		this.nome = nome;
		this.provinciaEstado = provinciaEstado != null ? provinciaEstado : null;
	}

	public CidadeDTO (final Long id, final String nome, final Long idPrefeitura) {
		this.id = id;
		this.nome = nome;
		this.idPrefeitura = idPrefeitura;
	}
	
	public CidadeDTO(Long codigoIbge, String estadoNome, String estadoSigla) {
		this.codigoIbge = codigoIbge;
		this.estadoNome = estadoNome;
		this.estadoSigla = estadoSigla;
	}
	
	public Cidade toEntityInsert() {
		Cidade cidade = new Cidade(null, this.nome, this.codigoIbge, this.populacao, this.anoDaPopulacao, this.enderecoDaPrefeitura, false, this.provinciaEstado, this.subdivisoes, this.area,
			    this.densidadeDemografica, this.salarioMedioMensal, this.populacaoOcupada, this.pibPerCapita, this.idhM, this.nomeContato, this.telFixoContato, this.telMovelContato, this.emailContato, this.campoObservacao);
		cidade.setLatitude(this.latitude);
		cidade.setLongitude(this.longitude);
		return cidade;
	}
	
	public Cidade toEntityUpdate(Cidade cidade) {
	  cidade.setNome(this.nome);
	  cidade.setCodigoIbge(this.codigoIbge);
	  cidade.setPopulacao(this.populacao);
	  cidade.setAnoDaPopulacao(this.anoDaPopulacao);
	  cidade.setEnderecoDaPrefeitura(this.enderecoDaPrefeitura);
	  cidade.setIsSignataria(this.isSignataria);
	  cidade.setProvinciaEstado(this.provinciaEstado);
	  cidade.setSubdivisoes(this.subdivisoes);
	  cidade.setLatitude(this.latitude);
	  cidade.setLongitude(this.longitude);
	  cidade.setSitePrefeitura(this.site);
	  cidade.setArea(area);
	  cidade.setDensidadeDemografica(densidadeDemografica);
	  cidade.setSalarioMedioMensal(salarioMedioMensal);
	  cidade.setPopulacaoOcupada(populacaoOcupada);
	  cidade.setPibPerCapita(pibPerCapita);
	  cidade.setIdhM(idhM);
	  cidade.setNomeContato(this.nomeContato);
	  cidade.setTelFixoContato(this.telFixoContato);
	  cidade.setTelMovelContato(this.telMovelContato);
	  cidade.setEmailContato(this.emailContato);
	  cidade.setCampoObservacao(this.campoObservacao);
	  return cidade;
	}
	
	public Cidade toEntityUpdateViaPrefeitura(Cidade cidade) {
		  cidade.setPopulacao(this.populacao);
		  cidade.setAnoDaPopulacao(this.anoDaPopulacao);
		  cidade.setEnderecoDaPrefeitura(this.enderecoDaPrefeitura);
		  cidade.setSubdivisoes(this.subdivisoes);
		  cidade.setLatitude(this.latitude);
		  cidade.setLongitude(this.longitude);
		  cidade.setSitePrefeitura(this.site);
		  cidade.setArea(area);
		  cidade.setDensidadeDemografica(densidadeDemografica);
		  cidade.setSalarioMedioMensal(salarioMedioMensal);
		  cidade.setPopulacaoOcupada(populacaoOcupada);
		  cidade.setPibPerCapita(pibPerCapita);
		  cidade.setIdhM(idhM);	  
		  cidade.setNomeContato(this.nomeContato);
		  cidade.setEmailContato(this.emailContato);
		  cidade.setTelMovelContato(this.telMovelContato);
		  cidade.setTelFixoContato(this.telFixoContato);
		  cidade.setCampoObservacao(this.campoObservacao);
		  return cidade;
	}
	
	public CidadeDTO(BoaPratica boaPratica) {
		if(boaPratica.getMunicipio() != null && boaPratica.getMunicipio() != null) {
			this.id = boaPratica.getMunicipio().getId();
			this.nome = boaPratica.getMunicipio().getNome();
			this.codigoIbge = boaPratica.getMunicipio().getCodigoIbge();
			this.enderecoDaPrefeitura = boaPratica.getMunicipio().getEnderecoDaPrefeitura();
			this.isSignataria = boaPratica.getMunicipio().getIsSignataria();
			this.latitude = boaPratica.getMunicipio().getLatitude();
			this.longitude = boaPratica.getMunicipio().getLongitude();
		}
	}
	
	public CidadeDTO(Long id, String nome, Long codigoIbge, ProvinciaEstado provinciaEstado, Long populacao ) {
		this.id = id;
		this.nome = nome;
		this.codigoIbge = codigoIbge;
		this.estadoNome = provinciaEstado.getNome();
		this.populacao = populacao;
	}

}
