package br.org.cidadessustentaveis.model.sistema;

import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.noticias.Noticia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;

@Entity
@Table(name = "historico_acesso_noticia")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class HistoricoAcessoNoticia {

    @Id
    @GeneratedValue(generator = "historico_acesso_noticia_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "historico_acesso_noticia_id_seq",
                        sequenceName = "historico_acesso_noticia_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "palavra_chave")
    private String palavraChave;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noticia")
    @JsonBackReference()
    private Noticia noticia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario")
    private Usuario usuario;

    @Column(name = "ip")
    private String ip;

    @Column(name="data_hora_acesso")
    private LocalDateTime dataHoraAcesso;

}
