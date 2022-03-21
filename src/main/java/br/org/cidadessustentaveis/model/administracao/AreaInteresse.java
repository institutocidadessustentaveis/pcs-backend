package br.org.cidadessustentaveis.model.administracao;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="area_interesse")
@Data
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(ListenerAuditoria.class)
public class AreaInteresse {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String nome;
	
}
