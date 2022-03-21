package br.org.cidadessustentaveis.model.participacaoCidada;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.dto.FormularioDTO;
import br.org.cidadessustentaveis.dto.SecaoFormularioDTO;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(ListenerAuditoria.class)
public class Formulario implements Serializable{
	private static final long serialVersionUID = -2637712730152432378L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String descricao;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="usuario_criador", nullable = false)
	private Usuario usuarioCriador;
	private LocalDate dataCriacao;
	private LocalDate inicioPeriodoAtividade;
	private LocalDate fimPeriodoAtividade;
	private String tipoUsuario;// Prefeitura, Cidadão, Todos
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "formulario_eixo",
				joinColumns = @JoinColumn(name = "formulario", referencedColumnName = "id"),
				inverseJoinColumns = @JoinColumn(name = "eixo", referencedColumnName = "id"))
	private List<Eixo> eixos;
	

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "formulario_ods",
				joinColumns = @JoinColumn(name = "formulario", referencedColumnName = "id"),
				inverseJoinColumns = @JoinColumn(name = "ods", referencedColumnName = "id"))
	private List<ObjetivoDesenvolvimentoSustentavel> ods;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "formulario_area_interesse",
				joinColumns = @JoinColumn(name = "formulario", referencedColumnName = "id"),
				inverseJoinColumns = @JoinColumn(name = "area_interesse", referencedColumnName = "id"))
	private List<AreaInteresse> temas;
	private Boolean apenasAutenticados;
	private Boolean publicado;
	private Boolean exibirPaginaPrefeitura;
	private String link;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="formulario")
	private List<SecaoFormulario> secoes;
	
	public void atualizar(FormularioDTO dto) {
		this.setNome(dto.getNome());
		this.setDescricao(dto.getDescricao());
		this.setInicioPeriodoAtividade(dto.getInicioPeriodoAtividade());
		this.setFimPeriodoAtividade(dto.getFimPeriodoAtividade());
		this.setTipoUsuario(dto.getTipoUsuario());
		this.setApenasAutenticados(dto.getApenasAutenticados());
		this.setExibirPaginaPrefeitura(dto.getExibirPaginaPrefeitura());
		this.setLink(dto.getLink());
		this.setPublicado(dto.getPublicado());
		
		if(dto.getSecoes() != null) {
			dto.getSecoes().forEach(secaoDTO -> {
				if(secaoDTO.getId() == null || secaoDTO.getId() <= 0) {
					SecaoFormulario novaSecao = secaoDTO.toEntity();
					novaSecao.setFormulario(this);
					getSecoes().add(novaSecao);
				} else {
					List<SecaoFormulario> listaSecao = getSecoes().stream().filter(fs -> fs.getId() == secaoDTO.getId()).collect(Collectors.toList());
					if(!listaSecao.isEmpty()) {
						SecaoFormulario secao =  listaSecao.get(0);
						secao.atualizar(secaoDTO);
					}
				}
			});
			for(int i = 0; i < secoes.size(); i++ ) {
				SecaoFormulario secao = secoes.get(i);
				List<SecaoFormularioDTO> listaSecao =dto.getSecoes().stream().filter(f -> f.getId() == secao.getId()).collect(Collectors.toList());
				if(listaSecao != null && listaSecao.isEmpty()) {
					secoes.remove(i);
					i--;
				}
			}
		}
		
	}
	
}
