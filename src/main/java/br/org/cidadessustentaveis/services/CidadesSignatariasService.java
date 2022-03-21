package br.org.cidadessustentaveis.services;

import java.io.IOException;


import javax.persistence.EntityManager;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



@Service
public class CidadesSignatariasService {
	
	@Autowired
	private EntityManager em;
	
	public static final String URL = "https://www.cidadessustentaveis.org.br/arquivos/imagens/subtitulo-pagina-cidades-signatarias.txt";
	
	public String conteudoTxtFtp() throws IOException {
				
		String text = Jsoup.connect(URL).get().text();
		
		if(text != null) {
			return text;
		} else {
			return null;
		}
		
	}
	
	

}
