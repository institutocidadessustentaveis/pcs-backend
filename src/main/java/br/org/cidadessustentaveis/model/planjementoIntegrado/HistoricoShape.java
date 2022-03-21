package br.org.cidadessustentaveis.model.planjementoIntegrado;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historico_shape")
@Getter @Setter
@EntityListeners(ListenerAuditoria.class)
@NoArgsConstructor @AllArgsConstructor
public class HistoricoShape {

    @Id
    @GeneratedValue(generator = "historico_shape_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "historico_shape_id_seq", sequenceName = "historico_shape_id_seq", allocationSize = 1)
    @Column(nullable = false)
    private Long id;

    @Column(name="nome_arquivo")
    private String nomeArquivo;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="shape", nullable = false)
    private ShapeFile shape;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tema", nullable = false)
    private TemaGeoespacial tema;

    @Column(name="data_hora_criacao")
    private LocalDateTime dataCriacao;

    @Column(name="data_hora_edicao")
    private LocalDateTime dataEdicao;
    
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="prefeitura")
    private Prefeitura prefeitura;

}
