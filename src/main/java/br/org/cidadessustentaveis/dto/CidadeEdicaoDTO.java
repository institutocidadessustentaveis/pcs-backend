package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.SubDivisao;
import lombok.*;

import java.io.Serializable;
import java.util.List;

import org.wololo.geojson.Feature;

@Data
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CidadeEdicaoDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String nome;

	private Double latitude;

	private Double longitude;

	private Long codigoIbge;

	private Long populacao;

	private Long anoDaPopulacao;

	private String enderecoDaPrefeitura;

	private Boolean isSignataria;

	private List<SubDivisao> subdivisoes;

	private CidadeProvinciaEstadoDTO provinciaEstado;

	private String siglaEstado;

	private String site;
	private String area;
	private String densidadeDemografica;
	private String salarioMedioMensal;
	private String populacaoOcupada;
	private String pibPerCapita;
	private String idhM;
	private String textoCidade;
	private String fotoCidade;
	
	private List<Feature> shapeZoneamento;
	
	//Ponto focal
	private String nomeContato;
	private String telFixoContato;
	private String telMovelContato;
	private String emailContato;
	private String campoObservacao;

	public CidadeEdicaoDTO(Cidade cidade) {

		this.id = cidade.getId();
		this.nome = cidade.getNome();
		this.codigoIbge = cidade.getCodigoIbge();

		if(cidade.getProvinciaEstado() != null) {
			this.provinciaEstado = new CidadeProvinciaEstadoDTO(cidade.getProvinciaEstado());
		}

		this.populacao = cidade.getPopulacao();
		this.anoDaPopulacao = cidade.getAnoDaPopulacao();
		this.enderecoDaPrefeitura = cidade.getEnderecoDaPrefeitura();
		this.isSignataria = cidade.getIsSignataria();
		this.subdivisoes = cidade.getSubdivisoes();
		this.latitude = cidade.getLatitude();
		this.longitude =  cidade.getLongitude();
		this.site = cidade.getSitePrefeitura();
		this.area = cidade.getArea();
		this.densidadeDemografica = cidade.getDensidadeDemografica();
		this.salarioMedioMensal = cidade.getSalarioMedioMensal();
		this.populacaoOcupada = cidade.getPopulacaoOcupada();
		this.pibPerCapita = cidade.getPibPerCapita();
		this.idhM = cidade.getIdhM();

		if(cidade.getImagemCidade() != null) {
			this.fotoCidade = cidade.getImagemCidade().toBase64();
		}

		this.textoCidade = cidade.getTextoCidadeDefault();
		this.siglaEstado = cidade.getProvinciaEstado().getSigla();
		this.nomeContato = cidade.getNomeContato();
		this.telFixoContato = cidade.getTelFixoContato();
		this.telMovelContato = cidade.getTelMovelContato();
		this.emailContato = cidade.getEmailContato();
		this.campoObservacao = cidade.getCampoObservacao();
	}

	public CidadeEdicaoDTO(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}

}
