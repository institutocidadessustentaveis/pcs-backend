package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.model.indicadores.PlanoDeMetas;
import br.org.cidadessustentaveis.model.institucional.Arquivo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlanoDeMetasDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String statusPlanoDeMetas;
	
	private String nomeCidade;
	
	private Long populacao;
	
	private Long idPrefeitura;
	
	private String nomePrefeito;
	
	private LocalDate inicioMandato;
	
	private LocalDate fimMandato;
	
	private Short primeiroAnoMandato;
	
	private Short segundoAnoMandato;
	
	private Short terceiroAnoMandato;
	
	private Short quartoAnoMandato;
	
	List<PlanoDeMetasDetalhadoDTO> planosDeMetasDetalhados;
	
	private UsuarioDTO usuario;
	
	private String apresentacao;
	
	private String descricao;
	
	private ArquivoDTO arquivo;

	private String siglaEstado;
	
	public PlanoDeMetasDTO (PlanoDeMetas planoDeMetasRef) {
		this.id = planoDeMetasRef.getId();
		this.statusPlanoDeMetas = planoDeMetasRef.getStatusPlanoDeMetas();
		this.nomeCidade = planoDeMetasRef.getPrefeitura().getCidade().getNome();
		this.siglaEstado = planoDeMetasRef.getPrefeitura().getCidade().getProvinciaEstado().getSigla()	;
		this.populacao = planoDeMetasRef.getPrefeitura().getCidade().getPopulacao();
		this.idPrefeitura = planoDeMetasRef.getPrefeitura().getId();
		this.nomePrefeito = planoDeMetasRef.getPrefeitura().getNome();
		this.inicioMandato = planoDeMetasRef.getPrefeitura().getInicioMandato();
		this.fimMandato = planoDeMetasRef.getPrefeitura().getFimMandato();
		this.planosDeMetasDetalhados = planoDeMetasRef.getPlanosDeMetasDetalhados().stream().map(obj -> new PlanoDeMetasDetalhadoDTO(obj)).collect(Collectors.toList());
		this.usuario = new UsuarioDTO(planoDeMetasRef.getUsuario()); 
		this.apresentacao = planoDeMetasRef.getApresentacao();
		this.descricao = planoDeMetasRef.getDescricao();
		this.arquivo = new ArquivoDTO(planoDeMetasRef.getArquivo() != null ? planoDeMetasRef.getArquivo() : new Arquivo());
	}
	
}
