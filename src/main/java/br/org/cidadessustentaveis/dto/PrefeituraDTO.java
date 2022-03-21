package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.administracao.CartaCompromisso;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class PrefeituraDTO {

	private Long id;
	
	@NotNull(message = "Obrigatório o preenchimento do nome do Prefeito.")
	private String nome;
	
	@NotNull(message = "Obrigatório preenchimento do e-mail.")
	private String email;
	
	@NotNull(message = "Obrigatório preenchimento do telefone.")
	private String telefone;
	
	@NotNull(message = "Obrigatório preenchimento do cargo (Prefeito ou Prefeita).")
	private String cargo;
	
	@NotNull(message = "Obrigatório preenchimento da cidade.")
	private ExibirCidadeProvinciaEstadoDTO cidade;
	
//	@NotNull(message = "Obrigatório o preenchimento do campo: Deseja receber boletins e informações do Programa Cidades Sustentáveis.")
//	private boolean naoDesejoReceberInformacoesPcs;
	
	@NotNull(message = "Obrigatório o upload da Carta-Compromisso.")
	private List<CartaCompromisso> cartaCompromisso;
	
	@NotNull(message = "Obrigatório preenchimento do partido político.")
	private PartidoPoliticoDTO partidoPolitico;
	
	private Boolean signataria;
	
    private LocalDate inicioMandato;
    
    private LocalDate fimMandato;
	
	public PrefeituraDTO(Prefeitura prefeitura) {
		super();
		if(prefeitura!= null) {
			this.id = prefeitura.getId();
			this.nome = prefeitura.getNome();
			this.email = prefeitura.getEmail();
			this.telefone = prefeitura.getTelefone();
			this.cargo = prefeitura.getCargo();
			this.cidade = new ExibirCidadeProvinciaEstadoDTO(prefeitura.getCidade());
			this.partidoPolitico = new PartidoPoliticoDTO(prefeitura.getPartidoPolitico());
			this.signataria = prefeitura.getSignataria();
			this.inicioMandato = prefeitura.getInicioMandato();
			this.fimMandato = prefeitura.getFimMandato();
		}
	}
	
	public Prefeitura toPrefeitura() {
		return new Prefeitura (this.getId(), this.getNome(), this.getEmail(), this.getTelefone(), this.getCargo(),
			    null, this.getCartaCompromisso(), null, this.getSignataria(), null, this.getInicioMandato(), this.getFimMandato(), null);
	}

	public Prefeitura toEntityInsert() {
	  return new Prefeitura (null, this.getNome(), this.getEmail(), this.getTelefone(), this.getCargo(),
	    null, this.getCartaCompromisso(), null, this.getSignataria(), null, this.getInicioMandato(), this.getFimMandato(), null);
	}
	
	public Prefeitura toEntityUpdate(Prefeitura prefeitura) {
		prefeitura.setNome(this.nome);
		prefeitura.setEmail(this.email);
		prefeitura.setTelefone(this.telefone);
		prefeitura.setCargo(this.cargo);
		prefeitura.setCartaCompromisso(this.cartaCompromisso);
		return prefeitura;
	}
	
	// Utilizado no metodo buscarLogin
	public PrefeituraDTO(Long id, Long idCidade, LocalDate inicioMandato, LocalDate fimMandato) {
		this.id = id;
		ExibirCidadeProvinciaEstadoDTO  exibirCidadeProvinciaEstadoDTO = new ExibirCidadeProvinciaEstadoDTO();
		exibirCidadeProvinciaEstadoDTO.setId(idCidade);
		this.cidade = exibirCidadeProvinciaEstadoDTO;
		this.inicioMandato = inicioMandato;
		this.fimMandato = fimMandato;
	}
	
	public PrefeituraDTO(Long id, String nomeCidade, String cargo, String nome, String email, String telefone, List<CartaCompromisso> cartasCompromisso) {
		this.id = id;
		this.cargo = cargo;
		this.nome = nome;
		this.email = email;
		this.telefone = telefone;
		
		ExibirCidadeProvinciaEstadoDTO cidade = new ExibirCidadeProvinciaEstadoDTO();
		cidade.setNome(nomeCidade);
		this.cidade = cidade;
		
		this.cartaCompromisso = new ArrayList<>(cartasCompromisso);
	}
	
	public PrefeituraDTO(Long id, LocalDate inicioMandato, LocalDate fimMandato) {
		this.id = id;
		this.inicioMandato = inicioMandato;
		this.fimMandato = fimMandato;
	}
}