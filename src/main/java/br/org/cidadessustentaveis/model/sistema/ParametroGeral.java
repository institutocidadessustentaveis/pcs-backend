package br.org.cidadessustentaveis.model.sistema;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Leo
 */
@Entity
@Table( name = "parametro_geral")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ParametroGeral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="endereco_smtp", length = 32)
    private String enderecoSMTP;
    
    @Column(name="porta_smtp", length = 8)
    private Integer portaSMTP;
    
    @Column(name="usuario_smtp", length = 32)
    private String usuarioSMTP;
    
    @Column(name="senha_smtp", length = 32)
    private String senhaSMTP;
    
    @Column(name="usuario_apelido_smtp", length = 32)
    private String usuarioApelidoSMTP;
    
    @Column(name="conexao_segura_smtp")
    private Boolean conexaoSeguraSMTP;
    
    @Column (name="email_sugestao_boa_pratica")
    private String emailSugestaoBoaPratica;
    
    public ParametroGeral(Long id, String emailSugestaoBoaPratica) {
    	this.id = id;
    	this.emailSugestaoBoaPratica = emailSugestaoBoaPratica;
    }
}

