package br.org.cidadessustentaveis.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.cidadessustentaveis.dto.FaqDTO;
import br.org.cidadessustentaveis.dto.FiltroBibliotecasDTO;
import br.org.cidadessustentaveis.dto.FiltroFaqDTO;
import br.org.cidadessustentaveis.dto.NoticiaBoletimDTO;
import br.org.cidadessustentaveis.model.participacaoCidada.Faq;
import br.org.cidadessustentaveis.services.FaqService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/faq")
public class FaqResource {
	
	@Autowired(required=true)
	private FaqService service;
	
	@Secured({ "ROLE_CADASTRAR_FAQ" })
	@PostMapping("/cadastrar")
	public ResponseEntity<FaqDTO> cadastrar( @RequestBody FaqDTO faq) {
		service.inserir(faq);
		return ResponseEntity.ok(faq);
	}
	
	@Secured({ "ROLE_EXCLUIR_FAQ" })
	@DeleteMapping("excluir/{id}")
	public ResponseEntity<Void> apagar( @PathVariable Long id) {
			service.deletar(id);
		return ResponseEntity.ok().build();
	}
	
	@Secured({ "ROLE_EDITAR_FAQ" })
	@PutMapping("/editar")
	public ResponseEntity<FaqDTO> alterar(@RequestBody FaqDTO faqDTO) throws Exception {
		Faq faq = service.alterar(faqDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(faq.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	
	@GetMapping("/buscarFaqPorId/{id}")
	public ResponseEntity<FaqDTO> buscarFaqPorId(@PathVariable("id") Long id){
		FaqDTO faq = service.buscarFaqPorId(id);
		return ResponseEntity.ok().body(faq);
	}
	
	@GetMapping("/buscarFaqFiltrado")
	public List<FiltroFaqDTO> buscarFaqFiltrado(@RequestParam("q") @Length(min = 2, max = 50) String palavraChave) throws Exception{
		List<Faq> listaFaq = service.buscarFaqFiltrado(palavraChave);
		return listaFaq.stream().map(n -> new FiltroFaqDTO(n)).collect(Collectors.toList());
	}
	
	@GetMapping("/listar")
	public ResponseEntity<List<Faq>> listarTodos() {
		List<Faq> faq = service.Listar();
		return ResponseEntity.ok().body(faq);
	}
	
}
