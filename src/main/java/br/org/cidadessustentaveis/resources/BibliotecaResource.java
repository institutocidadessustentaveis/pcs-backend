package br.org.cidadessustentaveis.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.xmlbeans.impl.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.github.slugify.Slugify;

import br.org.cidadessustentaveis.dto.BibliotecaDTO;
import br.org.cidadessustentaveis.dto.BibliotecaDetalhesDTO;
import br.org.cidadessustentaveis.dto.BibliotecaSimplesDTO;
import br.org.cidadessustentaveis.dto.CombosBibliotecaDTO;
import br.org.cidadessustentaveis.dto.FiltroBibliotecaPlanoLeisRegulamentacaoDTO;
import br.org.cidadessustentaveis.dto.FiltroBibliotecasDTO;
import br.org.cidadessustentaveis.dto.HomeImagemDTO;
import br.org.cidadessustentaveis.dto.IdBibliotecasDTO;
import br.org.cidadessustentaveis.model.biblioteca.Biblioteca;
import br.org.cidadessustentaveis.model.institucional.Arquivo;
import br.org.cidadessustentaveis.services.BibliotecaService;
import br.org.cidadessustentaveis.services.HomeService;
import br.org.cidadessustentaveis.util.ImageUtils;
import br.org.cidadessustentaveis.util.ZipFileWriter;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/biblioteca")
public class BibliotecaResource {

	@Autowired(required= true)
	private BibliotecaService service;
	@Autowired
	private HomeService homeService;
	
	@Secured({ "ROLE_CADASTRAR_BIBLIOTECA" })
	@PostMapping("/cadastrar")
	public ResponseEntity<BibliotecaDTO> cadastrar( @RequestBody BibliotecaDTO biblioteca) {
		service.inserir(biblioteca);
		return ResponseEntity.ok(biblioteca);
	}
	
	@Secured({ "ROLE_EXCLUIR_BIBLIOTECA" })
	@DeleteMapping("excluir/{id}")
	public ResponseEntity<Void> apagar( @PathVariable Long id) {
			service.deletar(id);
		return ResponseEntity.ok().build();
	}
	
	@Secured({"ROLE_EDITAR_BIBLIOTECA"})
	@PutMapping(value = "/editar/{id}")
	public ResponseEntity<BibliotecaDTO> alterar(final @PathVariable("id") Long id,
													@RequestBody BibliotecaDTO bibliotecaDTO) throws Exception {
		Biblioteca bibliotecaRef = service.alterar(bibliotecaDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(bibliotecaRef.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	
	@GetMapping("/buscarBibliotecaPorId/{id}")
	public ResponseEntity<BibliotecaDTO> buscarEventoPorId(@PathVariable("id") Long id) throws Exception{
		BibliotecaDTO biblioteca = service.buscarBibliotecaPorId(id);
		return ResponseEntity.ok().body(biblioteca);
	}
	@GetMapping("/buscarBibliotecaSimples/{id}")
	public ResponseEntity<BibliotecaSimplesDTO> buscarBibliotecaSimples(@PathVariable("id") Long id){
		Biblioteca biblioteca = service.buscarPorId(id);
		BibliotecaSimplesDTO dto = new BibliotecaSimplesDTO(biblioteca);
		return ResponseEntity.ok().body(dto);
	}
	
	
	@GetMapping("/buscarBibliotecasToList/{idUsuario}")
	public ResponseEntity<List<BibliotecaDTO>> buscarBibliotecasToList(@PathVariable("idUsuario") Long idUsuario){
		List<BibliotecaDTO> bibliotecasDTO = service.buscarBibliotecasToList(idUsuario);
		return ResponseEntity.ok().body(bibliotecasDTO);
	}
	
	@GetMapping("/buscarBibliotecasToListAdmin")
	public ResponseEntity<List<BibliotecaDTO>> buscarBibliotecasToListAdmin() throws Exception{
		List<BibliotecaDTO> bibliotecasDTO = service.buscarBibliotecasToListAdmin();
		return ResponseEntity.ok().body(bibliotecasDTO);
	}
	
	@GetMapping("/buscarBibliotecasParaComboBox")
	public ResponseEntity<List<BibliotecaDTO>> buscarBibliotecasParaComboBox() throws Exception {
		List<BibliotecaDTO> bibliotecasDTO = service.buscarBibliotecasParaComboBox();
		return ResponseEntity.ok().body(bibliotecasDTO);
	}
	
	@GetMapping("/buscarBibliotecaDetalhesPorId/{id}")
	public ResponseEntity<BibliotecaDetalhesDTO> buscarBibliotecaDetalhesPorId(@PathVariable("id") Long id){
		BibliotecaDetalhesDTO biblioteca = service.buscarBibliotecaDetalhesPorId(id);
		return ResponseEntity.ok().body(biblioteca);
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity<byte[]> downloadArquivos( @PathVariable("id") Long idBiblioteca, HttpServletRequest
	 request) throws IOException{
		
		List<Arquivo> arquivos = service.buscarArquivoPublicacao(idBiblioteca);
		Biblioteca biblioteca = service.buscarPorId(idBiblioteca);
		List<File> listFile = new ArrayList<File>();
		String pathInicial = "arquivos-biblioteca/";

		for(Arquivo arquivo : arquivos){
			String nomeArquivo = arquivo.getNomeArquivo();
			File file = new File( pathInicial , nomeArquivo );
			file.getParentFile().mkdirs();
			byte[] bytesArquivo = java.util.Base64.getDecoder().decode(arquivo.getConteudo());
			FileUtils.writeByteArrayToFile( file, bytesArquivo );
			listFile.add(file);
		}
		byte[] bytesZip = null;
		List<String> nomeArquivos = new ArrayList<String>();
		for(File file : listFile){
			nomeArquivos.add(file.getAbsolutePath());
		}
		String zipFilePath = ZipFileWriter.zip(nomeArquivos);

		bytesZip = IOUtils.toByteArray(new FileInputStream(new File(zipFilePath)));

		for(File file : listFile){
			file.delete();
		}

		HttpHeaders headers = new HttpHeaders();
		
		
		Slugify slg = new Slugify();
		slg.withLowerCase(true);
		String publicacao = slg.slugify(biblioteca.getTituloPublicacao())+".zip";
		headers.add("Content-Disposition", "attachment;filename=\""+ publicacao +"\"");
		return new ResponseEntity<byte[]>(bytesZip, headers, HttpStatus.OK);
	}

	@GetMapping(value = "/imagem/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> buscarImagem(final @PathVariable("id") Long id) throws Exception {
        Biblioteca biblioteca = service.buscarPorId(id);

        if(biblioteca == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl("public");
        headers.set("ETag", ImageUtils.generateImageETag(biblioteca.getImagemCapa().getConteudo()));

        return new ResponseEntity<byte[]>(Base64.decode(biblioteca.getImagemCapa().getConteudo().getBytes()), headers, HttpStatus.OK);
	}
	
	@GetMapping(value = "/imagemHome", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> buscarImagem() throws Exception {
		List<HomeImagemDTO> imagensDTO = homeService.buscarListaImagensGaleriaPorId(11l);
		HomeImagemDTO imagemDTOEscolhido = null;
        if(imagensDTO == null || imagensDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
		}
		if(imagensDTO != null) {
			if(imagensDTO != null) {
				for(HomeImagemDTO imagemDTO : imagensDTO){
					if(imagemDTO.getExibirBusca()){
						imagemDTOEscolhido = imagemDTO;
						break;
					}
				}
			}
		}

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl("public");
        headers.set("ETag", ImageUtils.generateImageETag(imagemDTOEscolhido.getConteudo()));

        return new ResponseEntity<byte[]>(Base64.decode(imagemDTOEscolhido.getConteudo().getBytes()), headers, HttpStatus.OK);
    }
	
	@GetMapping("/idBibliotecasOrdenadas")
	public ResponseEntity<List<IdBibliotecasDTO>> carregarIdsOrdenados() {
		List<Biblioteca> listaIdBiblioteca = service.carregarIdsOrdenados();
		List<IdBibliotecasDTO> listaIdBibliotecasDTO = listaIdBiblioteca.stream()
																.map(obj -> new IdBibliotecasDTO(obj))
																.collect(Collectors.toList());
		return ResponseEntity.ok().body(listaIdBibliotecasDTO);
	}
	@GetMapping("/buscarBibliotecaPlanoLeisRegulamentacaoFiltrida")
	public ResponseEntity<List<FiltroBibliotecaPlanoLeisRegulamentacaoDTO>> buscarBibliotecaPlanoLeisRegulamentacaoFiltrida(
			@RequestParam Long idCidade, @RequestParam Long idProvinciaEstado, @RequestParam Long idPais, @RequestParam Long idTema, @RequestParam String palavraChave) throws Exception{
				List<FiltroBibliotecaPlanoLeisRegulamentacaoDTO> listaBiblioteca = service.buscarBibliotecaPlanoLeisRegulamentacaoFiltrida(idCidade, idProvinciaEstado, idPais, idTema, palavraChave);
		return ResponseEntity.ok().body(listaBiblioteca);
	}
	
	@GetMapping("/carregarCombosBiblioteca")
	public ResponseEntity<CombosBibliotecaDTO> carregarCombosBiblioteca(){
		CombosBibliotecaDTO dto = service.carregarCombosBiblioteca();
		return ResponseEntity.ok().body(dto);
	}
	
	@GetMapping("/buscarBibliotecasFiltrado")
	public ResponseEntity<List<FiltroBibliotecasDTO>> buscarBibliotecasFiltrado(@RequestParam Long idAreaInteresse, @RequestParam List<String> modulo, @RequestParam Long idEixo, @RequestParam Long idOds, @RequestParam Long idMetasOds,
			@RequestParam Long idIndicador, @RequestParam Long idCidade, @RequestParam Long idProvinciaEstado, @RequestParam Long idPais, @RequestParam String palavraChave
			) throws Exception{
		List<FiltroBibliotecasDTO> listaBibliotecas = service.buscarBibliotecasFiltrado(idAreaInteresse, modulo, idEixo, idOds, idMetasOds, idIndicador, idCidade, idProvinciaEstado, idPais, palavraChave );
		return ResponseEntity.ok().body(listaBibliotecas);
	}
	
	
}
