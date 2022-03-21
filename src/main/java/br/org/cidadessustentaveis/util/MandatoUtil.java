package br.org.cidadessustentaveis.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.org.cidadessustentaveis.dto.Mandato2DTO;

public class MandatoUtil {
	
	private static boolean anoIncluso(int ano, List<List<String>> mandatos) {
		for(List<String> mandato : mandatos) {
			int anoInicial = Integer.parseInt(mandato.get(0));
			int anoFinal = Integer.parseInt(mandato.get(1));
			if(ano >= anoInicial && ano <= anoFinal ) {
				return true;
			} 
		}
		return false;
	}
	
	public static List<List<String>> getMandatos(){
		List<List<String>> mandatos = new ArrayList<>();
		
		int anoAtual = LocalDate.now().getYear();
		for(int i = 2005; i <= anoAtual ; i++) {
			if(!anoIncluso(i, mandatos)) {
				List<String> mandato = new ArrayList<>();
				mandato.add(i+"");
				mandato.add((i+3)+"");
				mandato.add(i +" - "+ (i+3));
				mandatos.add(mandato);
			}			
		}
		
		return mandatos;
		
	}
	
	public static List<Mandato2DTO> getMandatoNovo(){
		
		List<Mandato2DTO> mandatos = new ArrayList<>();
		int anoAtual = LocalDate.now().getYear();
		for(short i = 2005 ; i < anoAtual ; i += 4) {
			Mandato2DTO mandato = new Mandato2DTO();
			mandato.setAnoInicioMandato(i);
			mandato.setAnoFimMandato((short) (i+3));
			mandato.setPeriodo(i + " - " + (i + 3));
			mandatos.add(mandato);
		}
		return mandatos;
	}
	
	public static List<String> getAnosMandatos(int anoInicial , int anoFinal){
		List<String> anosMandatos = new ArrayList<>();
		
		for(int i = anoInicial ; i<= anoFinal ; i++) {
			anosMandatos.add(i+"");
		}
		return anosMandatos;
	}
	
}
