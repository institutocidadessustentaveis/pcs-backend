package br.org.cidadessustentaveis.resources.dev;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.model.dev.DBChangeLog;
import br.org.cidadessustentaveis.services.dev.DBChangeLogService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/dbchangelog")
public class DBChangeLogResource {

	@Autowired
	private DBChangeLogService service;
	
	@GetMapping("")
	public ResponseEntity<List<DBChangeLog>> buscar(){
		List<DBChangeLog> list = service.listar();		
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping("/versaoAtual")
	public ResponseEntity<DBChangeLog> findComPaginacao() {	
		return ResponseEntity.ok().body(service.versaoAtual());
	}
}
