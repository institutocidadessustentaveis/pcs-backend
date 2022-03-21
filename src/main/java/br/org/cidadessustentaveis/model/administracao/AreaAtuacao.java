package br.org.cidadessustentaveis.model.administracao;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.Data;

@Entity(name="area_atuacao")
@Data
@EntityListeners(ListenerAuditoria.class)
public class AreaAtuacao {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String nome;		
	
}
