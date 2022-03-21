package br.org.cidadessustentaveis.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.model.administracao.InstituicaoFonte;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.services.InstituicaoFonteService;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/instituicaofonte")
public class InstituicaoFonteResource {

	@Autowired
	private InstituicaoFonteService service;

	@Autowired
	private UsuarioContextUtil usuarioContextUtil;
	
	@GetMapping("/carregacomboboxinstituicaofonte")
	public ResponseEntity<List<ItemComboDTO>> buscarComboBoxInstituicaoFonte() {
		try{
			Usuario usuario = usuarioContextUtil.getUsuario();
			if(null == usuario) {
				throw new AuthorizationServiceException("Você precisa estar logado!");
			}
			if(null == usuario.getPrefeitura()) {
				throw new AuthorizationServiceException("Você não está associado a uma prefeitura!");
			}
			
			List<InstituicaoFonte> instituicaoFonte = service.buscarComboBoxInstituicaoFonte(usuario.getPrefeitura().getCidade().getId());
			List<ItemComboDTO> instituicaoFonteDto = instituicaoFonte.stream().map(obj -> new ItemComboDTO(obj.getId(), obj.getNome()))
					.collect(Collectors.toList());
			return ResponseEntity.ok().body(instituicaoFonteDto);
		}catch (Exception e) {
			throw new AuthorizationServiceException("Você precisa estar logado!");
		}
	}
	
	@PostMapping("/inserirFonte")
	public ResponseEntity<Void> inserir(@RequestBody InstituicaoFonte instituicaoFonte) {
		InstituicaoFonte fonte = service.inserirInstituicaoFonte(instituicaoFonte);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(fonte.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
}
