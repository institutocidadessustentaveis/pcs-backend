package br.org.cidadessustentaveis.util;

import java.util.List;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.repository.ParametroGeralRepository;

/**
 *
 * @author Leo
 */
@Service
public class RecalculoUtil {
	
	@Autowired
	private ParametroGeralRepository parametroGeralRepository; 
	
	@Async("threadRecalculo")
    public void enviarEmailHTML(int random_int) throws EmailException, InterruptedException {
		System.out.println("comecei :"+random_int);
		teste(random_int);
		Thread.sleep(2000);
		System.out.println("terminei :"+random_int);
    }
	
	public void teste(int i) {
		System.out.println("Executando :"+i);
	}
}
