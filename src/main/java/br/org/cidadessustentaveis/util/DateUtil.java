package br.org.cidadessustentaveis.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
	public static String localDate2String (LocalDate data) {
		String convertido = data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		return convertido;
	}
	public static String localDate2StringAno (LocalDate data) {
		String convertido = data.format(DateTimeFormatter.ofPattern("yyyy"));
		return convertido;
	}

	public static boolean entre(LocalDate data, LocalDate dataInicial, LocalDate dataFinal){
		if(data == null || dataInicial == null || dataFinal == null){
			return false;
		}
		boolean estaEntre = false;

		if( ( dataInicial.equals(data) || dataInicial.isBefore(data) ) &&
			( dataFinal.equals(data) || dataFinal.isAfter(data) ) ){
				estaEntre = true;
			}

		return estaEntre;
	}
}
