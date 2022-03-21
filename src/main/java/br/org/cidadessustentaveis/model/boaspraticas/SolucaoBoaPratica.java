package br.org.cidadessustentaveis.model.boaspraticas;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "boa_pratica_solucao")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SolucaoBoaPratica {
	
	@Id
    @GeneratedValue(generator = "boa_pratica_solucao_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "boa_pratica_solucao_id_seq",
                        sequenceName = "boa_pratica_solucao_id_seq", allocationSize = 1)
	private Long id;
	
	@Column(name = "_id")
	private String _id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_boa_pratica")
    @JsonBackReference
    private BoaPratica boaPratica;
	
	@Column(name = "tema")
	private String tema;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "caracterizacao_solucao")
	private String caracterizacaoSolucao;
}
