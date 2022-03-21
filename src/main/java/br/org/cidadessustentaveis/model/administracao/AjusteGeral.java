package br.org.cidadessustentaveis.model.administracao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ajustes_gerais")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AjusteGeral {
	
	@Id
	@GeneratedValue(generator = "ajustes_gerais_id_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ajustes_gerais_id_seq", sequenceName = "ajustes_gerais_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;
	
	@Column(name = "conteudo")
    private String conteudo;
	
	@Column(name = "local_aplicacao")
    private String localAplicacao;

}
