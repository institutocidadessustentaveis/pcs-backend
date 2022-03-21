package br.org.cidadessustentaveis.services.dev;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.dev.VersaoBuild;
import br.org.cidadessustentaveis.model.enums.TipoBuild;
import br.org.cidadessustentaveis.repository.dev.VersaoBuildRepository;

@Service
public class VersaoBuildService {

	@Autowired
	private VersaoBuildRepository repository;

	public List<VersaoBuild> listar(){
		return repository.findAll();	
	}

	public VersaoBuild versaoAtual(TipoBuild tipoBuild){
		return repository.findTopByTipoBuildOrderByIdDesc(tipoBuild);	
	}

	public void gerarVersao(String[] args){
		try {
			VersaoBuild versaoBuild = new VersaoBuild();
			for( String arg : args) {			
				System.out.println("Arg: "+arg);
				if(arg.startsWith("BUILD_VERSION")) {
					StringTokenizer tokenizer = new StringTokenizer(arg, "=");
					while(tokenizer.hasMoreTokens()) {
						versaoBuild.setVersao(tokenizer.nextToken());
					}
				}
				if(arg.startsWith("HASH_GIT")) {
					StringTokenizer tokenizer = new StringTokenizer(arg, "=");
					while(tokenizer.hasMoreTokens()) {
						versaoBuild.setHashGit(tokenizer.nextToken());
					}
				}			
			}	
			if(!versaoBuild.getVersao().isEmpty()) {
				versaoBuild.setDataBuild(LocalDateTime.now());
				versaoBuild.setTipoBuild(TipoBuild.BACKEND);
				repository.save(versaoBuild);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}



}
