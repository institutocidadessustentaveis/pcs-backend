package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.indicadores.Indicador;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class IndicadorSimplesDTO {
	private Long id;
	private String nome;
	private Integer numeroODS;
	private String nomeODS;
	private Long idEixo;
	private String nomeEixo;
	private String numeroMeta;
	private String descricaoMeta;
	private String formula;
	private String descricaoIndicador;
	private boolean numerico;
	private String ordemClassificacao;

	public IndicadorSimplesDTO(Indicador indicador) {
		id = indicador.getId();
		nome = indicador.getNome();
		if(indicador.getEixo() != null) {
			idEixo = indicador.getEixo().getId();
			nomeEixo = indicador.getEixo().getNome();
		}
		if(indicador.getOds() != null) {
			numeroODS = indicador.getOds().getNumero();
			nomeODS = indicador.getOds().getTitulo();
		}
		if(indicador.getMetaODS() != null) {
			numeroMeta = indicador.getMetaODS().getNumero(); 
			descricaoMeta = indicador.getMetaODS().getDescricao();
		}
		formula = indicador.getFormulaResultado();
		descricaoIndicador = indicador.getDescricao();
		this.numerico = indicador.isNumerico();
		this.ordemClassificacao = indicador.getOrdemClassificacao();
	}
}
