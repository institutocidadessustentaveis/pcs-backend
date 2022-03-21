package br.org.cidadessustentaveis.model.administracao;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.enums.RedeSocial;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "redes_sociais_rodape")
@Getter @Setter
@EntityListeners(ListenerAuditoria.class)
@NoArgsConstructor @AllArgsConstructor
public class RedeSocialRodape {

    @Id
    @GeneratedValue(generator = "redes_sociais_rodape_id_seq")
    @SequenceGenerator(name = "redes_sociais_rodape_id_seq",
                        sequenceName = "redes_sociais_rodape_id_seq", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private RedeSocial tipo;

    @Column(name = "url_perfil", nullable = false)
    private String urlPerfil;

    @Column(name = "ordem", nullable = false)
    private Integer ordem;

}
