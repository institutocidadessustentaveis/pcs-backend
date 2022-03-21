package br.org.cidadessustentaveis.dto;



import br.org.cidadessustentaveis.model.planjementoIntegrado.ConsultaBoaPratica;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter @Setter @NoArgsConstructor
public class FiltroCidadesComBoasPraticas {

	private Integer page;
	
	private Integer linesPerPage;
	
	private String continente;
	
	private Long idPais;
	
	private ItemComboDTO pais;
	
	private Long idEstado;
	
	private ItemComboDTO estado;
	
	private Long idCidade;
	
	private ItemComboDTO cidade;
	
	private Long idEixo;
	
	private Long idOds;
	
	private Long idMetaOds;
	
	private ItemComboDTO metaOds;
	
	private Long popuMin;
	
	private Long popuMax;
	
	private Long idIndicador;
	
	private String nomeConsulta;
	
	private Long idConsulta;
	
	private boolean visualizarComoPontos = true;
	
	private String palavraChave;
	
	public FiltroCidadesComBoasPraticas(ConsultaBoaPratica consultaBoaPratica) {
		this.idConsulta = consultaBoaPratica.getId();
		this.idPais = consultaBoaPratica.getIdPais();
		this.idEstado = consultaBoaPratica.getIdEstado();
		this.idCidade = consultaBoaPratica.getIdCidade();
		this.continente = consultaBoaPratica.getContinente();
		this.idEixo = consultaBoaPratica.getIdEixo();
		this.idOds = consultaBoaPratica.getIdOds();
		this.idMetaOds = consultaBoaPratica.getIdMetaOds();
		this.popuMin = consultaBoaPratica.getPopuMin();
		this.popuMax = consultaBoaPratica.getPopuMax();
		this.idIndicador = consultaBoaPratica.getIdIndicador();
		this.nomeConsulta = consultaBoaPratica.getNome();
		this.visualizarComoPontos = consultaBoaPratica.getVisualizarComoPontos();
	}

}
