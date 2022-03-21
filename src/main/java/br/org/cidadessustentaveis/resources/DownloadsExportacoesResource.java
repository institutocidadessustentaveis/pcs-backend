package br.org.cidadessustentaveis.resources;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.DownloadsExportacoesDTO;
import br.org.cidadessustentaveis.dto.ExibirCidadeProvinciaEstadoDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.model.administracao.DownloadsExportacoes;
import br.org.cidadessustentaveis.services.DownloadsExportacoesService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/downloadsexportacoes")
public class DownloadsExportacoesResource {

	@Autowired
	private DownloadsExportacoesService downloadsExportacoesService;

	@PostMapping("/registraLogDownloadsExportacoes")
	public ResponseEntity<Void> registrarLogDownload(@RequestBody DownloadsExportacoesDTO dto) {
		dto.setId(null);
		dto.setDataHora(LocalDateTime.now());
		dto.setNomeUsuario(SecurityContextHolder.getContext().getAuthentication().getName());

		downloadsExportacoesService.insert(dto);
		return ResponseEntity.status(201).build();
	}
	
	@GetMapping("/buscarComboBoxArquivo")
	public ResponseEntity<List<ItemComboDTO>> buscarComboBoxArquivo() {
		List<DownloadsExportacoes> listadownloadExportacao = downloadsExportacoesService.buscarComboBoxArquivo();
		List<ItemComboDTO> itensDTO = 
				listadownloadExportacao.stream()
							.map(obj -> new ItemComboDTO(obj))
							.collect(Collectors.toList());
		return ResponseEntity.ok().body(itensDTO);
	}

}
