package br.org.cidadessustentaveis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import lombok.Getter;
import lombok.Setter;

@ApplicationScope
@Service
public class ProfileUtil {
	@Autowired
    private Environment env;   
	@Getter @Setter
	private String version = "";
	
	public String getProperty(String propriedade) {
		try {
			String valor = env.getProperty(propriedade);
			return valor;
		}catch (Exception e) {
			System.out.println("Valor não encontrado");
			return "";
		}
	}
}
