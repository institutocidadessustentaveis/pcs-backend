package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.participacaoCidada.Formulario;
import br.org.cidadessustentaveis.model.participacaoCidada.SecaoFormulario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormularioDTO {
	private Long id;
	private String nome;
	private String descricao;
	private String cidade;
	private Boolean publicado;
	private String usuarioCriador;
	private LocalDate dataCriacao;
	private LocalDate inicioPeriodoAtividade;
	private LocalDate fimPeriodoAtividade;
	private String tipoUsuario;
	private Boolean apenasAutenticados;
	private Boolean exibirPaginaPrefeitura;
	private String link;
	private Long idUsuarioCriador;
	private List<Long> eixos;
	private List<Long> ods;
	private List<Long> temas;
	private List<SecaoFormularioDTO> secoes;
	private Long idPrefeituraDoUsuarioCriador;
	private List<String> eixosNomes;
	private List<String> temasNomes;
	private String prefeituraCidade;
	
	public FormularioDTO(Formulario formulario) {
		this.id = formulario.getId();
		this.nome = formulario.getNome();
		this.descricao = formulario.getDescricao();
		if(formulario.getUsuarioCriador() != null && formulario.getUsuarioCriador().getPrefeitura() != null) {
			this.cidade = formulario.getUsuarioCriador().getPrefeitura().getCidade().getNome(); 
		} else {
			this.cidade = "ICS";
		}
		this.idPrefeituraDoUsuarioCriador = formulario.getUsuarioCriador() != null ? (formulario.getUsuarioCriador().getPrefeitura() != null ? formulario.getUsuarioCriador().getPrefeitura().getId() : null) : null;
		this.publicado = formulario.getPublicado();
		this.usuarioCriador = formulario.getUsuarioCriador().getNome();
		
		if(formulario.getSecoes() != null) {
			this.secoes = new ArrayList<>();
			formulario.getSecoes().forEach(s -> this.secoes.add(new SecaoFormularioDTO(s)));
		}
		this.dataCriacao = formulario.getDataCriacao();
		this.inicioPeriodoAtividade = formulario.getInicioPeriodoAtividade();
		this.fimPeriodoAtividade = formulario.getFimPeriodoAtividade();
		this.tipoUsuario = formulario.getTipoUsuario();
		this.apenasAutenticados = formulario.getApenasAutenticados();
		this.exibirPaginaPrefeitura = formulario.getExibirPaginaPrefeitura();
		this.link = formulario.getLink();
		this.idUsuarioCriador = formulario.getUsuarioCriador().getId();
		if(formulario.getEixos() != null) {
			this.eixos = new ArrayList<>();
			formulario.getEixos().forEach(e -> this.eixos.add(e.getId()));
		}
		if(formulario.getOds() != null) {
			this.ods = new ArrayList<>();
			formulario.getOds().forEach(e -> this.ods.add(e.getId()));
		}
		if(formulario.getTemas() != null) {
			this.temas= new ArrayList<>();
			formulario.getTemas().forEach(e -> this.temas.add(e.getId()));
		}
		
		this.temasNomes = formulario.getTemas().stream().map(AreaInteresse:: getNome).collect(Collectors.toList());
		this.eixosNomes = formulario.getEixos() != null ? formulario.getEixos().stream().map(Eixo:: getNome).collect(Collectors.toList()) : null;
		this.prefeituraCidade = formulario.getUsuarioCriador() != null ? (formulario.getUsuarioCriador().getPrefeitura() != null ? formulario.getUsuarioCriador().getPrefeitura().getCidade() != null ? formulario.getUsuarioCriador().getPrefeitura().getCidade().getNome() : null : null) : null;

	}
	
	public Formulario toEntity(){
		if(this.id != null && this.id <=0 ) {
			this.id = null;
		}
		Formulario formulario = new Formulario(this.id, this.nome,this.descricao, null, LocalDate.now(),this.inicioPeriodoAtividade, this.fimPeriodoAtividade, this.tipoUsuario ,null, null, null, this.apenasAutenticados, this.publicado, this.exibirPaginaPrefeitura, this.link, null);
		if( this.secoes != null ) {
			if(formulario.getSecoes() == null) {
				formulario.setSecoes(new ArrayList<>());
			}
			for (SecaoFormularioDTO sfDto : this.secoes) {
				SecaoFormulario sf = sfDto.toEntity();
				if(sf != null) {
					formulario.getSecoes().add(sf);
				}
			}
		}
		
		return formulario;
	}
	
	
}
