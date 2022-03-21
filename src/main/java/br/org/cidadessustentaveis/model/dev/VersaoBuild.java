package br.org.cidadessustentaveis.model.dev;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.org.cidadessustentaveis.model.enums.TipoBuild;
import lombok.Data;
@Data
@Entity(name="versao_build")
public class VersaoBuild {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;
	
	private String versao;
	
	@Column(name="hash_git")
	private String hashGit;
	
	@Column(name="data_build")
	private LocalDateTime dataBuild;
	
	@Enumerated(EnumType.STRING)
	@Column(name="tipo_build")
	private TipoBuild tipoBuild;
}
