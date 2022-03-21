package br.org.cidadessustentaveis.util;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NumeroUtil {
	public static boolean isANumber(String valor) {
		try {
			Double.parseDouble(valor);
			if(Double.isInfinite(Double.parseDouble(valor))) {
				return false;
			}
			if(Double.isNaN(Double.parseDouble(valor))) {
				return false;
			}
    		return true;
        } catch (NumberFormatException e) {
           return false;                  
        }
	}
	
	public static int toInt(String valor) throws Exception {
		vazio(valor);
		try {
			Double number = Double.parseDouble(valor);
			return number.intValue();
		} catch (Exception e) {
			throw new Exception("Não foi possivel converter o valor"+valor+" para um numero");
		}
		
	}

	public static long toLong(String valor) throws Exception {
		vazio(valor);
		try {
			Double number = Double.parseDouble(valor);
			return number.longValue();
		} catch (Exception e) {
			throw new Exception("Não foi possivel converter o valor"+valor+" para um numero");
		}
	}

	public static double toDouble(String valor) throws Exception {
		vazio(valor);
		try {
			Double number = Double.parseDouble(valor);
			return number.doubleValue();
		} catch (Exception e) {
			throw new Exception("Não foi possivel converter o valor"+valor+" para um numero");
		}
	}
	public static short toShort(String valor) throws Exception {
		vazio(valor);		
		try {
			Double number = Double.parseDouble(valor);
			return number.shortValue();
		} catch (Exception e) {
			throw new Exception("Não foi possivel converter o valor"+valor+" para um numero");
		}
	}
	
	public static void vazio(String valor) throws Exception {
		if(valor.isEmpty()) {
			throw new Exception("O Valor informado está vazio");
		}
	}
	
	public static Double arredondarDuasCasasDecimais(Double valor) {
		DecimalFormat aproximador = new DecimalFormat( "0.00" );
		String valorTexto = aproximador.format(valor);
		valorTexto = valorTexto.contains(",") ? valorTexto.replaceAll(",", ".") : valorTexto;
		Double valorRetorno = Double.parseDouble(valorTexto);
		return valorRetorno;
		
	}public static Double arredondarTresCasasDecimais(Double valor) {
		DecimalFormat aproximador = new DecimalFormat( "0.000" );
		String valorTexto = aproximador.format(valor);
		valorTexto = valorTexto.contains(",") ? valorTexto.replaceAll(",", ".") : valorTexto;
		Double valorRetorno = Double.parseDouble(valorTexto);
		return valorRetorno;
	}

	public static String decimalToString(Double valor) {
		DecimalFormat formatter = new DecimalFormat("###,###,###,###,###,###,##0.00");
		String valorTexto = formatter.format(valor);
		return valorTexto;
	}
	
	public static String decimal3ToString(Double valor) {
		DecimalFormat formatter = new DecimalFormat("###,###,###,###,###,###,##0.000");
		String valorTexto = formatter.format(valor);
		return valorTexto;
	}
	
	public static String integerToString(Double valor) {
		DecimalFormat formatter = new DecimalFormat("###,###,###,###,###,###,##0");
		String valorTexto = formatter.format(valor);
		return valorTexto;
	}
	
	public static List<Short> getMandatos(LocalDate inicio, LocalDate fim){
		List<Short> anos = new ArrayList<>();
		Short anoInicio= Short.valueOf(inicio.getYear()+"");
		Short anoFim= Short.valueOf(fim.getYear()+"");
		
		for(short i = anoInicio ; i <= anoFim; i++) {
			anos.add(i);			
		}
		return anos;		
	}
}
