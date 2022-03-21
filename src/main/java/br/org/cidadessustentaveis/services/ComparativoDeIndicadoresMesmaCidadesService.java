package br.org.cidadessustentaveis.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.ChartComparaIndicadorDTO;
import br.org.cidadessustentaveis.dto.ComparativoDeIndicadoresMesmaCidadeDTO;
import br.org.cidadessustentaveis.dto.LineChartDataDTO;
import br.org.cidadessustentaveis.dto.SerieHistoricaDTO;
import br.org.cidadessustentaveis.dto.TreeMapChartDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.model.indicadores.VariavelPreenchida;
import br.org.cidadessustentaveis.util.CalculadoraFormulaUtil;
import br.org.cidadessustentaveis.util.MandatoUtil;
import br.org.cidadessustentaveis.util.NumeroUtil;

@Service
public class ComparativoDeIndicadoresMesmaCidadesService {

	@Autowired
	private CidadeService cidadeService;
	@Autowired
	private IndicadorService indicadorService;
	@Autowired
	private IndicadorPreenchidoService indicadorPreenchidoService;
	@Autowired
	private CalculadoraFormulaUtil calculadoraFormulaUtil;
	
	public ComparativoDeIndicadoresMesmaCidadeDTO buscarComparativoDeIndicadoresMesmaCidade(Long idCidade, List<IndicadorPreenchido> listaindicadores) {

		int x = 0;
		Cidade cidade  = cidadeService.buscarPorId(idCidade);		
		
		ComparativoDeIndicadoresMesmaCidadeDTO dto;
		dto = ComparativoDeIndicadoresMesmaCidadeDTO.builder()
				.nomeCidade(cidade.getNome())
				.labels(null)
				.chartData(null)
				.serieHistorica(null)
				.treeMap(null)
				.build();
		
		List<String> labelGrafico = gerarAnosMandato(listaindicadores);			
		
		List<SerieHistoricaDTO> serieHistorica = gerarSerieHistorica(listaindicadores );
		dto.setSerieHistorica(serieHistorica);
		
		dto.setLabels(labelGrafico);
		
		List<ChartComparaIndicadorDTO> listaChartData = gerarChartComparaIndicador(listaindicadores );
		dto.setChartData(listaChartData);

		return dto;
		
	}
	
	
	private List<TreeMapChartDTO> gerarDadosTreeMap(List<IndicadorPreenchido> lista, Indicador indicador, String cidade) {
		List<TreeMapChartDTO> dtos = new ArrayList<>();
		if(indicador.isNumerico()) {
			for(IndicadorPreenchido ip : lista) {
				TreeMapChartDTO dto = new TreeMapChartDTO();
				Double valorIndicador =  Double.valueOf(ip.getResultado());
				dto.setValue(NumeroUtil.arredondarDuasCasasDecimais(valorIndicador));
				dto.setName(ip.getPrefeitura().getCidade().getNome()+" ("+ip.getAno()+")");
				dtos.add(dto);
			}
		} else {
			return null;
		}
		return dtos;
	}
	private LineChartDataDTO gerarDadosGrafico( List<IndicadorPreenchido> lista, String nomeCidade, List<String> anos, IndicadorPreenchido indicador) {
		
		//if(indicador.isNumerico()) {
			LineChartDataDTO dto = new LineChartDataDTO();
			dto.setLabel(indicador.getIndicador().getNome());
			dto.setValor(new ArrayList<>());
			for(String ano : anos) {
				if(!lista.stream().anyMatch(ip -> ano.equals(ip.getAno()+""))) {}
				else {
					for(IndicadorPreenchido ip : lista) {
						if(ano.equals(ip.getAno()+"")) {
							Double valorIndicador = ip.getResultadoReferencia() != null ? ip.getResultadoReferencia()  : Double.valueOf(ip.getResultado());
							dto.getValor().add(NumeroUtil.arredondarDuasCasasDecimais(valorIndicador));
							break;
						}
					}				
				}
	
			}
			return dto;
		//} else {
		//	return null;
		//}
	}

	private List<String> gerarAnosMandato( List<IndicadorPreenchido> lista) {
		List<String> listaRetorno = new ArrayList<>();
		List<Long> idPrefeituras = new ArrayList<>();

		lista.forEach(ip->{
			if(!idPrefeituras.contains(ip.getPrefeitura().getId())) {
				idPrefeituras.add(ip.getPrefeitura().getId());
				for(int ano = ip.getPrefeitura().getInicioMandato().getYear(); ano <= ip.getPrefeitura().getFimMandato().getYear() ; ano++) {
					listaRetorno.add(ano+"");
				}
			}
		});

		return listaRetorno;
	}

	private List<List<String>> gerarValoresSerieHistorica(Indicador indicador, List<IndicadorPreenchido> lista, List<String> cabecalho, int anoInicioMandato, int anoFimMandato, List<String> anos ) {
		
		List<List<String>> listaRetorno = new ArrayList<>();
		
		List<String> listaValores = new ArrayList<>();
		
		String[] linha2 = new String[cabecalho.size()];
		
		linha2[0] = indicador.getNome();

		int i = 1;
		for(String ano : anos) {
			if(!lista.stream().anyMatch(ip -> ano.equals(ip.getAno()+""))) {
				linha2[cabecalho.size()-1]=null;
			}else {
				for(IndicadorPreenchido ip : lista) {
					if(ano.equals(ip.getAno()+"") && ip.getIndicador().getId() == indicador.getId()) {
						String valorIndicador = ip.getResultadoApresentacao();
						linha2[i] = valorIndicador;
						break;
					}
				}
				i++;
			}
		}
		listaValores = Arrays.asList(linha2);
		listaRetorno.add(listaValores);
		
		return listaRetorno;
	}
	
	public List<String> gerarValoresSerieHistoricaAno(List<String> cabecalho, IndicadorPreenchido ip){
		
		List<String> listaValores = new ArrayList<>();
		String[] linha = new String[cabecalho.size()];
		linha[0] = ip.getAno()+"";
		
		for(VariavelPreenchida vp : ip.getVariaveisPreenchidas()) {
			int indice = cabecalho.indexOf(vp.getVariavel().getNome());
			linha[indice] = vp.getValor()+"";
		}
		
		linha[cabecalho.size()-1] =  ip.getResultadoReferencia() != null ? ip.getResultadoReferencia()+"" : ip.getResultado();
		listaValores = Arrays.asList(linha);
		return listaValores;
	}
	
	
	private List<List<String>> gerarValoresChartComparaIndicador(Indicador indicador, List<IndicadorPreenchido> lista, List<String> cabecalho, int anoInicioMandato, int anoFimMandato, List<String> anos ) {
		
		List<List<String>> listaRetorno = new ArrayList<>();
		
		List<String> listaValores = new ArrayList<>();
		
		String[] linha2 = new String[cabecalho.size()];
		
		linha2[0] = indicador.getNome();

		int i = 1;
		for(String ano : anos) {
			if(!lista.stream().anyMatch(ip -> ano.equals(ip.getAno()+""))) {
				linha2[cabecalho.size()-1]=null;
			}else {
				for(IndicadorPreenchido ip : lista) {
					if(ano.equals(ip.getAno()+"") && ip.getIndicador().getId() == indicador.getId()) {
						String valorIndicador = ip.getResultadoApresentacao();
						linha2[i] = valorIndicador;
						break;
					}
				}
				i++;
			}
		}
		listaValores = Arrays.asList(linha2);
		
		listaRetorno.add(listaValores);
		
		return listaRetorno;
	}



	private List<String> gerarCabecalho( List<String> anos){

		List<String> cabecalho = new ArrayList<>();
		cabecalho.add("Indicador");
		
		for(String label: anos) {
			cabecalho.add(label);
		}

		return cabecalho;
	}

	private List<SerieHistoricaDTO> gerarSerieHistorica(List<IndicadorPreenchido> listaIndicadorPreenchido) {
		
		List<SerieHistoricaDTO> serieHistoricaDTOs = new ArrayList<>();

		List<List<String>> mandatos = MandatoUtil.getMandatos();
		for(List<String> mandato : mandatos) {
			SerieHistoricaDTO dto = SerieHistoricaDTO.builder()
					.mandato(mandato.get(2))
					.inicioMandato(Integer.parseInt(mandato.get(0)))
					.fimMandato(Integer.parseInt(mandato.get(1)))
					.cabecalho(null)
					.valores(null).build();
			serieHistoricaDTOs.add(dto);
		}

		List<Long> indicadoresPreenchidos = new ArrayList<>();
		List<String> indicadoresPreenchidosString = new ArrayList<>();
		
		for(IndicadorPreenchido ip : listaIndicadorPreenchido) {
			
			for(SerieHistoricaDTO dto :serieHistoricaDTOs) {
				
				if(!indicadoresPreenchidos.contains( ip.getIndicador().getId()) &&
						ip.getAno().intValue()  >= dto.getInicioMandato() &&  ip.getAno().intValue() <= dto.getFimMandato() ) {
					List<String> anosMandatos = MandatoUtil.getAnosMandatos(dto.getInicioMandato(), dto.getFimMandato());
					List<String> cabecalho = gerarCabecalho( anosMandatos);
					List<List<String>> valores = gerarValoresSerieHistorica(ip.getIndicador(), listaIndicadorPreenchido, cabecalho, dto.getInicioMandato(), dto.getFimMandato(), anosMandatos);
					if(dto.getValores() == null) {
						dto.setValores(valores);
					} else {
						dto.getValores().addAll(valores);
					}
					if(dto.getCabecalho() == null) {
						dto.setCabecalho(cabecalho);
					}
					indicadoresPreenchidos.add(ip.getIndicador().getId());
					indicadoresPreenchidosString.add(ip.getIndicador().getNome());
				}
			
			}
		
			
		}

		List<String> indicadoresPreenchidosOrdenadas = new ArrayList<>();
		indicadoresPreenchidosString.stream().sorted(Comparator.naturalOrder()).forEach(cid -> indicadoresPreenchidosOrdenadas.add(cid));

		
		for(SerieHistoricaDTO dto : serieHistoricaDTOs) {
		  	 				
			for(int ano = dto.getInicioMandato(); ano<= dto.getFimMandato(); ano++) {
					
					if(dto.getCabecalho() == null) {
						List<String> anosMandatos = MandatoUtil.getAnosMandatos(dto.getInicioMandato(), dto.getFimMandato());
						List<String> cabecalho = gerarCabecalho( anosMandatos);
						dto.setCabecalho(cabecalho);
					}
					int indiceAno = dto.getCabecalho().indexOf(ano+"");
					
					for(String indicadorPreenchida: indicadoresPreenchidosOrdenadas ) {
						
						Boolean indicadorEncontrada = false;
						if(dto.getValores() == null) {
							dto.setValores(new ArrayList<>());
						}
						
						for(List<String> linha : dto.getValores()) {
							
							 if(linha.get(0).equals(indicadorPreenchida)) { // sigla estado
								 indicadorEncontrada =true;
								 if(linha.get(indiceAno) == null) {
									 linha.set(indiceAno, "-");
								 }
								 break;
							 }
						}
						if(!indicadorEncontrada) {
							String[] linha = new String[dto.getCabecalho().size()];
							linha[0] = indicadorPreenchida ;
							linha[indiceAno] = "-";
							dto.getValores().add(Arrays.asList(linha));
							
						}
						
					} 
					
				}
		}
		
		for(SerieHistoricaDTO dto : serieHistoricaDTOs ) {
			if(dto.getValores() != null) {
				dto.getValores().sort(new Comparator<List<String>>() {
					@Override
				    public int compare(List<String> m1, List<String> m2) {
						if(m1.get(0).equals(m2.get(0))){
				            return 0;
				        }if(null == m1.get(0)){
				            return 1;
				        }if(null == m2.get(0)){
				            return 1;
				        }
				        return m1.get(0).compareTo(m2.get(0));
					}
				});
			}
		}
		 
		return serieHistoricaDTOs;		
	}
	
	private List<ChartComparaIndicadorDTO> gerarChartComparaIndicador(List<IndicadorPreenchido> listaIndicadorPreenchido) {
		
		List<ChartComparaIndicadorDTO> chartComparaIndicadorDTO = new ArrayList<>();

		List<List<String>> mandatos = MandatoUtil.getMandatos();
		for(List<String> mandato : mandatos) {
			ChartComparaIndicadorDTO dto = ChartComparaIndicadorDTO.builder()
					.mandato(mandato.get(2))
					.inicioMandato(Integer.parseInt(mandato.get(0)))
					.fimMandato(Integer.parseInt(mandato.get(1)))
					.valores(null).build();
			chartComparaIndicadorDTO.add(dto);
		}

		List<Long> indicadoresPreenchidos = new ArrayList<>();
		List<String> indicadoresPreenchidosString = new ArrayList<>();
		
		for(IndicadorPreenchido ip : listaIndicadorPreenchido) {
			
			if(!ip.getIndicador().isNumerico()) {
				continue;
			}		
			
			for(ChartComparaIndicadorDTO dto :chartComparaIndicadorDTO) {
				
				if(!indicadoresPreenchidos.contains( ip.getIndicador().getId()) &&
						ip.getAno().intValue()  >= dto.getInicioMandato() &&  ip.getAno().intValue() <= dto.getFimMandato() ) {
					List<String> anosMandatos = MandatoUtil.getAnosMandatos(dto.getInicioMandato(), dto.getFimMandato());
					List<String> cabecalho = gerarCabecalho( anosMandatos);
					List<List<String>> valores = gerarValoresChartComparaIndicador(ip.getIndicador(), listaIndicadorPreenchido, cabecalho, dto.getInicioMandato(), dto.getFimMandato(), anosMandatos);
					
					// Conversão para saída LineChartDataDTO
					List<LineChartDataDTO> listaValores = new ArrayList<LineChartDataDTO>();
					LineChartDataDTO linhagrafico = new LineChartDataDTO();
					List<Double> montaValor = new ArrayList<Double>();
					for(List<String> valor :valores) {
					
						for(int i=0; i < valor.size(); i++) {
							if(i==0) {
								linhagrafico.setLabel(valor.get(0));
							} else {
								if((valor.get(i) != null) && (NumeroUtil.isANumber(valor.get(i).replace(',', '.')))) {
									montaValor.add(Double.parseDouble(valor.get(i).replace(',', '.')));
								}
								
							}
						}
						linhagrafico.setValor(montaValor);
						
						listaValores.add(linhagrafico);	
					}
					// fim			
					
					if(dto.getValores() == null) {
						dto.setValores(listaValores);
					} else {
						dto.getValores().addAll(listaValores);
					}
				
					indicadoresPreenchidos.add(ip.getIndicador().getId());
					indicadoresPreenchidosString.add(ip.getIndicador().getNome());
				}
			
			}
		
			
		}

		List<String> indicadoresPreenchidosOrdenadas = new ArrayList<>();
		indicadoresPreenchidosString.stream().sorted(Comparator.naturalOrder()).forEach(cid -> indicadoresPreenchidosOrdenadas.add(cid));
		
		/*
		for(ChartComparaIndicadorDTO dto : chartComparaIndicadorDTO ) {
			dto.getValores().sort(new Comparator<List<String>>() {
				@Override
			    public int compare(List<String> m1, List<String> m2) {
					if(m1.get(0).equals(m2.get(0))){
			            return 0;
			        }if(null == m1.get(0)){
			            return 1;
			        }if(null == m2.get(0)){
			            return 1;
			        }
			        return m1.get(0).compareTo(m2.get(0));
				}
			});
		}
		*/
		
		
		List<ChartComparaIndicadorDTO> listaparaRemover = new ArrayList<>();
		for(ChartComparaIndicadorDTO comparativoDTO : chartComparaIndicadorDTO) {
			if(comparativoDTO.getValores() == null || comparativoDTO.getValores().isEmpty()) {
				listaparaRemover.add(comparativoDTO);
			}
		}
		chartComparaIndicadorDTO.removeAll(listaparaRemover);		

		 
		return chartComparaIndicadorDTO;		
	}
}
