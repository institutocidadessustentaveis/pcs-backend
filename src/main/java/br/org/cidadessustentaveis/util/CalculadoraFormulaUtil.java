package br.org.cidadessustentaveis.util;

import static java.util.regex.Pattern.matches;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import br.org.cidadessustentaveis.dto.VariavelDTO;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.model.indicadores.SubdivisaoIndicadorPreenchido;
import br.org.cidadessustentaveis.model.indicadores.SubdivisaoVariavelPreenchida;
import br.org.cidadessustentaveis.model.indicadores.Variavel;
import br.org.cidadessustentaveis.model.indicadores.VariavelPreenchida;
import br.org.cidadessustentaveis.services.VariavelService;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CalculadoraFormulaUtil {

	private Logger log = LoggerFactory.getLogger(CalculadoraFormulaUtil.class);
	
	@Autowired
	private VariavelPreenchidaUtil variavelPreenchidaUtil;
	
	@Autowired
	private VariavelService variavelService;

	@Bean
	private ExpressionParser getParser() {
		return new SpelExpressionParser();
	}
	
	public Integer contarVariaveis(String texto) {
		Integer total = 0;
		
		for(int i = 0; i < texto.length(); i++) {
			 char ch = texto.charAt(i);
			 String x1 = String.valueOf(ch);
			 
			 if( x1.equalsIgnoreCase("#")) {
				 total+=1;
			 }
		}
		return total;
	}
	
	public boolean valida(String variavel) {

		for (int i = 0; i < variavel.length(); i++) { 
		    if (!Character.isDigit((variavel.charAt(i)))) { 
		        return false; } }

		return true; 
	}
	
	//FORMATAR FÓRMULA FOI AJUSTADA PARA CARREGAR OS VALORES CORRETOS, PROBLEMA ESTÁ NO BANCO QUE ESTÁ FALTANDO VARIÁVEIS NA RELAÇÃO COM O INDICADOR
	public void formatarFormula(Indicador indicador) {
		String parcial = null;
		String[] teste = new String[contarVariaveis(indicador.getFormulaResultado())/2];
		teste = indicador.getFormulaResultado().split("#");
		List<Long> listaIdsVariaveis = new ArrayList<>();
		for (String valor : teste) {
			if(valor != null && valor != "" && valor.length() > 0) {
				if(valida(valor)) {
					listaIdsVariaveis.add(Long.parseLong(valor));
				}
			}
		}
		
		if (listaIdsVariaveis != null && !listaIdsVariaveis.isEmpty()) {
			List<VariavelDTO> listaVariavel = new ArrayList<VariavelDTO>();
			listaVariavel = variavelService.buscarPorIdIndicador(listaIdsVariaveis);
			parcial = indicador.getFormulaResultado();
			parcial = parcial.replace("/", " ÷ ");
			parcial = parcial.replace("*", " × ");
			parcial = parcial.replace(",", ", ");
			indicador.setFormulaResultado(parcial);
			for (VariavelDTO valor : listaVariavel) {
				parcial = indicador.getFormulaResultado();
//				parcial = parcial.replace("/", " ÷ ");
//				parcial = parcial.replace("*", " x ");
//				parcial = parcial.replace(",", ", ");
				parcial = parcial.replace("#" + valor.getId() + "#", valor.getNome());
				indicador.setFormulaResultado(parcial);
			}			
		}
		
		indicador.setFormulaResultado(indicador.getFormulaResultado().replaceAll("\\.", ","));
		
		if (indicador.getFormulaResultado().contains("concat")) {
			indicador.setFormulaResultado(indicador.getFormulaResultado().replaceAll("concat", ""));
			indicador.setFormulaResultado(indicador.getFormulaResultado().replace(";", "<br>"));
			indicador.setFormulaResultado(indicador.getFormulaResultado().replaceAll("[()]", ""));
		}
	}

	public void calcularResultado(IndicadorPreenchido preenchido) {
		try {						
			Indicador indicador = preenchido.getIndicador();
						
			Map<Variavel, Double> valoresPreenchidos = preenchido.getVariaveisPreenchidas().stream()
					.collect(Collectors.toMap(VariavelPreenchida::getVariavel, preenchida -> variavelPreenchidaUtil.getValorVariavelPreenchida(preenchida)));
			
			String formula = indicador.getFormulaResultado().replaceAll(",", ".");
			String referencia = indicador.getFormulaReferencia() == null ? "" : indicador.getFormulaReferencia().replaceAll(",", ".");
			
			List<Variavel> variaveis = indicador.getVariaveis();
			
			for (Variavel variavel : variaveis) {
				Double valor = valoresPreenchidos.get(variavel);
				if ( null != valor ) {
					String tag = "#" + variavel.getId() + "#";
					formula = formula.replaceAll(tag, valor.toString());
					referencia = referencia.replaceAll(tag, valor.toString());
				}
			}
			
			if (formula.contains("concat")) {
				List<String> itens = getItensConcat(formula);
				List<Double> valores = itens.stream()
						.map(item -> (Double) getParser().parseExpression(item).getValue())
						.collect(Collectors.toList());
				preenchido.setResultado(valores.toString());
			} else {
				Double valor = Double.valueOf(getParser().parseExpression(formula).getValue()+"");
				preenchido.setResultado(valor.toString());
			}
			
			if ( null != referencia && !referencia.isEmpty()) {
				Double valor = Double.valueOf(getParser().parseExpression(referencia).getValue()+"");
				preenchido.setResultadoReferencia(valor);
			}		
			preenchido.setResultadoApresentacao(preenchido.getValorApresentacaoTabelas());
			if(!preenchido.getIndicador().isNumerico()){
				String valorTexto = preenchido.formulaTextualDadosAbertos();
				if(valorTexto != null){
					preenchido.setValorTexto(valorTexto);
				}
			}
			
		} catch (Exception e) {
			log.error("Não foi possível realizar o calculo do indicador");
			preenchido.setResultadoApresentacao(preenchido.getValorApresentacaoTabelas());
			if(!preenchido.getIndicador().isNumerico()){
				String valorTexto = preenchido.formulaTextualDadosAbertos();
				if(valorTexto != null){
					preenchido.setValorTexto(valorTexto);
				}
			}
		}	
		
		
	}
	public void calcularResultado(SubdivisaoIndicadorPreenchido preenchido) {
		try {						
			Indicador indicador = preenchido.getIndicador();
						
			Map<Variavel, Double> valoresPreenchidos = preenchido.getVariaveisPreenchidas().stream()
					.collect(Collectors.toMap(SubdivisaoVariavelPreenchida::getVariavel, preenchida -> variavelPreenchidaUtil.getValorVariavelPreenchida(preenchida)));
			
			String formula = indicador.getFormulaResultado().replaceAll(",", ".");
			String referencia = indicador.getFormulaReferencia() == null ? "" : indicador.getFormulaReferencia().replaceAll(",", ".");
			
			List<Variavel> variaveis = indicador.getVariaveis();
			
			for (Variavel variavel : variaveis) {
				Double valor = valoresPreenchidos.get(variavel);
				if ( null != valor ) {
					String tag = "#" + variavel.getId() + "#";
					formula = formula.replaceAll(tag, valor.toString());
					referencia = referencia.replaceAll(tag, valor.toString());
				}
			}
			
			if (formula.contains("concat")) {
				List<String> itens = getItensConcat(formula);
				List<Double> valores = itens.stream()
						.map(item -> (Double) getParser().parseExpression(item).getValue())
						.collect(Collectors.toList());
				preenchido.setResultado(valores.toString());
			} else {
				Double valor = Double.valueOf(getParser().parseExpression(formula).getValue()+"");
				preenchido.setResultado(valor.toString());
			}
			
			if ( null != referencia && !referencia.isEmpty()) {
				Double valor = Double.valueOf(getParser().parseExpression(referencia).getValue()+"");
				preenchido.setResultadoReferencia(valor);
			}		
			preenchido.setResultadoApresentacao(preenchido.getValorApresentacaoTabelas());
			if(!preenchido.getIndicador().isNumerico()){
				String valorTexto = preenchido.formulaTextualDadosAbertos();
				if(valorTexto != null){
					preenchido.setValorTexto(valorTexto);
				}
			}
			
		} catch (Exception e) {
			log.error("Não foi possível realizar o calculo do indicador");
			preenchido.setResultadoApresentacao(preenchido.getValorApresentacaoTabelas());
			if(!preenchido.getIndicador().isNumerico()){
				String valorTexto = preenchido.formulaTextualDadosAbertos();
				if(valorTexto != null){
					preenchido.setValorTexto(valorTexto);
				}
			}
		}	
		
		
	}

	public Boolean validarFormula(String formula) {
		formula = formula.toLowerCase();
		if (formula.contains("concat")) {
			if (!formula.startsWith("concat(") || !formula.endsWith(")")) {
				return false;
			}
			
			List<String> itens = getItensConcat(formula);
			
			Long validos = itens.stream()
					.filter(item -> validarFormulaMatematica(item))
					.count();
			
			return validos == itens.size();
		} else {
			return validarFormulaMatematica(formula);
		}
	}

	private List<String> getItensConcat(String formula) {	
		formula = formula.substring(7, formula.length() - 1);
		List<String> itens = Arrays.asList(formula.split(";", -1));
		
		return itens;
	}

	private Boolean validarFormulaMatematica(String formula) {
		formula = formula.replaceAll("\\s", "");
		if (formula.contains(";") || formula.isEmpty() ||
				formula.endsWith("/") || formula.endsWith("*")) {
			return false;
		}
		Stack<Character> paratenses = new Stack<>();
		String numero = "";
		String operacao = "";
		String ultimaOperacao = "";
		boolean hasNumero = false;
		for (char c : formula.toCharArray()) {
			if ( c == '(') {
				if ( operacao.isEmpty() & !numero.isEmpty()) {
					return false;
				}
				paratenses.add(c);
			} else if ( c == ')' ) {
				if ( paratenses.isEmpty() || !operacao.isEmpty()) {
					return false;
				}
				paratenses.pop();
			} else if (isOperacao(c)) {
				if (operacao.isEmpty()) {
					operacao = String.valueOf(c);
				} else {
					return false;
				}
				if ( !validarNumero(numero, ultimaOperacao) ) {
					return false;
				}
				if (numero.isEmpty() && c != '+' && c != '-') {
					return false;
				}
				numero = "";
			} else {
				if (!operacao.isEmpty()) {
					ultimaOperacao =  operacao;
				}
				operacao = "";
				numero += c;
				hasNumero = true;
			}
		}
		if ( !validarNumero(numero, ultimaOperacao) ) {
			return false;
		}
		if ( paratenses.size() > 0 ) {
			return false;
		}
		return true && hasNumero;
	}

	private Boolean validarNumero(String numero, String ultimaOperacao) {
		if (numero.startsWith("#")) {
			if (!matches("[#]\\d+[#]", numero)) {
				return false;
			}
		} else {
			try {
				if (!numero.isEmpty()) {
					double n = Double.parseDouble(numero.replace(",", "."));
					if (n == 0 && ultimaOperacao.equals("/")) {
						return false;
					}
				}
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	private boolean isOperacao(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/';
	}

}
