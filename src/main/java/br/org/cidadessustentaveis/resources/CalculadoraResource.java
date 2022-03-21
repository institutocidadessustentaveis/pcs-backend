package br.org.cidadessustentaveis.resources;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.util.CalculadoraFormulaUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/calculadora")
public class CalculadoraResource {
	
	@Getter @Setter @NoArgsConstructor
	static class FormulaDTO {
		@NotNull(message = "Obrigatório preenchimento da fórmula.")
		private String formula;
	}
	
	@Autowired
	private CalculadoraFormulaUtil calculadora;

	@PostMapping("/validar")
	public ResponseEntity<Boolean> validar(@RequestBody FormulaDTO formula) {		
		Boolean resultado = calculadora.validarFormula(formula.getFormula());
		
		return ResponseEntity.ok().body(resultado);
	}

}
