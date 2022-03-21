package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ValorPorCidadeDTO implements Serializable{

	private static final long serialVersionUID = -2522519978361722589L;
	
	private Long idCidade;
	
	private String cidade;
	
	private Double latitude;
	
	private Double longitude;
	
	private Short anoPreenchimento;
	
	private String resultado;
	
	private Double resultadoReferencia;
	
	private String resultadoApresentacao;
	
	private String indicadorMultiplo = "N";
	
	public ValorPorCidadeDTO(Long idCidade, String siglaEstado, String nomeCidade, Double latitude, Double longitude,
			Short anoPreenchimento, String resultado, Double resultadoReferencia, String resultadoApresentacao) {
		
		setIdCidade(idCidade);
		setCidade(nomeCidade + " - " + siglaEstado);
		setLatitude(latitude);
		setLongitude(longitude);
		setAnoPreenchimento(anoPreenchimento);
		setResultado(formataResultado(resultado));
		setResultadoReferencia(resultadoReferencia);
		setResultadoApresentacao(resultadoApresentacao);
	}

	private String formataResultado(String texto) {
		if(texto != null && texto.contains("[")) {
			setIndicadorMultiplo("S");
			return "@";
		}
		else {
			if(texto != null && texto != "") {
				String resultado = texto.replace('.', ',');
				String array[] = new String[2];
				array = resultado.split(",");
				String formatado = "";
				
				try {
					if(array[1].length() > 1) {
						formatado = array[0] + "," + array[1].substring(0,2);
					}
					else {
						formatado = array[0] + "," + array[1].substring(0,1);
					}
					return formatado;
				} catch(Exception e) {
					System.out.println("Não foi possível converter o valor");
					e.printStackTrace();
				}
				
			}
			return "-";
		}
	}
}
