package br.org.cidadessustentaveis.model.planjementoIntegrado;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.enums.TipoExportacaoHitoricoExportacaoCatalogoShape;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historico_uso_shape")
@Getter @Setter
@EntityListeners(ListenerAuditoria.class)
@NoArgsConstructor @AllArgsConstructor
public class HistoricoUsoShape {

    @Id
    @GeneratedValue(generator = "historico_uso_shape_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "historico_uso_shape_id_seq",
                        sequenceName = "historico_uso_shape_id_seq", allocationSize = 1)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cidade", nullable = true)
    private Cidade cidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shape", nullable = false)
    private ShapeFile shape;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoUsoShape tipo;

    @Column(name="data_hora_acesso")
    private LocalDateTime dataHoraAcesso;
    
    @Enumerated(EnumType.STRING)
    @Column(name="tipo_arquivo")
    private TipoExportacaoHitoricoExportacaoCatalogoShape tipoArquivo;

}
