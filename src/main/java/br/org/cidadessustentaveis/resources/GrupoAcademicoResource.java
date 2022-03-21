package br.org.cidadessustentaveis.resources;

import java.net.URI;
import java.util.List;

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

import br.org.cidadessustentaveis.dto.EmpresaDTO;
import br.org.cidadessustentaveis.dto.FiltroGruposAcademicosDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoCardDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoComboDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoDetalheDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoPainelDTO;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.biblioteca.Biblioteca;
import br.org.cidadessustentaveis.model.contribuicoesAcademicas.GrupoAcademico;
import br.org.cidadessustentaveis.services.GrupoAcademicoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/grupoAcademico")
public class GrupoAcademicoResource {

	@Autowired(required= true)
	private GrupoAcademicoService service;
	
	@Secured({ "ROLE_CADASTRAR_GRUPO_ACADEMICO" })
	@PostMapping("/cadastrar")
	public ResponseEntity<GrupoAcademicoDTO> cadastrar( @RequestBody GrupoAcademicoDTO grupoAcademicoDTO) {
		service.inserir(grupoAcademicoDTO);
		return ResponseEntity.ok(grupoAcademicoDTO);
	}
	
	@Secured({ "ROLE_EXCLUIR_GRUPO_ACADEMICO" })
	@DeleteMapping("excluir/{id}")
	public ResponseEntity<Void> apagar( @PathVariable Long id) {
			service.deletar(id);
		return ResponseEntity.ok().build();
	}
	
	@Secured({"ROLE_EDITAR_GRUPO_ACADEMICO"})
	@PutMapping(value = "/editar/{id}")
	public ResponseEntity<GrupoAcademicoDTO> alterar(final @PathVariable("id") Long id,
													@RequestBody GrupoAcademicoDTO grupoAcademicoDTO) throws Exception {
		GrupoAcademico bibliotecaRef = service.alterar(grupoAcademicoDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(bibliotecaRef.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	
	@GetMapping("/buscarGrupoAcademicoPorId/{id}")
	public ResponseEntity<GrupoAcademicoDTO> buscarGrupoAcademicoPorId(@PathVariable("id") Long id) throws Exception{
		GrupoAcademicoDTO grupoAcademico = service.buscarGrupoAcademicoPorId(id);
		return ResponseEntity.ok().body(grupoAcademico);
	}
	
	@GetMapping("/filtrarGruposAcademicosToList/{nomeGrupoAcademico}")
	public ResponseEntity<List<GrupoAcademicoDTO>> filtrarGruposAcademicosToList(@PathVariable("nomeGrupoAcademico") String nomeGrupoAcademico){
		List<GrupoAcademicoDTO> gruposAcademicos = service.filtrarGruposAcademicosToList(nomeGrupoAcademico);
		return ResponseEntity.ok().body(gruposAcademicos);
	}
	
	@GetMapping("/buscarInstituicaoPorId/{id}")
	public ResponseEntity<EmpresaDTO> buscarInstituicaoPorId(@PathVariable("id") Long id){
		EmpresaDTO grupoAcademico = service.buscarInstituicaoPorId(id);
		return ResponseEntity.ok().body(grupoAcademico);
	}
	
	@GetMapping("/buscarGruposAcademicosFiltrados")
	public ResponseEntity<List<FiltroGruposAcademicosDTO>> buscarGruposAcademicosFiltrados(
			@RequestParam(required=false)String tipoCadastro,
			@RequestParam(required=false)Long idAreaInteresse,
			@RequestParam(required=false)Long idEixo,
			 @RequestParam(required=false)Long idOds,
			 @RequestParam(required=false)Long idCidade,
			 @RequestParam(required=false)Long idProvinciaEstado,
			 @RequestParam(required=false)Long idPais,
			 @RequestParam(required=false)String palavraChave,
			 @RequestParam(required=false)String vinculo,
			 @RequestParam (required=false)String nomeGrupo,
			 @RequestParam (required=false)List<String> setoresApl,
			 @RequestParam (required=false)Long cidadesApl,
			 @RequestParam (required=false)Long receitaAnual,
			 @RequestParam (required=false)boolean participaApl,
			 @RequestParam (required=false)boolean associadaEthos,
			 @RequestParam (required=false)String setorEconomico,
			 @RequestParam (required=false)Long quantidadeFuncionarios,
			 @RequestParam (required=false)boolean atuaProjetoSustentabilidade,
			 @RequestParam (required=false)Long quantidadeAlunosMin,
			 @RequestParam (required=false)Long quantidadeAlunosMax,
			 @RequestParam (required=false)boolean possuiExperiencias,
			 @RequestParam (required=false)String tipoInstituicaoAcademia
			) throws Exception{
		List<FiltroGruposAcademicosDTO> listaGruposAcademicos = service.buscarGruposAcademicosFiltrados(tipoCadastro, idAreaInteresse, idEixo, idOds, idCidade, idProvinciaEstado, idPais, palavraChave,
				vinculo,  nomeGrupo,  setoresApl,  cidadesApl,  receitaAnual,  participaApl,  associadaEthos,  setorEconomico,  quantidadeFuncionarios, atuaProjetoSustentabilidade, quantidadeAlunosMin,
				quantidadeAlunosMax, possuiExperiencias, tipoInstituicaoAcademia);
		return ResponseEntity.ok().body(listaGruposAcademicos);
	}
	
	@GetMapping("/buscarGrupoAcademicoCard/{id}")
	public ResponseEntity<GrupoAcademicoCardDTO> buscarGrupoAcademicoPorIdCard(@PathVariable("id") Long id) throws Exception{
		GrupoAcademicoCardDTO grupoAcademicoCard = service.buscarGrupoAcademicoPorIdCard(id);
		return ResponseEntity.ok().body(grupoAcademicoCard);
	}

	@GetMapping("/buscarGrupoAcademicoPorIdPainel/{id}")
	public ResponseEntity<GrupoAcademicoPainelDTO> buscarGrupoAcademicoPorIdPainel(@PathVariable("id") Long id) throws Exception{
		GrupoAcademicoPainelDTO grupoAcademicoPainel = service.buscarGrupoAcademicoPorIdPainel(id);

		List<Eixo> eixos = service.findEixoById(grupoAcademicoPainel.getId());
		grupoAcademicoPainel.listarEixos(eixos);
		
		List<AreaInteresse> areasInteresse = service.findAreaInteresseById(grupoAcademicoPainel.getId());
		grupoAcademicoPainel.listarAreasInteresse(areasInteresse);
		
		List<ObjetivoDesenvolvimentoSustentavel> ods = service.findODSById(grupoAcademicoPainel.getId());
		grupoAcademicoPainel.listarOds(ods);
		
		List<Biblioteca> bibliotecas = service.findBibliotecaById(grupoAcademicoPainel.getId());
		grupoAcademicoPainel.listarBibliotecas(bibliotecas);

		return ResponseEntity.ok().body(grupoAcademicoPainel);
	}
	
	@GetMapping("/buscarGruposAcademicosToList")
	public ResponseEntity<List<GrupoAcademicoDTO>> buscarBibliotecasToList() throws Exception{
		List<GrupoAcademicoDTO> grupoAcademicoDTO = service.buscarGruposAcademicosToList();
		return ResponseEntity.ok().body(grupoAcademicoDTO);
	}
	
	@GetMapping("/buscarGruposAcademicosMapa")
	public ResponseEntity<List<GrupoAcademicoDTO>> buscarGruposAcademicosMapa(){
		List<GrupoAcademicoDTO> grupoAcademicoDTO = service.buscarGruposAcademicosMapa();
		return ResponseEntity.ok().body(grupoAcademicoDTO);
	}
	
	@GetMapping("/buscarComboGruposAcademicos")
	public ResponseEntity<List<GrupoAcademicoComboDTO>> buscarComboGruposAcademicos(){
		List<GrupoAcademicoComboDTO> grupoAcademicoComboDTO = service.buscarComboGruposAcademicos();
		return ResponseEntity.ok().body(grupoAcademicoComboDTO);
	}
	
	@GetMapping("/buscarGrupoAcademicoPorIdDetalhesDTO/{id}")
	public ResponseEntity<GrupoAcademicoDetalheDTO> buscarGrupoAcademicoPorIdDetalhesDTO(@PathVariable("id") Long id){
		GrupoAcademicoDetalheDTO grupoAcademico = service.buscarGrupoAcademicoPorIdDetalhesDTO(id);
		return ResponseEntity.ok().body(grupoAcademico);
	}
	
	@GetMapping("/buscarOdsDoGrupoAcademicoPorId/{id}")
	public ResponseEntity<List<ObjetivoDesenvolvimentoSustentavel>> buscarOdsDoGrupoAcademicoPorId(@PathVariable("id") Long id){
		List<ObjetivoDesenvolvimentoSustentavel> listaOds = service.buscarOdsDoGrupoAcademicoPorId(id);
		return ResponseEntity.ok().body(listaOds);	
	}
}
