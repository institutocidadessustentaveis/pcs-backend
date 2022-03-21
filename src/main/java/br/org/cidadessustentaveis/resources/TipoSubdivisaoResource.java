package br.org.cidadessustentaveis.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.org.cidadessustentaveis.dto.TipoSubdivisaoDTO;
import br.org.cidadessustentaveis.model.administracao.TipoSubdivisao;
import br.org.cidadessustentaveis.services.TipoSubdivisaoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/tipoSubdivisao")
public class TipoSubdivisaoResource {
	
	@Autowired
	private TipoSubdivisaoService subdivisaoService;
	
	@GetMapping("/buscarTodosPorPrefeituraId/{id}")
	public ResponseEntity<List<TipoSubdivisaoDTO>> buscarTodosPorPrefeituraId(@PathVariable Long id) throws Exception{
		List<TipoSubdivisaoDTO> dtos = subdivisaoService.buscarTodosPorPrefeituraId(id);
		return ResponseEntity.ok().body(dtos);
	}

	@GetMapping("/cidade/{id}")
	public ResponseEntity<List<TipoSubdivisaoDTO>> buscarPorCidadeId(@PathVariable Long id) throws Exception{
		List<TipoSubdivisao> tipos = subdivisaoService.buscarPorCidadeId(id);
		List<TipoSubdivisaoDTO> dtos = new ArrayList();
		for(TipoSubdivisao tipo : tipos){
			TipoSubdivisaoDTO dto = new TipoSubdivisaoDTO(tipo);
			dtos.add(dto);
		}
		return ResponseEntity.ok().body(dtos);
	}
	
	@Secured({ "ROLE_EXCLUIR_TIPO_SUBDIVISAO" })
	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<Void> apagar( @PathVariable Long id) {
		subdivisaoService.deletar(id);
		return ResponseEntity.ok().build();
	}
	
	@Secured({ "ROLE_CADASTRAR_TIPO_SUBDIVISAO" })
	@PostMapping("/cadastrar")
	public ResponseEntity<TipoSubdivisao> cadastrar( @RequestBody TipoSubdivisaoDTO evento) throws Exception {
		TipoSubdivisao tipoSubdivisao = subdivisaoService.inserir(evento);
		
		return ResponseEntity.ok(tipoSubdivisao);
	}
	
	@Secured({ "ROLE_EDITAR_TIPO_SUBDIVISAO" })
	@PutMapping("/editar")
	public ResponseEntity<TipoSubdivisaoDTO> alterar(@RequestBody TipoSubdivisaoDTO tipoSubdivisaoDTO) throws Exception {
		TipoSubdivisaoDTO dto = subdivisaoService.alterar(tipoSubdivisaoDTO);
		return  ResponseEntity.ok(dto);
	}

}
