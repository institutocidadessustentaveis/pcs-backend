package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.eventos.Evento;
import br.org.cidadessustentaveis.model.noticias.Noticia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class EventoDTO {

	private Long id;
	    
    @NotNull
    @NotEmpty
	private String tipo;
    
    @NotNull
    @NotEmpty
	private String nome;
    
    @NotNull
    private LocalTime horario;
	
    @NotNull
    @NotEmpty
	private String descricao;

    @NotNull
	private LocalDate data;
    
    @NotNull
    private LocalDateTime dataHorario;
    
    @NotNull
    @NotEmpty
	private String organizador;
	
	private List<Long> temas;
	
    @NotNull
	private Long pais;
    
    private String paisNome;
	
    @NotNull
	private Long estado;
    
    private String estadoNome;
	
    @NotNull
	private Long cidade;
    
    private String cidadeNome;
	
	private List<Long> eixos;
	
	private List<String> eixosNome;
	
    @NotNull
	private Long ods;
	
    @NotNull
	private boolean online;
	
    @NotNull
	private String endereco;
	
	private String site;
	
    @NotNull
	private Double latitude;
	
    @NotNull
	private Double longitude;	
	
    @NotNull
	private boolean publicado;
	
    @NotNull
	private boolean externo;
	
	private String linkExterno;
	
	private List<String> temasNomes;
	
	private String enderecoExibicao;
	
	private String odsNomeString;
	
	private List<Long> noticiasRelacionadas;
	
	private LocalDate dataInicio;
	
	private LocalDate dataFim;
	
	public Evento toEntityInsert(Pais pais, Cidade cidade,  ProvinciaEstado estado, List<Eixo> eixos, ObjetivoDesenvolvimentoSustentavel ods) {
		return Evento.builder()
			.tipo(this.tipo)
			.nome(this.nome)
			.horario(this.horario)
			.descricao(this.descricao)
			.dataEvento(this.data)
			.organizador(this.organizador)
			.pais(pais)
			.provinciaEstado(estado)
			.cidade(cidade)
			.eixos(eixos)
			.ods(ods)
			.online(this.online).build();
	}
	
	public EventoDTO(Evento evento) {
		this.id = evento.getId();
		this.tipo = evento.getTipo();
		this.nome = evento.getNome();
		this.horario = evento.getHorario();
		this.descricao = evento.getDescricao();
		this.data = evento.getDataEvento();
		this.organizador = evento.getOrganizador();
		this.temas = evento.getTemas().stream().map(AreaInteresse:: getId).collect(Collectors.toList());
		this.noticiasRelacionadas = evento.getNoticiasRelacionadas().stream().map(Noticia:: getId).collect(Collectors.toList());
		this.pais = evento.getPais() != null ? evento.getPais().getId() : null;
		this.estado = evento.getProvinciaEstado() != null ? evento.getProvinciaEstado().getId() : null;
		this.cidade = evento.getCidade() != null ? evento.getCidade().getId() : null;
		this.eixos = evento.getEixos().stream().map(Eixo:: getId).collect(Collectors.toList());
		this.ods = evento.getOds() != null ? evento.getOds().getId() : null;
		this.online = evento.isOnline();
		this.endereco = evento.getEndereco();
		this.site = evento.getSite();
		this.latitude = evento.getLatitude();
		this.longitude = evento.getLongitude();
		this.publicado = evento.isPublicado();
		this.externo = evento.isExterno();
		this.linkExterno = evento.getLinkExterno();
		
		this.temasNomes = evento.getTemas().stream().map(AreaInteresse:: getNome).collect(Collectors.toList());
		this.eixosNome = evento.getEixos() != null ? evento.getEixos().stream().map(Eixo:: getNome).collect(Collectors.toList()) : null;
		
		Cidade cidadeAux = evento.getCidade();
		String cidadeNome = cidadeAux != null ? cidadeAux.getNome() : null;
		
		ProvinciaEstado estadoAux = cidadeAux.getProvinciaEstado();
		String estadoNome = estadoAux != null ? estadoAux.getNome() : null;
		Pais paisAux = evento.getPais();
		String paisNome = paisAux != null ? paisAux.getNome() : null;
		if (this.endereco != null) {			
			this.enderecoExibicao = this.endereco + ", "+ cidadeNome +", "+ estadoNome+ ", "+ paisNome;
		}
		else {
			this.enderecoExibicao = cidadeNome +", "+ estadoNome+ ", "+ paisNome;
		}
		
		ObjetivoDesenvolvimentoSustentavel odsAux = evento.getOds();
		this.odsNomeString = odsAux != null ? odsAux.getTitulo() : null;
	}
	
	//Para a query buscarEventosToList()
	public EventoDTO(Long id, String tipo, String nome, LocalDate data, LocalTime horario, boolean publicado, String endereco, String descricao, String site, Double latitude, Double longitude) {
		this.id = id;
		this.tipo = tipo;
		this.nome = nome;
		this.data = data;
		this.horario = horario;
		if (this.data != null && this.horario != null) {
			this.dataHorario = LocalDateTime.of(this.data, this.horario);
		}
		this.publicado = publicado;
		this.endereco = endereco;
		this.descricao = descricao;
		this.site = site;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	
	public Evento toEntityInsert(EventoDTO evento) {
		return new Evento(null, tipo, nome, horario, descricao, data, organizador, null, null, null, null, null, null, online, endereco, site, latitude, longitude, publicado, externo, linkExterno, null, null);
	}
	
	public Evento toEntityUpdate(Evento evento) {
		//Não incluídos: tema, país, Estado, cidade, eixo, ods (os que são chave de outra tabela)
		evento.setTipo(this.tipo);
		evento.setNome(this.nome);
		evento.setHorario(this.horario);
		evento.setDescricao(this.descricao);
		evento.setDataEvento(this.data);
		evento.setOrganizador(this.organizador);
		evento.setOnline(this.online);
		evento.setEndereco(this.endereco);
		evento.setSite(this.site);
		evento.setLatitude(this.latitude);
		evento.setLongitude(this.longitude);
		evento.setPublicado(this.publicado);
		evento.setExterno(this.externo);
		evento.setLinkExterno(this.linkExterno);
		return evento;
	}
	
	public EventoDTO(Long id, String tipo, String nome) {
		this.id = id;
		this.tipo = tipo;
		this.nome = nome;
	}
	
	public EventoDTO(Long id, LocalDate dataEvento, String tipo,
			 String nome,  String descricao, String organizador,
			Double latitude, Double longitude,
			 boolean publicado, LocalTime horario, String site, String endereco, String linkExterno) {
		super();
		this.id = id;
		this.data = dataEvento;
		this.tipo = tipo;
		this.nome = nome;
		this.descricao = descricao;
		this.organizador = organizador;
		this.latitude = latitude;
		this.longitude = longitude;
		this.publicado = publicado;
		this.horario = horario;
		this.site = site;
		this.endereco = endereco;
		this.dataHorario = LocalDateTime.of(this.data, this.horario);
		this.linkExterno = linkExterno;
	}
}
