package br.org.cidadessustentaveis.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import br.org.cidadessustentaveis.dto.PremiacaoExibicaoDTO;
import br.org.cidadessustentaveis.dto.PremiacaoPaginacaoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.cidadessustentaveis.dto.PremiacaoDTO;
import br.org.cidadessustentaveis.model.administracao.Premiacao;
import br.org.cidadessustentaveis.services.InstitucionalService;
import br.org.cidadessustentaveis.services.PremiacaoPrefeituraService;
import br.org.cidadessustentaveis.services.PremiacaoService;
import springfox.documentation.annotations.ApiIgnore;	

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/premiacao")
public class PremiacaoResource {	

	@Autowired
	private PremiacaoService service;	
	
	@Autowired
	private PremiacaoPrefeituraService premiacaoPrefeituraService;	

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Secured({"ROLE_ADMINISTRAR_PREMIACOES","ROLE_VISUALIZAR_PREMIACOES"})
	@GetMapping("/buscarTodos")
	public ResponseEntity<PremiacaoPaginacaoDTO> buscar(
									@RequestParam(required = false, defaultValue = "0") Integer page,
									@RequestParam(required = false, defaultValue = "10") Integer itemsPerPage,
									@RequestParam(name = "orderBy", defaultValue = "inicio") String orderBy,
									@RequestParam(name = "direction", defaultValue = "DESC") String direction) {

		List<Premiacao> premiacoes = service.buscar(page, itemsPerPage, orderBy, direction);
		List<PremiacaoExibicaoDTO> premiacoesDto = premiacoes.stream()
																.map(obj -> new PremiacaoExibicaoDTO(obj))
															.collect(Collectors.toList());

		PremiacaoPaginacaoDTO dto = new PremiacaoPaginacaoDTO(premiacoesDto, service.count());

		return ResponseEntity.ok().body(dto);
	}
	 
	@Secured({"ROLE_ADMINISTRAR_PREMIACOES"})
	@PutMapping("/editarPremiacao/{id}/idArquivo/{idBannerPremiacaoRemover}")
	public ResponseEntity<Void> editar(@Valid @RequestBody PremiacaoDTO premiacaoDTO, @PathVariable("id") Long id, @PathVariable("idBannerPremiacaoRemover") Long idBannerPremiacaoRemover) {
		service.editar(premiacaoDTO, id);
		if(idBannerPremiacaoRemover != -1l) {
			service.deletarArquivo(idBannerPremiacaoRemover);
		}
		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_ADMINISTRAR_PREMIACOES"})
	@PostMapping("/inserirPremiacao")
	public ResponseEntity<Void> inserir(@RequestBody PremiacaoDTO premiacaoDTO) {
		Premiacao premiacao = service.inserirPremiacao(premiacaoDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(premiacao.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@Secured({"ROLE_ADMINISTRAR_PREMIACOES"})
	@GetMapping("/{id}")
	public ResponseEntity<PremiacaoDTO> buscarPorId(@PathVariable("id") Long id) {
		Premiacao premiacao = service.buscarPorId(id);
		return ResponseEntity.ok().body(new PremiacaoDTO(premiacao));
	}
	
	@Secured({"ROLE_ADMINISTRAR_PREMIACOES"})
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}	
	

	@Secured({"ROLE_ADMINISTRAR_PREMIACOES"})
	@GetMapping("/buscar/{descricao}")
	public ResponseEntity<List<PremiacaoDTO>> buscarPorDescricao(@PathVariable String descricao) {
		List<Premiacao> premiacoes = service.buscarPorDescricaoLike(descricao);
		List<PremiacaoDTO> premiacoesDto = premiacoes.stream().map(obj -> new PremiacaoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(premiacoesDto);
	}
	
	
	@Secured({"ROLE_VISUALIZAR_PREMIACOES"})
	@GetMapping("/participarPremiacao/{id}")
	public ResponseEntity<PremiacaoDTO> participarPremiacao(@PathVariable("id") Long id){
		Premiacao premiacao = premiacaoPrefeituraService.participarPremiacao(id);
		return ResponseEntity.ok().body(new PremiacaoDTO(premiacao));
	}
	
	@Secured({"ROLE_VISUALIZAR_PREMIACOES"})
	@GetMapping("/buscarPremiacoesPorPrefeitura")
	public ResponseEntity<List<PremiacaoDTO>> buscarPremiacoesPorPrefeitura() {
		List<Premiacao> premiacoes = service.buscarPremiacoesPorPrefeitura();
		List<PremiacaoDTO> premiacoesDto = premiacoes.stream().map(obj -> new PremiacaoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(premiacoesDto);
	}
	
	@Secured({"ROLE_VISUALIZAR_PREMIACOES"})
	@DeleteMapping("/cancelarInscricao/{id}")
	public ResponseEntity<Void> cancelarInscricao(@PathVariable("id") Long id) {
		premiacaoPrefeituraService.cancelarInscricao(id);
		return ResponseEntity.noContent().build();
	}	
	
	@GetMapping("/verificaParticipacaoPremiacaoEmAvaliacao")
	public ResponseEntity<List<PremiacaoDTO>> verificaParticipacaoPremiacaoEmAvaliacao() {
		List<Premiacao> premiacoes = service.buscarPremiacoesEmAvaliacaoPorPrefeitura();
		List<PremiacaoDTO> premiacoesDto = premiacoes.stream().map(obj -> new PremiacaoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(premiacoesDto);
	}
	

	@GetMapping("/buscarTodasInscricoesAbertas")
	public ResponseEntity<List<PremiacaoDTO>> buscarTodasInscricoesAbertas() {
		List<Premiacao> premiacoes = service.buscarPremiacoesEmInscricoesAbertas();
		List<PremiacaoDTO> premiacoesDto = null;
		if(premiacoes != null) {
			premiacoesDto = premiacoes.stream().map(obj -> new PremiacaoDTO(obj)).collect(Collectors.toList());
		}
		return ResponseEntity.ok().body(premiacoesDto);
	}
	
	
}
