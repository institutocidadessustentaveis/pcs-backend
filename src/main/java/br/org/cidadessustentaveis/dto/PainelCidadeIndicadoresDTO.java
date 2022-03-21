package br.org.cidadessustentaveis.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.model.indicadores.ValorReferencia;
import br.org.cidadessustentaveis.model.indicadores.Variavel;
import br.org.cidadessustentaveis.model.indicadores.VariavelPreenchida;
import br.org.cidadessustentaveis.util.VariavelPreenchidaUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class PainelCidadeIndicadoresDTO {

	private String cidade;
	
	private String estado;
	
	private String uf;
	
	private String pais;
	
	private Long populacao;
	
	private List<Mandato2DTO> mandatos;

	private Long anoDaPopulacao;

	private String enderecoDaPrefeitura;

	private Long codigoIbge;
	
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
	
	private Long idRelatorioContas;
	private Long idPlanoMetas;
	
	private boolean possuiIndicadoresCadastrados;
	
	public PainelCidadeIndicadoresDTO(Cidade cidade,
									  Map<Prefeitura, Map<Indicador, List<IndicadorPreenchido>>> indicadoresPorPrefeitura,
									  Map<IndicadorPreenchido, ValorReferencia> valores) {
		this.cidade = cidade.getNome();
		this.estado = cidade.getProvinciaEstado().getNome();
		this.uf = cidade.getProvinciaEstado().getSigla();
		this.pais = cidade.getProvinciaEstado().getPais().getNome();
		this.populacao = cidade.getPopulacao();
		this.anoDaPopulacao = cidade.getAnoDaPopulacao();
		this.enderecoDaPrefeitura = cidade.getEnderecoDaPrefeitura();
		this.codigoIbge = cidade.getCodigoIbge();
		this.longitude = cidade.getLongitude();
		this.latitude = cidade.getLatitude();
		this.site = cidade.getSitePrefeitura();
		this.area = cidade.getArea(); 
		this.densidadeDemografica = cidade.getDensidadeDemografica();
		this.salarioMedioMensal = cidade.getSalarioMedioMensal();
		this.populacaoOcupada = cidade.getPopulacaoOcupada();
		this.pibPerCapita = cidade.getPibPerCapita();
		this.idhM = cidade.getIdhM();
		this.textoCidade = cidade.getTextoCidade() != null ?  cidade.getTextoCidade() :  cidade.getTextoCidadeDefault();

		if(cidade.getImagemCidade() != null) {
			this.fotoCidade = "imagens/" + cidade.getImagemCidade().getId();
		}
		
		if(cidade.getPlanoMetas() != null) {
			this.idPlanoMetas = cidade.getPlanoMetas().getId();
		}
		
		if(cidade.getRelatorioContas() != null) {
			this.idRelatorioContas = cidade.getRelatorioContas().getId();
		}
		
		
		this.mandatos = new ArrayList<>();
	}
}

@Getter @Setter @AllArgsConstructor
class MandatoDTO {

	private Integer inicioMandato;
	
	private Integer fimMandato;
	
	private TreeSet<IndicadoresEixoDTO> eixos;
	
	public MandatoDTO(int inicioMandato, int fimMandato, Map<Indicador, List<IndicadorPreenchido>> indicadores, Map<IndicadorPreenchido, ValorReferencia> valores) {
		this.inicioMandato = inicioMandato;
		this.fimMandato = fimMandato;

		SortedMap<String, SortedSet<IndicadorDetalheDTO>> mapIndicadores = new TreeMap<String, SortedSet<IndicadorDetalheDTO>>();
		
		Map<String, List<String>> cabelhacos = new HashMap<>();
		
		for (Indicador indicador : indicadores.keySet()) {			
			String eixo = indicador.getPrefeitura() != null ? "Indicadores da Cidade" : indicador.getEixo().getNome();
			if ( null == mapIndicadores.get(eixo) )
				mapIndicadores.put(eixo, new TreeSet<>());
			
			Map<Short, IndicadorPreenchido> preenchidos = indicadores.get(indicador).stream()
					.collect(Collectors.toMap(IndicadorPreenchido::getAno, preenchido -> preenchido));
			
			//Verifica se o indicador e unico ou multiplo
			String tipoIndicador = (indicador.getFormulaReferencia() == null || indicador.getFormulaReferencia().isEmpty()? "Único": "Multiplo");
			IndicadorDetalheDTO indicadorDetalhe = new IndicadorDetalheDTO();
			indicadorDetalhe.setId(indicador.getId());
			indicadorDetalhe.setNome(indicador.getNome());
			indicadorDetalhe.setTipo(tipoIndicador); 
			indicadorDetalhe.setAnos(new HashMap<>());
			indicadorDetalhe.setVariaveis(new ArrayList<>());
			indicadorDetalhe.setNumerico(indicador.isNumerico());
			
			cabelhacos.put(eixo, new LinkedList<>());
			cabelhacos.get(eixo).add("Nome");
			for (Short ano = (short) inicioMandato; ano <= fimMandato; ano++) {
				cabelhacos.get(eixo).add(String.valueOf(ano));
				IndicadorPreenchido preenchido = preenchidos.get(ano);
				String cor = null != preenchido ? valores.get(preenchido).getCor() : "#D3D3D3";
				indicadorDetalhe.getAnos().put(String.valueOf(ano), new ValorIndicadorDTO(getValorIndicador(preenchido), cor));
				
				getVariaveisPreenchidas(indicadorDetalhe.getVariaveis(), String.valueOf(ano), indicador, preenchido);
			}
			mapIndicadores.get(eixo).add(indicadorDetalhe);
			cabelhacos.get(eixo).add("Resultado");
			cabelhacos.get(eixo).add("");
		}
		
		this.eixos = new TreeSet<>();
		for (String eixo: mapIndicadores.keySet()) {
			eixos.add(new IndicadoresEixoDTO(eixo, cabelhacos.get(eixo), mapIndicadores.get(eixo)));
		}
	}
	
	private List<ValorVariavelDTO> getVariaveisPreenchidas(List<ValorVariavelDTO> variaveis, String ano, Indicador indicador, IndicadorPreenchido preenchido) {
		Map<Variavel, VariavelPreenchida> preenchidas = null;
		if ( null != preenchido ) {
			preenchidas = preenchido.getVariaveisPreenchidas().stream().collect(Collectors.toMap(VariavelPreenchida::getVariavel, preenchida -> preenchida));
		}
		for (Variavel variavel : indicador.getVariaveis()) {
			VariavelPreenchida preenchida = null != preenchidas ? preenchidas.get(variavel) : null ;
			String valor = VariavelPreenchidaUtil.valorApresentacao(preenchida);

			Optional<ValorVariavelDTO> encontrada = variaveis.stream().filter(v -> v.getNome().equals(variavel.getNome())).findFirst();
			if (encontrada.isPresent())
				encontrada.get().getAnos().put(ano, new ValorIndicadorDTO(valor, null));
			else {
				ValorVariavelDTO dto = new ValorVariavelDTO(variavel.getNome(), variavel.getTipo(), new HashMap<>());
				dto.getAnos().put(ano, new ValorIndicadorDTO(valor, null));
				variaveis.add(dto);
			}
		}
		return variaveis;
	}

	private String getValorIndicador(IndicadorPreenchido preenchido) {
		if ( null == preenchido )
			return "-";
		else
			if(preenchido.getIndicador().isNumerico()) {
				return preenchido.getResultadoApresentacao();
			}
			return null != preenchido.getResultadoReferencia() ? preenchido.getResultadoReferencia().toString() : null != preenchido.getResultado() ? preenchido.getResultado() : "-";
	}
}

@Getter @Setter @NoArgsConstructor
class IndicadoresEixoDTO implements Comparable<IndicadoresEixoDTO> {
	
	private String nome;
	
	private List<String> cabecalho;
	
	private SortedSet<IndicadorDetalheDTO> indicadores;
	
	public IndicadoresEixoDTO(String eixo, List<String> cabecalho, SortedSet<IndicadorDetalheDTO>  indicadores) {
		this.nome = eixo;
		this.cabecalho = cabecalho;
		this.indicadores = indicadores;
	}
	
    public int compareTo(IndicadoresEixoDTO other) {
   	 return this.nome.compareTo(other.nome);
   }
}

@Getter @Setter @NoArgsConstructor
class IndicadorDetalheDTO  implements Comparable<IndicadorDetalheDTO>{
	
	private Long id;

	private String nome;
	
	private String tipo;
	
	private Map<String, ValorIndicadorDTO> anos;
	
	private List<ValorVariavelDTO> variaveis;
	

	private boolean numerico;


    public int compareTo(IndicadorDetalheDTO other) {
    	 return this.nome.compareTo(other.nome);
    }
}

@Getter @Setter @AllArgsConstructor
class ValorIndicadorDTO {
	
	private String valor;
	
	private String cor;
}

@Getter @Setter @AllArgsConstructor
class ValorVariavelDTO {
	
	private String nome;
	
	private String tipo;
	
	private Map<String, ValorIndicadorDTO> anos;
}
