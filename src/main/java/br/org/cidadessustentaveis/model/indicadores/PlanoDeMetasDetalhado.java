package br.org.cidadessustentaveis.model.indicadores;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
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
@Table(name="plano_de_metas_detalhado") 
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(ListenerAuditoria.class)
public class PlanoDeMetasDetalhado implements Cloneable, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "plano_de_metas_detalhado_id_seq")
	@SequenceGenerator(name = "plano_de_metas_detalhado_id_seq", sequenceName = "plano_de_metas_detalhado_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="indicador")
	@JsonBackReference
	private Indicador indicador;
	
	@Column(name="meta_anual_primeiro_ano")
	private String metaAnualPrimeiroAno;
	
	@Column(name="meta_anual_segundo_ano")
	private String metaAnualSegundoAno;
	
	@Column(name="meta_anual_terceiro_ano")
	private String metaAnualTerceiroAno;
	
	@Column(name="meta_anual_quarto_ano")
	private String metaAnualQuartoAno;
	
	@Column(name="orcamento_previsto")
	private Double orcamentoPrevisto;
	
	@Column(name="orcamento_executado")
	private Double orcamentoExecutado;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="plano_de_metas")
	@JsonBackReference
	private PlanoDeMetas planoDeMetas;
	
	@Column(name="plano_para_alcancar_proposta")
	private String planoParaAlcancarProposta;
	
	//Criado para copiar o objeto e não a referencia
	public PlanoDeMetasDetalhado clone() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (PlanoDeMetasDetalhado) ois.readObject();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
}
