package br.org.cidadessustentaveis.model.administracao;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.Data;

@Entity(name="responsavel")
@Data
@EntityListeners(ListenerAuditoria.class)
public class Responsavel {

	@Id @GeneratedValue(generator = "responsavel_id_seq")
	@SequenceGenerator(name = "reponsavel_id_seq", sequenceName = "responsavel_id_seq", allocationSize = 1)
    private Long id;
	
	private String nome;
	
	private String email;
	
	private String telefoneFixo;
	
	private String celular;
	
	private String cargo;
	
	private Perfil perfil_id;
	
	private Boolean recebeEmail;
	
	
}
