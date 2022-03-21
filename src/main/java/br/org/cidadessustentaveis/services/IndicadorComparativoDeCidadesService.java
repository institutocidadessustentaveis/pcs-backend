package br.org.cidadessustentaveis.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.CidadeDTO;
import br.org.cidadessustentaveis.dto.ComparativoGraficoDTO;
import br.org.cidadessustentaveis.dto.DadosDispersaoAnimadaDTO;
import br.org.cidadessustentaveis.dto.IndicadorComparativoDeCidadeDTO;
import br.org.cidadessustentaveis.dto.LineChartDataDTO;
import br.org.cidadessustentaveis.dto.SerieHistoricaDTO;
import br.org.cidadessustentaveis.dto.TreeMapAnoDTO;
import br.org.cidadessustentaveis.dto.TreeMapChartDTO;
import br.org.cidadessustentaveis.model.administracao.InstituicaoFonte;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.util.CalculadoraFormulaUtil;
import br.org.cidadessustentaveis.util.MandatoUtil;
import br.org.cidadessustentaveis.util.NumeroUtil;

@Service
public class IndicadorComparativoDeCidadesService {

	@Autowired
	private IndicadorService indicadorService;
	@Autowired
	private IndicadorPreenchidoService indicadorPreenchidoService;
	@Autowired
	private CalculadoraFormulaUtil calculadoraFormulaUtil;
	@Autowired
	private VariavelPreenchidaService variavelPreenchidaService;

	public IndicadorComparativoDeCidadeDTO buscarIndicadorComparativoDeCidades(Long idIndicador, List<CidadeDTO> cidades, Integer formulaidx) {
		
		if (cidades == null || cidades.isEmpty()) {
			cidades = indicadorPreenchidoService.buscarCidadesQuePreencheram(idIndicador);
		}

		Indicador indicador  = indicadorService.listarById(idIndicador);
		List<InstituicaoFonte> listaFontes = variavelPreenchidaService.buscaVariavelPreenchidaPorIdIndicador(idIndicador);
		IndicadorComparativoDeCidadeDTO dto;
		dto = IndicadorComparativoDeCidadeDTO.builder()
				.indicador(null)
				.descricao(null)
				.formula(null)
				.fontes(null)
				.meta(null)
				.ods(null)
				.odsNumero(null)
				.labels(null)
				.chartData(null)
				.serieHistorica(null)
				.treeMap(null)
				.numerico(indicador.isNumerico())
				.build();
		List<IndicadorPreenchido> listaIndicadoresPreenchidos = new ArrayList<>();

		for(CidadeDTO cidadeDTO : cidades) {
			List<IndicadorPreenchido> lista = indicadorPreenchidoService.buscarPorIndicadorCidade(indicador, cidadeDTO.getId());
			listaIndicadoresPreenchidos.addAll(lista);	
		}

		StringBuilder sb = new StringBuilder();
		for (InstituicaoFonte fontes : listaFontes) {
			sb.append("<li>");
			sb.append(". ");
			sb.append(fontes.getNome());
			sb.append("</li>");
		}

		calculadoraFormulaUtil.formatarFormula(indicador);
		String meta = indicador.getMetaODS() != null ? "Meta "+indicador.getMetaODS().getNumero()+": "+indicador.getMetaODS().getDescricao() : "Meta não vinculada";
		String odsTitulo = indicador.getMetaODS() != null ? indicador.getOds().getTitulo() : null;
		Integer numero = ((indicador.getOds() != null) && (indicador.getOds().getNumero() != null)) ? indicador.getOds().getNumero() : null;
		dto = IndicadorComparativoDeCidadeDTO.builder()
				.indicador(indicador.getNome())
				.descricao(indicador.getDescricao())
				.formula(indicador.getFormulaResultado())
				.fontes(listaFontes)
				.meta("Meta "+meta)
				.ods(odsTitulo)
				.odsNumero(numero)
				.numerico(dto.getNumerico())
				.build();
		List<ComparativoGraficoDTO> graficos = gerarGraficos(listaIndicadoresPreenchidos,indicador,formulaidx);	
		gerarDadosDeDispersao(listaIndicadoresPreenchidos,dto,indicador);		
		dto.setGraficos(graficos);
		return dto;
	}

	private List<ComparativoGraficoDTO> gerarGraficos(List<IndicadorPreenchido> listaIndicadorPreenchido, Indicador indicador,Integer formulaidx) {
		if(!indicador.isNumerico()) {
			return null;
		}
		List<ComparativoGraficoDTO> comparativoGraficoDTO = new ArrayList<>();
		List<List<String>> mandatos = MandatoUtil.getMandatos();
		for(List<String> mandato : mandatos) {
			List<String> anosMandatos = MandatoUtil.getAnosMandatos(Integer.parseInt(mandato.get(0)), Integer.parseInt(mandato.get(1)));
			ComparativoGraficoDTO dto = ComparativoGraficoDTO.builder()
					.mandato(mandato.get(2))
					.inicioMandato(Integer.parseInt(mandato.get(0)))
					.fimMandato(Integer.parseInt(mandato.get(1)))
					.labels(anosMandatos)
					.treeMap(new ArrayList<>())
					.build();
			comparativoGraficoDTO.add(dto);
		}

		for(ComparativoGraficoDTO comparativoDTO : comparativoGraficoDTO) {
			if(comparativoDTO.getValores() == null) {
				comparativoDTO.setValores(new ArrayList<>(comparativoDTO.getLabels().size()));
			}
			for(String ano : comparativoDTO.getLabels()) {
				for(IndicadorPreenchido ip : listaIndicadorPreenchido) {
					if(ip.getResultado() != null && (ip.getResultado().contains("NaN") || ip.getResultado().contains("Infinity"))) {
						continue;
					}
					String nomeCidade = ip.getPrefeitura().getCidade().getNome()+" - "+ip.getPrefeitura().getCidade().getProvinciaEstado().getSigla();
					if(ano.equals(ip.getAno()+"")) {
						int indexAno = comparativoDTO.getLabels().indexOf(ip.getAno()+"");
						boolean encontrado = false;

						for(LineChartDataDTO dto : comparativoDTO.getValores()) {
							if(dto.getLabel().equals(nomeCidade)) {
								List<Double> valores = dto.getValor();
								if(ip.getResultado() != null && ip.getResultado() != "" && NumeroUtil.isANumber(ip.getResultado())) {
									valores.set(indexAno, NumeroUtil.arredondarDuasCasasDecimais(Double.parseDouble(ip.getResultado())));
									adicionarValorTreeMap(comparativoDTO, ip, nomeCidade);									
								}else if(ip.getResultado() != null){
							
									String resultado = ip.getResultado().replace("[", "");
									resultado =  resultado.replace("]", "");
									
									String[] tempArray;
									String delimiter = "\\,";
							        tempArray = resultado.split(delimiter);
							        
							        Double[] tempValores = new Double[tempArray.length];
							        for (int i = 0; i < tempArray.length; i++) {
							            tempValores[i] = NumeroUtil.arredondarDuasCasasDecimais(Double.parseDouble(tempArray[i]));
							        }
							
									if(formulaidx != null) {
										valores.set(indexAno,tempValores[formulaidx]);
										 
										TreeMapAnoDTO treeMapAno = new TreeMapAnoDTO();
										treeMapAno.setAno(ip.getAno());
										treeMapAno.setValores( new ArrayList<>());
										treeMapAno.getValores().add(new TreeMapChartDTO(nomeCidade, tempValores[formulaidx]));
										comparativoDTO.getTreeMap().add(treeMapAno);
									}				

								}

								encontrado = true;
								break;
							}
						}
						if(!encontrado) {
							LineChartDataDTO dto = new LineChartDataDTO();
							dto.setLabel(nomeCidade);

							Double[] valores = new Double[comparativoDTO.getLabels().size()];
							if(ip.getResultado() != null &&  ip.getResultado() != "" && NumeroUtil.isANumber(ip.getResultado())) {
								valores[indexAno] = NumeroUtil.arredondarDuasCasasDecimais(Double.parseDouble(ip.getResultado()));
								dto.setValor(Arrays.asList(valores));
								comparativoDTO.getValores().add(dto);	
								adicionarValorTreeMap(comparativoDTO, ip, nomeCidade);	
							}else if(ip.getResultado() != null){
								
								String resultado = ip.getResultado().replace("[", "");
								resultado =  resultado.replace("]", "");
								
								String[] tempArray;
								String delimiter = "\\,";
						        tempArray = resultado.split(delimiter);
						        
						        Double[] tempValores = new Double[tempArray.length];
						        for (int i = 0; i < tempArray.length; i++) {
						            tempValores[i] = NumeroUtil.arredondarDuasCasasDecimais(Double.parseDouble(tempArray[i]));
						        }
						        
								if(formulaidx != null) {
									valores[indexAno] = NumeroUtil.arredondarDuasCasasDecimais(tempValores[formulaidx]);
									dto.setValor(Arrays.asList(valores));
									comparativoDTO.getValores().add(dto);

									TreeMapAnoDTO treeMapAno = new TreeMapAnoDTO();
									treeMapAno.setAno(ip.getAno());
									treeMapAno.setValores( new ArrayList<>());
									treeMapAno.getValores().add(new TreeMapChartDTO(nomeCidade, tempValores[formulaidx]));
									comparativoDTO.getTreeMap().add(treeMapAno);	
								 }
							}

						}
					}
				}
			}
		}

		List<ComparativoGraficoDTO> listaparaRemover = new ArrayList<>();
		for(ComparativoGraficoDTO comparativoDTO : comparativoGraficoDTO) {
			if(comparativoDTO.getValores() == null || comparativoDTO.getValores().isEmpty()) {
				listaparaRemover.add(comparativoDTO);
			}
		}
		comparativoGraficoDTO.removeAll(listaparaRemover);
		comparativoGraficoDTO.forEach(dto -> {
			dto.setTreeMap(dto.getTreeMap().stream().sorted(Comparator.comparing(TreeMapAnoDTO::getAno)).collect(Collectors.toList()));
			dto.getTreeMap().forEach(treeMapAno -> {
				treeMapAno.setValores(treeMapAno.getValores().stream().sorted(Comparator.comparing(TreeMapChartDTO::getValue).reversed()).collect(Collectors.toList()));
			});
		});

		return comparativoGraficoDTO;
	}

	public void adicionarValorTreeMap(ComparativoGraficoDTO dto, IndicadorPreenchido ip, String nomeCidade) {
		Boolean anoEncontrado = false;
		for(TreeMapAnoDTO treeMapAno: dto.getTreeMap()) {
			if(treeMapAno.getAno().equals(ip.getAno())) {
				treeMapAno.getValores().add(new TreeMapChartDTO(nomeCidade, NumeroUtil.arredondarDuasCasasDecimais(Double.parseDouble(ip.getResultado()))));
				anoEncontrado = true;
				break;
			}
		}
		if(!anoEncontrado) {
			TreeMapAnoDTO treeMapAno = new TreeMapAnoDTO();
			treeMapAno.setAno(ip.getAno());
			treeMapAno.setValores( new ArrayList<>());
			treeMapAno.getValores().add(new TreeMapChartDTO(nomeCidade, NumeroUtil.arredondarDuasCasasDecimais(Double.parseDouble(ip.getResultado()))));
			dto.getTreeMap().add(treeMapAno);
		}


	}

	private List<List<String>> gerarValoresSerieHistorica(Indicador indicador, List<IndicadorPreenchido> lista, List<String> cabecalho, int anoInicioMandato, int anoFimMandato, List<String> anos, String nomeCidade ) {

		List<List<String>> listaRetorno = new ArrayList<>();

		List<String> listaValores = new ArrayList<>();

		String[] linha2 = new String[cabecalho.size()];

		linha2[0] = nomeCidade;

		int i = 1;
		for(String ano : anos) {
			if(!lista.stream().anyMatch(ip -> ano.equals(ip.getAno()+""))) {
				linha2[cabecalho.size()-1]=null;
			}else {
				for(IndicadorPreenchido ip : lista) {
					if(ano.equals(ip.getAno()+"") && (ip.getPrefeitura().getCidade().getNome()+"-"+ip.getPrefeitura().getCidade().getProvinciaEstado().getSigla()).equals(nomeCidade)) {
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
		cabecalho.add("Cidade");

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

		List<Long> cidadesPreenchidas = new ArrayList<>();
		List<String> cidadesPreenchidasString = new ArrayList<>();
		for(IndicadorPreenchido ip : listaIndicadorPreenchido) {
			for(SerieHistoricaDTO dto :serieHistoricaDTOs) {
				if(!cidadesPreenchidas.contains( ip.getPrefeitura().getCidade().getId()) &&
						ip.getAno().intValue()  >= dto.getInicioMandato() &&  ip.getAno().intValue() <= dto.getFimMandato() ) {
					List<String> anosMandatos = MandatoUtil.getAnosMandatos(dto.getInicioMandato(), dto.getFimMandato());
					List<String> cabecalho = gerarCabecalho( anosMandatos);
					List<List<String>> valores = gerarValoresSerieHistorica(ip.getIndicador(), listaIndicadorPreenchido, cabecalho, dto.getInicioMandato(), dto.getFimMandato(), anosMandatos, ip.getPrefeitura().getCidade().getNome()+"-"+ip.getPrefeitura().getCidade().getProvinciaEstado().getSigla());
					if(dto.getValores() == null) {
						dto.setValores(valores);
					} else {
						dto.getValores().addAll(valores);
					}
					if(dto.getCabecalho() == null) {
						dto.setCabecalho(cabecalho);
					}
					cidadesPreenchidas.add(ip.getPrefeitura().getCidade().getId());
					cidadesPreenchidasString.add(ip.getPrefeitura().getCidade().getNome()+"-"+ip.getPrefeitura().getCidade().getProvinciaEstado().getSigla());
				}
			}
		}
		List<String> cidadesPreenchidasOrdenadas = new ArrayList<>();
		cidadesPreenchidasString.stream().sorted(Comparator.naturalOrder()).forEach(cid -> cidadesPreenchidasOrdenadas.add(cid));
		for(SerieHistoricaDTO dto : serieHistoricaDTOs) {
			for(int ano = dto.getInicioMandato(); ano<= dto.getFimMandato(); ano++) {
				if(dto.getCabecalho() == null) {
					List<String> anosMandatos = MandatoUtil.getAnosMandatos(dto.getInicioMandato(), dto.getFimMandato());
					List<String> cabecalho = gerarCabecalho( anosMandatos);
					dto.setCabecalho(cabecalho);
				}
				int indiceAno = dto.getCabecalho().indexOf(ano+"");
				for(String cidadePreenchida: cidadesPreenchidasOrdenadas ) {
					Boolean cidadeEncontrada = false;
					if(dto.getValores() == null) {
						dto.setValores(new ArrayList<>());
					}
					for(List<String> linha : dto.getValores()) {
						if(linha.get(0).equals(cidadePreenchida)) {
							cidadeEncontrada =true;
							if(linha.get(indiceAno) == null) {
								linha.set(indiceAno, "-");
							}
							break;
						}
					}
					if(!cidadeEncontrada) {
						String[] linha = new String[dto.getCabecalho().size()];
						linha[0] = cidadePreenchida;
						linha[indiceAno] = "-";
						dto.getValores().add(Arrays.asList(linha));
					}

				}
			}
		}
		for(SerieHistoricaDTO dto : serieHistoricaDTOs ) {
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
		return serieHistoricaDTOs;		
	}

	private void gerarDadosDeDispersao(List<IndicadorPreenchido> listaIndicadorPreenchido, IndicadorComparativoDeCidadeDTO indicadorComparativoDeCidadeDTO, Indicador indicador) {
		if(indicador.isNumerico()) {
			List<DadosDispersaoAnimadaDTO> dispersaoAnimada = new ArrayList<>();
			List<String> labelDispersaoAnimada = new ArrayList<>();
			List<String> cidadesDispersaoAnimada = new ArrayList<>();
			for(IndicadorPreenchido ip : listaIndicadorPreenchido) {
				if(ip.getResultado() == null || !NumeroUtil.isANumber(ip.getResultado())) {
					ip.setResultado("");
				}
				String nomeCidade = ip.getPrefeitura().getCidade().getNome()+" - "+ip.getPrefeitura().getCidade().getProvinciaEstado().getSigla();
				Long idCidade = ip.getPrefeitura().getCidade().getId();
				Long populacaoCidade = ip.getPrefeitura().getCidade().getPopulacao();
				if(ip.getResultado() != "" && NumeroUtil.isANumber(ip.getResultado())) {
					DadosDispersaoAnimadaDTO dto = new DadosDispersaoAnimadaDTO(ip.getAno(),nomeCidade, NumeroUtil.arredondarDuasCasasDecimais(Double.parseDouble(ip.getResultado())),idCidade,populacaoCidade);
					dispersaoAnimada.add(dto);
				}

				if(!labelDispersaoAnimada.contains(ip.getAno()+"")) {
					labelDispersaoAnimada.add(ip.getAno()+"");
				}
				if(!cidadesDispersaoAnimada.contains(nomeCidade)) {
					cidadesDispersaoAnimada.add(nomeCidade);
				}
			}
			labelDispersaoAnimada.sort((a,b) -> a.compareTo(b));
			cidadesDispersaoAnimada.sort((a,b) -> a.compareTo(b));
			indicadorComparativoDeCidadeDTO.setLabelsDispersaoAnimada(labelDispersaoAnimada);
			indicadorComparativoDeCidadeDTO.setCidadesDispersaoAnimada(cidadesDispersaoAnimada);
			indicadorComparativoDeCidadeDTO.setDispersao(dispersaoAnimada);
		}
	}

	public IndicadorComparativoDeCidadeDTO buscarIndicadorComparativoDeCidadesVisualizaIndicador(Long idIndicador, Long estado, Long cidade, Long populacao, Integer indexFormula) {

		Indicador indicador  = indicadorService.listarById(idIndicador);
		List<InstituicaoFonte> listaFontes = variavelPreenchidaService.buscaVariavelPreenchidaPorIdIndicador(idIndicador);
		IndicadorComparativoDeCidadeDTO dto;
		dto = IndicadorComparativoDeCidadeDTO.builder()
				.indicador(null)
				.descricao(null)
				.formula(null)
				.fontes(null)
				.meta(null)
				.ods(null)
				.odsNumero(null)
				.labels(null)
				.chartData(null)
				.serieHistorica(null)
				.treeMap(null)
				.numerico(indicador.isNumerico())
				.build();
		List<IndicadorPreenchido> listaIndicadoresPreenchidos = new ArrayList<>();

		List<IndicadorPreenchido> lista = indicadorPreenchidoService.buscarPorIndicadorCidadeVisualizarIndicador(idIndicador, estado, cidade, populacao);
		listaIndicadoresPreenchidos.addAll(lista);	


		StringBuilder sb = new StringBuilder();
		for (InstituicaoFonte fontes : listaFontes) {
			sb.append("<li>");
			sb.append(". ");
			sb.append(fontes.getNome());
			sb.append("</li>");
		}

		calculadoraFormulaUtil.formatarFormula(indicador);
		String meta = indicador.getMetaODS() != null ? "Meta "+indicador.getMetaODS().getNumero()+": "+indicador.getMetaODS().getDescricao() : "Meta não vinculada";
		String odsTitulo = indicador.getMetaODS() != null ? indicador.getOds().getTitulo() : null;
		Integer numero = ((indicador.getOds() != null) && (indicador.getOds().getNumero() != null)) ? indicador.getOds().getNumero() : null;
		dto = IndicadorComparativoDeCidadeDTO.builder()
				.indicador(indicador.getNome())
				.descricao(indicador.getDescricao())
				.formula(indicador.getFormulaResultado())
				.fontes(listaFontes)
				.meta("Meta "+ meta)
				.ods(odsTitulo)
				.odsNumero(numero)
				.numerico(dto.getNumerico())
				.build();
		if(listaIndicadoresPreenchidos != null && listaIndicadoresPreenchidos.size() > 0) {
			List<ComparativoGraficoDTO> graficos = gerarGraficos(listaIndicadoresPreenchidos,indicador,indexFormula);		
			List<SerieHistoricaDTO> serieHistorica = gerarSerieHistorica(listaIndicadoresPreenchidos );
			gerarDadosDeDispersao(listaIndicadoresPreenchidos,dto,indicador);		
			dto.setSerieHistorica(serieHistorica);
			dto.setGraficos(graficos);
			return dto;
		}
		else {
			dto = new IndicadorComparativoDeCidadeDTO();
		}
		return dto;
	}


	public IndicadorComparativoDeCidadeDTO buscarIndicadorComparativoDeCidadesComparativoCidades(Long idIndicador, List<Long> cidades) {

		Indicador indicador  = indicadorService.listarById(idIndicador);
		List<InstituicaoFonte> listaFontes = variavelPreenchidaService.buscaVariavelPreenchidaPorIdIndicador(idIndicador);
		IndicadorComparativoDeCidadeDTO dto;
		dto = IndicadorComparativoDeCidadeDTO.builder()
				.indicador(null)
				.descricao(null)
				.formula(null)
				.fontes(null)
				.meta(null)
				.ods(null)
				.odsNumero(null)
				.labels(null)
				.chartData(null)
				.serieHistorica(null)
				.treeMap(null)
				.numerico(indicador.isNumerico())
				.build();
		List<IndicadorPreenchido> listaIndicadoresPreenchidos = new ArrayList<>();

		List<IndicadorPreenchido> lista = indicadorPreenchidoService.buscarPorIndicadorCidadeComparativoCidades(idIndicador, cidades);
		listaIndicadoresPreenchidos.addAll(lista);	


		StringBuilder sb = new StringBuilder();
		for (InstituicaoFonte fontes : listaFontes) {
			sb.append("<li>");
			sb.append(". ");
			sb.append(fontes.getNome());
			sb.append("</li>");
		}

		calculadoraFormulaUtil.formatarFormula(indicador);
		String meta = indicador.getMetaODS() != null ? "Meta "+indicador.getMetaODS().getNumero()+": "+indicador.getMetaODS().getDescricao() : "Meta não vinculada";
		String odsTitulo = indicador.getMetaODS() != null ? indicador.getOds().getTitulo() : null;
		Integer numero = ((indicador.getOds() != null) && (indicador.getOds().getNumero() != null)) ? indicador.getOds().getNumero() : null;
		dto = IndicadorComparativoDeCidadeDTO.builder()
				.indicador(indicador.getNome())
				.descricao(indicador.getDescricao())
				.formula(indicador.getFormulaResultado())
				.fontes(listaFontes)
				.meta("Meta "+ meta)
				.ods(odsTitulo)
				.odsNumero(numero)
				.numerico(dto.getNumerico())
				.build();
		List<ComparativoGraficoDTO> graficos = gerarGraficos(listaIndicadoresPreenchidos,indicador,null);		
		gerarDadosDeDispersao(listaIndicadoresPreenchidos,dto,indicador);		
		dto.setGraficos(graficos);
		return dto;
	}

}
