package br.org.cidadessustentaveis.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.CidadeDTO;
import br.org.cidadessustentaveis.dto.IndicadorPreenchidoSimplesDTO;
import br.org.cidadessustentaveis.dto.RelatorioIndicadorCompletoDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.util.MandatoUtil;

@Service
public class RelatorioIndicadoresCompletoService {
	
	@Autowired
	private CidadeService cidadeService;
	
	@Autowired
	private IndicadorPreenchidoService indicadorPreenchidoService;
	
	@Autowired
	private PrefeituraService prefeituraService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	
	public List<RelatorioIndicadorCompletoDTO> gerarRelatorio() {
		
		List<Prefeitura> listaPrefeiturasSig = prefeituraService.listarPrefeiturasSignatariasVigentes();
		
		List<RelatorioIndicadorCompletoDTO> listaRelatorio = new ArrayList<RelatorioIndicadorCompletoDTO>();
		
		for(Prefeitura prefeitura : listaPrefeiturasSig) {
			RelatorioIndicadorCompletoDTO relatorio = new RelatorioIndicadorCompletoDTO();
			
			Cidade cidade = prefeitura.getCidade();
			
			relatorio.setCodigoIBGE(cidade.getCodigoIbge());
			relatorio.setCidade(cidade.getNome());
			relatorio.setEstado(cidade.getProvinciaEstado().getNome());
			relatorio.setPrefeito(prefeitura.getNome());
			relatorio.setPartido(prefeitura.getPartidoPolitico().getNome());
			relatorio.setPopulacao(cidade.getPopulacao());
			relatorio.setPorte(cidade.getPopulacao() != null ? this.definirPorteCidade(cidade.getPopulacao()) : null);
			relatorio.setUsuarioCadastrado(this.isUsuarioCadastrado(prefeitura.getId()));
			relatorio.setQtdUsuarioCadastrado(this.buscarQtdUsuariosCadastrados(prefeitura.getId()));
			relatorio.setIndicadoresMinimos(prefeitura.getCidade().getPopulacao() != null ? this.calcularIndicadoresMinimos(cidade.getPopulacao()) : null);
			relatorio.setQtdIndicadoresPreenchidos(this.contagemIndicadoresPreenchidos(prefeitura.getId()));
			relatorio.setPorcentagemIndicadoresPreenchidos(this.calcularPorcentagemIndicadoresPreenchidosPorIndicadoresMinimosString(relatorio.getQtdIndicadoresPreenchidos(), relatorio.getIndicadoresMinimos()));
			relatorio.setNumPorcentagemIndicadores(this.calcularPorcentagemIndicadoresPreenchidosPorIndicadoresMinimos(relatorio.getQtdIndicadoresPreenchidos(), relatorio.getIndicadoresMinimos()));
			
			listaRelatorio.add(relatorio);
		}
		
		return listaRelatorio;
		
	}
	
	public String definirPorteCidade(Long populacao) {
		if(populacao <= 100000) {
			return "Pequeno";
		} else if (populacao > 100000 && populacao <= 500000) {
			return "Médio";
		} else if (populacao > 500000) {
			return "Grande";
		} else {
			return null;
		}
		
	}
	
	public boolean isUsuarioCadastrado(Long idPrefeitura) {
		int count = usuarioService.countUsuariosPrefeitura(idPrefeitura);
		if(count > 0) {
			return true;
		} 
		
		return false;
	}
	
	public Long buscarQtdUsuariosCadastrados(Long idPrefeitura) {
		int count = usuarioService.countUsuariosPrefeitura(idPrefeitura);
		return Long.valueOf(count);
	}
	
	public Long calcularIndicadoresMinimos(Long populacao) {
		if(populacao <= 100000) {
			return 50L;
		} else if (populacao > 100000 && populacao <= 500000) {
			return 75L;
		} else if (populacao > 500000) {
			return 100L;
		} else {
			return null;
		}
	}
	
	public Long contagemIndicadoresPreenchidos(Long idPrefeitura) {
		List<IndicadorPreenchidoSimplesDTO> listaIndicadoresPreenchidos = indicadorPreenchidoService.findByPrefeituraSimplesRelatorio(idPrefeitura);
		List<Long> idsIndicadoresAux = new ArrayList<Long>();
		
		int anoInicial = 0;
		List<List<String>> mandatos = MandatoUtil.getMandatos();
		for (List<String> mandato : mandatos) {
			anoInicial = Integer.parseInt(mandato.get(0));
		}
		
		int qtdPreenchida = 0;
		if(listaIndicadoresPreenchidos.size() > 0) {
			for(IndicadorPreenchidoSimplesDTO indicador : listaIndicadoresPreenchidos) {
				if(indicador.getComplementar() != null && !indicador.getComplementar()) {
					if(indicador.getDataPreenchimento().getYear() >= anoInicial) {
						if(!idsIndicadoresAux.contains(indicador.getIdIndicador())) {
							idsIndicadoresAux.add(indicador.getIdIndicador());
							qtdPreenchida++;
						}
					}
				}
			}
		}
		
		
		return Long.valueOf(qtdPreenchida);
	}
	
	public Integer calcularPorcentagemIndicadoresPreenchidosPorIndicadoresMinimos(Long qtdPreenchida, Long indicadoresMinimos) {
		int numIndicadoresMinimos = 0;
		Integer porcentagemIndicadores = 0;
		if(qtdPreenchida > 0 && indicadoresMinimos != null) {
			numIndicadoresMinimos = indicadoresMinimos.intValue();
			porcentagemIndicadores = (qtdPreenchida.intValue() * 100) / numIndicadoresMinimos;
		}
		
		return porcentagemIndicadores;
	}
	
	public String calcularPorcentagemIndicadoresPreenchidosPorIndicadoresMinimosString(Long qtdPreenchida, Long indicadoresMinimos) {
		int numIndicadoresMinimos = 0;
		Integer porcentagemIndicadores = 0;
		if(qtdPreenchida > 0 && indicadoresMinimos != null) {
			numIndicadoresMinimos = indicadoresMinimos.intValue();
			porcentagemIndicadores = (qtdPreenchida.intValue() * 100) / numIndicadoresMinimos;
		}
		
		String porcentagem = porcentagemIndicadores.toString() + "%";
		
		return porcentagem;
	}
	
	public File gerarArquivoXlsx() throws IOException {
		List<RelatorioIndicadorCompletoDTO> lista = this.gerarRelatorio();
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet indicadoresCompleto = workbook.createSheet("Indicadores Completo");
		indicadoresCompleto = gerarCabecalho(indicadoresCompleto);
		
		int rownum = 0;		
		for(RelatorioIndicadorCompletoDTO registro : lista) {
			
			Row row = indicadoresCompleto.createRow(++rownum);
			
			CellStyle cellStyleSimple = workbook.createCellStyle();
			cellStyleSimple.setLocked(false);
			
			CellStyle cellStyleGreen = workbook.createCellStyle();
			cellStyleGreen.setLocked(false);
			cellStyleGreen.setFillForegroundColor(IndexedColors.GREEN.getIndex());
			cellStyleGreen.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			CellStyle cellStyleRed = workbook.createCellStyle();
			cellStyleRed.setLocked(false);
			cellStyleRed.setFillForegroundColor(IndexedColors.RED.getIndex());
			cellStyleRed.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			Cell cellCodIbge = row.createCell(0);
			cellCodIbge.setCellValue(registro.getCodigoIBGE());
			cellCodIbge.setCellStyle(cellStyleSimple);
			
			Cell cellCidade = row.createCell(1);
			cellCidade.setCellValue(registro.getCidade());
			cellCidade.setCellStyle(cellStyleSimple);
			
			Cell cellEstado = row.createCell(2);
			cellEstado.setCellValue(registro.getEstado());
			cellEstado.setCellStyle(cellStyleSimple);
			
			Cell cellPrefeito = row.createCell(3);
			cellPrefeito.setCellValue(registro.getPrefeito());
			cellPrefeito.setCellStyle(cellStyleSimple);
			
			Cell cellPartido = row.createCell(4);
			cellPartido.setCellValue(registro.getPartido());
			cellPartido.setCellStyle(cellStyleSimple);
			
			Cell cellPopulacao = row.createCell(5);
			cellPopulacao.setCellValue(registro.getPopulacao() != null ? registro.getPopulacao().toString() : "N/A");
			cellPopulacao.setCellStyle(cellStyleSimple);
			
			Cell cellPorte = row.createCell(6);
			cellPorte.setCellValue(registro.getPorte() != null ? registro.getPorte() : "N/A");
			cellPorte.setCellStyle(cellStyleSimple);
			
			Cell cellUsuarioCadastrado = row.createCell(7);
			cellUsuarioCadastrado.setCellValue(registro.isUsuarioCadastrado() ? "Sim" : "Não");
			cellUsuarioCadastrado.setCellStyle(cellStyleSimple);
			
			Cell cellQtdUserCad = row.createCell(8);
			cellQtdUserCad.setCellValue(registro.getQtdUsuarioCadastrado().toString());
			cellQtdUserCad.setCellStyle(cellStyleSimple);
			
			Cell cellIndicadoresMin = row.createCell(9);
			cellIndicadoresMin.setCellValue(registro.getIndicadoresMinimos() != null ? registro.getIndicadoresMinimos().toString() : "N/A");
			cellIndicadoresMin.setCellStyle(cellStyleSimple);
			
			Cell cellQtdIndPreenchido = row.createCell(10);
			cellQtdIndPreenchido.setCellValue(registro.getQtdIndicadoresPreenchidos().toString());
			cellQtdIndPreenchido.setCellStyle(cellStyleSimple);
			
			Cell cellPorcentagemIndPreenchido = row.createCell(11);
			cellPorcentagemIndPreenchido.setCellValue(registro.getPorcentagemIndicadoresPreenchidos());
			if(registro.getNumPorcentagemIndicadores() < 99) {
				cellPorcentagemIndPreenchido.setCellStyle(cellStyleRed);
			} else {
				cellPorcentagemIndPreenchido.setCellStyle(cellStyleGreen);
			}
			
		}
		
		File arquivo = new File("arquivo_"+LocalDateTime.now().getNano()+".xlsx");
		FileOutputStream out = new FileOutputStream(arquivo);
		workbook.write(out);
		workbook.close();
		out.close();

		return arquivo;
	}
	
	private XSSFSheet gerarCabecalho(XSSFSheet aba) {
		Row row = aba.createRow(0);
		XSSFCellStyle style = aba.getWorkbook().createCellStyle();
		XSSFFont font = aba.getWorkbook().createFont();
		font.setBold(true);
		style.setFont(font);
		style.setLocked(true);

		Cell cellCodIbge = row.createCell(0);
		cellCodIbge.setCellStyle(style);
		cellCodIbge.setCellValue("Código IBGE");
		
		Cell cellCidade = row.createCell(1);
		cellCidade.setCellValue("Cidade");
		cellCidade.setCellStyle(style);
		
		Cell cellEstado = row.createCell(2);
		cellEstado.setCellValue("Estado");
		cellEstado.setCellStyle(style);
		
		Cell cellPrefeito = row.createCell(3);
		cellPrefeito.setCellValue("Prefeito");
		cellPrefeito.setCellStyle(style);
		
		Cell cellPartido = row.createCell(4);
		cellPartido.setCellValue("Partido");
		cellPartido.setCellStyle(style);
		
		Cell cellPopulacao = row.createCell(5);
		cellPopulacao.setCellValue("População");
		cellPopulacao.setCellStyle(style);
		
		Cell cellPorte = row.createCell(6);
		cellPorte.setCellValue("Porte");
		cellPorte.setCellStyle(style);
		
		Cell cellUsuarioCadastrado = row.createCell(7);
		cellUsuarioCadastrado.setCellValue("Usuários Cadastrados");
		cellUsuarioCadastrado.setCellStyle(style);
		
		Cell cellQtdUserCad = row.createCell(8);
		cellQtdUserCad.setCellValue("Qtd. Usuários Cadastrados");
		cellQtdUserCad.setCellStyle(style);
		
		Cell cellIndicadoresMin = row.createCell(9);
		cellIndicadoresMin.setCellValue("Indicadores Mínimos");
		cellIndicadoresMin.setCellStyle(style);
		
		Cell cellQtdIndPreenchido = row.createCell(10);
		cellQtdIndPreenchido.setCellValue("Qtd. Indicadores Preenchidos");
		cellQtdIndPreenchido.setCellStyle(style);
		
		Cell cellPorcentagemIndPreenchido = row.createCell(11);
		cellPorcentagemIndPreenchido.setCellValue("% Indicadores Preenchidos");
		cellPorcentagemIndPreenchido.setCellStyle(style);
		
		return aba;	
	}
	

}
