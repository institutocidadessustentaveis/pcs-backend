package br.org.cidadessustentaveis.model.sistema;

import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.noticias.Noticia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "historico_busca")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class HistoricoBusca {

    @Id
    @GeneratedValue(generator = "historico_busca_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "historico_busca_id_seq",
                        sequenceName = "historico_busca_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "palavra_chave", nullable = false)
    private String palavraChave;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario", nullable = true)
    private Usuario usuario;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ip_lookup", nullable = true)
    private IPLookup ipLookup;

    @Column(name = "ip", nullable = true)
    private String ip;

    @Column(name="data_hora_busca")
    private LocalDateTime dataHoraBusca;

}
