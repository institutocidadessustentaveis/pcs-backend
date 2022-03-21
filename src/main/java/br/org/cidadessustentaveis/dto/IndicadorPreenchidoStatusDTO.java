package br.org.cidadessustentaveis.dto;

import java.util.List;

import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.model.indicadores.SubdivisaoIndicadorPreenchido;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class IndicadorPreenchidoStatusDTO {
	
	private Long id;
	
	private String nome;
	
	private String prefeitura;
	
	private String status;
	
	private Long idEixo;
	
	private Boolean complementar;
	
	public IndicadorPreenchidoStatusDTO(Indicador indicador) {
		this(indicador, null);
	}
	
	public IndicadorPreenchidoStatusDTO(Indicador indicador, IndicadorPreenchido preenchido) {
		this.id = indicador.getId();
		this.nome = indicador.getNome();
		this.prefeitura = (null != preenchido && null != preenchido.getPrefeitura()) ? preenchido.getPrefeitura().getCidade().getNome() : null;
		this.status = (null != preenchido && (preenchido.getResultadoApresentacao() != null && !preenchido.getResultadoApresentacao().isEmpty())) ? "Preenchido" : "Pendente";
		this.idEixo = indicador.getEixo().getId();
		this.complementar = indicador.getComplementar();
	}
	public IndicadorPreenchidoStatusDTO(Indicador indicador, SubdivisaoIndicadorPreenchido preenchido,boolean subdivisao) {
		this.id = indicador.getId();
		this.nome = indicador.getNome();
		this.prefeitura = (null != preenchido && null != preenchido.getPrefeitura()) ? preenchido.getPrefeitura().getCidade().getNome() : null;
		this.status = (null != preenchido && (preenchido.getResultadoApresentacao() != null && !preenchido.getResultadoApresentacao().isEmpty())) ? "Preenchido" : "Pendente";
		this.idEixo = indicador.getEixo().getId();
		this.complementar = indicador.getComplementar();
	}
	
	public IndicadorPreenchidoStatusDTO(Indicador indicador, List<IndicadorPreenchido> preenchido, String status) {
		this.id = indicador.getId();
		this.nome = indicador.getNome();
		this.prefeitura = (null != preenchido && null != preenchido.get(0).getPrefeitura()) ? preenchido.get(0).getPrefeitura().getCidade().getNome() : null;
		this.idEixo = indicador.getEixo().getId();
		Boolean corretamentePreenchido = false;
		for (IndicadorPreenchido indicadorPreenchido : preenchido) {
			if(indicadorPreenchido.getResultadoApresentacao() != null && !indicadorPreenchido.getResultadoApresentacao().isEmpty()) {
				corretamentePreenchido = true;
			}
		}
		
		this.status = (null != preenchido && corretamentePreenchido) ? status : "Pendente";
		this.complementar = indicador.getComplementar();
	}
	
	public IndicadorPreenchidoStatusDTO(Indicador indicador, List<SubdivisaoIndicadorPreenchido> preenchido, String status, boolean subdivisao) {
		this.id = indicador.getId();
		this.nome = indicador.getNome();
		this.prefeitura = (null != preenchido && null != preenchido.get(0).getPrefeitura()) ? preenchido.get(0).getPrefeitura().getCidade().getNome() : null;
		this.idEixo = indicador.getEixo().getId();
		Boolean corretamentePreenchido = false;
		for (SubdivisaoIndicadorPreenchido indicadorPreenchido : preenchido) {
			if(indicadorPreenchido.getResultadoApresentacao() != null && !indicadorPreenchido.getResultadoApresentacao().isEmpty()) {
				corretamentePreenchido = true;
			}
		}
		
		this.status = (null != preenchido && corretamentePreenchido) ? status : "Pendente";
		this.complementar = indicador.getComplementar();
	}

}
