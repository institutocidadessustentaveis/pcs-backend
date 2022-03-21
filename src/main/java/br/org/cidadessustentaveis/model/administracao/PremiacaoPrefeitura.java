package br.org.cidadessustentaveis.model.administracao;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;	
@Entity	
@Builder	
@Table(name = "premiacao_prefeitura")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PremiacaoPrefeitura implements Serializable {	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "premiacao_prefeitura_id_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "premiacao_prefeitura_id_seq", sequenceName = "premiacao_prefeitura_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="id_prefeitura", nullable = false)
	@JsonBackReference
	private Prefeitura prefeitura;	
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="id_premiacao", nullable = false)
	@JsonBackReference
	private Premiacao premiacao;
	
	
	public PremiacaoPrefeitura(Long id,String status) {
		super();
		this.id = id;
	}
	
	public PremiacaoPrefeitura (Long id, String status, Prefeitura prefeitura, Premiacao premiacao) {
		  super();
		  this.id = id;
		  this.prefeitura = prefeitura;
		  this.premiacao = premiacao;
		  
		}

}
