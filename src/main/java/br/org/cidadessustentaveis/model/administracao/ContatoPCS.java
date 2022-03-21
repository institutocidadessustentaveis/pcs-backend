package br.org.cidadessustentaveis.model.administracao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "contato_pcs")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ContatoPCS {

    @Id
    @GeneratedValue(generator = "contato_pcs_id_seq")
    @SequenceGenerator(name = "contato_pcs_id_seq", sequenceName = "contato_pcs_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "endereco", nullable = false)
    private String endereco;

    @Column(name = "cidade", nullable = false)
    private String cidade;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @Column(name = "cep", nullable = false)
    private String cep;

    @Column(name = "link", nullable = false)
    private String link;

    @Column(name = "data", nullable = false)
    private Date data;

}
