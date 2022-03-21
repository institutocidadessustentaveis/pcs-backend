package br.org.cidadessustentaveis.model.administracao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;

@Entity
@EntityListeners(ListenerAuditoria.class)
@Table(name = "link_rodape")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class LinkRodape {

    @Id
    @GeneratedValue(generator = "link_rodape_id_seq")
    @SequenceGenerator(name = "link_rodape_id_seq", sequenceName = "link_rodape_id_seq", allocationSize = 1)
    @Column(nullable=false)
    private Long id;

    @Column(name = "ordem", nullable = false)
    private Integer ordem;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "abrir_nova_janela", nullable = false)
    private Boolean abrirNovaJanela;

}
