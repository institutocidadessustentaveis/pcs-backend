package br.org.cidadessustentaveis.model.participacaoCidada;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PropostaMunicipio implements Serializable {
    private static final long serialVersionUID = -6108755107689960353L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne 
    @JoinColumn(name = "prefeitura")
    private Prefeitura prefeitura;
    private String descricao;
    @ManyToOne
    @JoinColumn(name = "usuario")
    private Usuario usuario;
    private LocalDateTime dataEnvio;
}