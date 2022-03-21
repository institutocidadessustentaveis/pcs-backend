package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "carta_compromisso")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(ListenerAuditoria.class)
public class CartaCompromisso implements Serializable {

	private static final long serialVersionUID = 3835902178440962187L;

	@Id
	@GeneratedValue(generator = "carta_compromisso_id_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "carta_compromisso_id_seq", sequenceName = "carta_compromisso_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;

	@Column(name = "nome_arquivo")
	private String nomeArquivo;
	
	private String extensao;
	
	private String arquivo;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="prefeitura", nullable = false)
	@JsonBackReference
	private Prefeitura prefeitura;
  
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : Integer.parseInt(id+"") * prime);
		return result;
	}
}
