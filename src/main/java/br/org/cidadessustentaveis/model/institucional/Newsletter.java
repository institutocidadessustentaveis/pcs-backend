package br.org.cidadessustentaveis.model.institucional;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "newsletter")
@Data @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Newsletter {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "data_cadastro")
    private Date data;

    @Column(name = "ativo")
    private boolean ativo;

}
