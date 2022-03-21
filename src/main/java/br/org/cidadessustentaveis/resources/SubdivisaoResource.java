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

import br.org.cidadessustentaveis.dto.SubdivisaoCidadeDTO;
import br.org.cidadessustentaveis.dto.SubdivisaoDTO;
import br.org.cidadessustentaveis.dto.SubdivisaoFilhosDTO;
import br.org.cidadessustentaveis.model.administracao.SubdivisaoCidade;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ShapeFile;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ShapeItem;
import br.org.cidadessustentaveis.services.ShapeFileService;
import br.org.cidadessustentaveis.services.ShapeItemService;
import br.org.cidadessustentaveis.services.SubdivisaoService;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/subdivisao")
public class SubdivisaoResource {
    
	@Autowired
	private SubdivisaoService subdivisaoService;
	@Autowired
	private UsuarioContextUtil usuarioContextUtil;
	@Autowired
	private ShapeFileService shapeFileService;
	@Autowired
	private ShapeItemService shapeItemService;
	
	
    @GetMapping()
    private List<SubdivisaoCidadeDTO> todasSubdivisoes(){
        return new ArrayList();
    }
    
    @GetMapping("/cadastradas")
    private List<SubdivisaoCidadeDTO> subdivisoesCadastradasCidade(){
        return new ArrayList();
    }

    @PostMapping()
    private void cadastrarSubdivisao(@RequestBody SubdivisaoCidadeDTO subdivisaoDTO){

        
    }
    
	
	@GetMapping("/buscarTodosPorCidadeId/{id}")
	public ResponseEntity<List<SubdivisaoDTO>> buscarTodosPorCidadeId(@PathVariable Long id) throws Exception{
		List<SubdivisaoDTO> dtos = subdivisaoService.buscarTodosPorCidadeId(id);
		return ResponseEntity.ok().body(dtos);
	}

	@GetMapping("/{id}")
	public ResponseEntity<SubdivisaoDTO> buscarPorId(@PathVariable Long id) throws Exception{
		SubdivisaoCidade subdivisao = subdivisaoService.buscarPorId(id);
		SubdivisaoDTO dto = new SubdivisaoDTO(subdivisao);
		return ResponseEntity.ok().body(dto);
	}

	@GetMapping("/arvore/{idCidade}")
	public ResponseEntity<List<SubdivisaoFilhosDTO>> buscarArvoreCidade(@PathVariable Long idCidade) throws Exception{
		List<SubdivisaoCidade> listaSubdivisao = subdivisaoService.buscarPorNivelECidade(1l, idCidade);
		List<SubdivisaoFilhosDTO> dtos = new ArrayList<>();
		for(SubdivisaoCidade subdivisao: listaSubdivisao){
			SubdivisaoFilhosDTO dto = new SubdivisaoFilhosDTO(subdivisao);
			this.subdivisaoService.preencherFilhosDTO(dto);
			dtos.add(dto);
		}
		return ResponseEntity.ok().body(dtos);
	}
	@GetMapping("/{uf}/{cidade}/{nomeSubdivisao}")
	public ResponseEntity<SubdivisaoDTO> buscarUfCidadeSubdivisao(@PathVariable String uf, 
			@PathVariable String cidade, 
			@PathVariable String nomeSubdivisao) throws Exception{
		SubdivisaoCidade subdivisao = subdivisaoService.buscarUfCidadeSubdivisao(uf, cidade, nomeSubdivisao);
		SubdivisaoDTO dto = new SubdivisaoDTO(subdivisao);
		return ResponseEntity.ok().body(dto);
	}
	
	@GetMapping("/feature/{idCidade}/{nivel}")
	public ResponseEntity<List<SubdivisaoDTO>> buscarFeaturesCidadeTipo(@PathVariable Long idCidade,@PathVariable Long nivel) throws Exception{
		List<SubdivisaoCidade> listaSubdivisao = subdivisaoService.buscarPorNivelECidade(nivel, idCidade);
		List<SubdivisaoDTO> dtos = new ArrayList<>();
		
		for(SubdivisaoCidade subdivisao: listaSubdivisao){
			SubdivisaoDTO dto = new SubdivisaoDTO(subdivisao);
			ShapeFile shapeFile = shapeFileService.buscarShapeFilePorSubdivisaoId(subdivisao.getId());
			dto.setFeatures(new ArrayList<>());
			shapeFile.getShapes().forEach(shapeItem -> {
				dto.getFeatures().add(shapeItemService.convertEntityToFeature(shapeItem));
				dtos.add(dto);
			});
		}
		
		return ResponseEntity.ok().body(dtos);
	}

	@GetMapping("/feature/{idSubdivisao}")
	public ResponseEntity<SubdivisaoDTO> buscarFeaturesCidadeTipo(@PathVariable Long idSubdivisao) throws Exception{
		SubdivisaoCidade subdivisao = subdivisaoService.buscarPorId(idSubdivisao);
		SubdivisaoDTO dto = new SubdivisaoDTO(subdivisao);
		ShapeFile shapeFile = shapeFileService.buscarShapeFilePorSubdivisaoId(subdivisao.getId());
		dto.setFeatures(new ArrayList<>());
		shapeFile.getShapes().forEach(shapeItem -> {
			dto.getFeatures().add(shapeItemService.convertEntityToFeature(shapeItem));
		});
		
		return ResponseEntity.ok().body(dto);
	}


	
	@GetMapping("/buscarTodosPorCidade")
	public ResponseEntity<List<SubdivisaoDTO>> buscarTodosPorCidade() throws Exception{
		Usuario usuario = this.usuarioContextUtil.getUsuario();
		Long idCidade = usuario.getPrefeitura().getCidade().getId();
		List<SubdivisaoDTO> dtos = subdivisaoService.buscarTodosPorCidadeId(idCidade);
		return ResponseEntity.ok().body(dtos);
	}
	
	@GetMapping("/buscarTodasSubdivisaoRelacionadasComSubdivisaoPai/{id}")
	public ResponseEntity<List<SubdivisaoDTO>> buscarTodasSubdivisaoRelacionadasComSubdivisaoPai(@PathVariable Long id) throws Exception{
		List<SubdivisaoDTO> dtos = subdivisaoService.buscarTodasSubdivisaoRelacionadasComSubdivisaoPai(id);
		return ResponseEntity.ok().body(dtos);
	}
	
	@Secured({ "ROLE_EXCLUIR_SUBDIVISAO" })
	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<Void> apagar( @PathVariable Long id) {
		subdivisaoService.deletar(id);
		return ResponseEntity.ok().build();
	}
	
	@Secured({ "ROLE_CADASTRAR_SUBDIVISAO" })
	@PostMapping("/cadastrar")
	public ResponseEntity<SubdivisaoCidade> cadastrar( @RequestBody SubdivisaoDTO evento) throws Exception {
		SubdivisaoCidade subdivisao = subdivisaoService.inserir(evento);
		
		return ResponseEntity.ok(subdivisao);
	}
	
	@Secured({ "ROLE_EDITAR_SUBDIVISAO" })
	@PutMapping("/editar")
	public ResponseEntity<SubdivisaoDTO> alterar(@RequestBody SubdivisaoDTO subdivisaoDTO) throws Exception {
		SubdivisaoDTO dto = subdivisaoService.alterar(subdivisaoDTO);
		return  ResponseEntity.ok(dto);
	}

    
}
