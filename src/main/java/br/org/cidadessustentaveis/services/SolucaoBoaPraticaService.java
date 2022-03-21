package br.org.cidadessustentaveis.services;

import java.text.Normalizer;
import java.util.Optional;

import javax.transaction.Transactional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.SolucaoBoaPraticaDTO;
import br.org.cidadessustentaveis.model.boaspraticas.SolucaoBoaPratica;
import br.org.cidadessustentaveis.repository.SolucaoBoaPraticaRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.RestClient;

@Service
public class SolucaoBoaPraticaService {
	
	@Autowired
	private SolucaoBoaPraticaRepository repository;

	@Transactional
	public void deleteByIdBoaPratica(Long id) {
		repository.deleteByIdBoaPratica(id);
	}
	
	public SolucaoBoaPratica buscarPorId(Long id) {
		Optional<SolucaoBoaPratica> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Solução não encontrada!"));
	}
	
	public SolucaoBoaPraticaDTO buscarSolucaoBoaPraticaDTO(Long id) {
		SolucaoBoaPratica solucaoBoaPraticaRef = buscarPorId(id);
		SolucaoBoaPraticaDTO solucaoBoaPraticaDTO = new SolucaoBoaPraticaDTO(solucaoBoaPraticaRef);
		
		return solucaoBoaPraticaDTO;
	}
	
	public String buscarSolucaoImagem(Long id) {
		SolucaoBoaPratica solucaoBoaPratica = buscarPorId(id);
		
		try {
			RestClient rest = new RestClient(true);
			String resposta = rest.get("https://oics.cgee.org.br/solucoes/" + formataSolucao(solucaoBoaPratica));
			
			Document doc = Jsoup.parse(resposta);

			if(doc != null) {
				Element element = doc.getElementsByClass("inovacao-cover__img").first();
				if(element != null) {
					String img = element.attr("src");
					return img;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return "";
	}
	
	public String formataSolucao(SolucaoBoaPratica solucao) {
		String solucaoFormatada = solucao.getNome().toLowerCase();
		solucaoFormatada = Normalizer.normalize(solucaoFormatada, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
		String aux = solucaoFormatada.replace(" ", "-");
		
		return aux + "_" + solucao.get_id();
	}
}
