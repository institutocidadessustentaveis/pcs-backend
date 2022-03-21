package br.org.cidadessustentaveis.resources;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.FormularioDTO;
import br.org.cidadessustentaveis.dto.FormularioResumidoDTO;
import br.org.cidadessustentaveis.model.participacaoCidada.Formulario;
import br.org.cidadessustentaveis.services.FormularioService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/formulario")
public class FormularioResource {
	@Autowired
	private FormularioService service; 

	@Secured({ "ROLE_VISUALIZAR_FORMULARIO" })
	@GetMapping("")
	public List<FormularioDTO> listar() {
		try {
			List<FormularioDTO> lista = service.listar();
			return lista;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	

	@GetMapping("/{id}")
	public FormularioDTO buscarPorId( @PathVariable Long id) {
		
		try {
			Formulario formulario = service.buscarPorId(id);
			FormularioDTO dto = new FormularioDTO(formulario);
			return dto;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Secured({ "ROLE_CADASTRAR_FORMULARIO" })
	@PostMapping
	public FormularioDTO salvar(@RequestBody FormularioDTO dto) throws Exception {
		service.salvar(dto);
		return dto;
	}

	@Secured({ "ROLE_EDITAR_FORMULARIO" })
	@PutMapping
	public FormularioDTO atualizar(@RequestBody FormularioDTO dto) throws Exception {
		service.atualizar(dto);
		return dto;
	}

	@Secured({ "ROLE_EXCLUIR_FORMULARIO" })
	@DeleteMapping("/{id}")
	public void excluir(@PathVariable Long id) throws AuthenticationException, Exception {
		service.excluir(id);
	}
	
	@GetMapping("/link")
	public FormularioDTO buscarPorId( @RequestParam String link) {
		
		try {
			Formulario formulario = service.buscarPorLink(link);
			FormularioDTO dto = new FormularioDTO(formulario);
			return dto;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@GetMapping("/buscarFormulariosResumido/{idCidade}")
	public List<FormularioResumidoDTO> buscarFormulariosResumido(@PathVariable Long idCidade) {
		try {
			List<FormularioResumidoDTO> lista = service.buscarFormulariosResumido(idCidade);
			return lista;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
}
