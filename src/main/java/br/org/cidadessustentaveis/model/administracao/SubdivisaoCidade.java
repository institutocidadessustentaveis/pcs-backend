package br.org.cidadessustentaveis.model.administracao;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EntityListeners(ListenerAuditoria.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SubdivisaoCidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="cidade")
    private Cidade cidade;
    
    private String nome;	
    
    @ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="subdivisao_pai")
    private SubdivisaoCidade subdivisaoPai;
    
    @ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="tipo_subdivisao")
    private TipoSubdivisao tipoSubdivisao;
}
