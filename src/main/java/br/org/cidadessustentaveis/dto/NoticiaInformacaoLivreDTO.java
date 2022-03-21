package br.org.cidadessustentaveis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class NoticiaInformacaoLivreDTO {

    private Long idNoticia;
    
    private String linkNoticia;
    
    private String tituloNoticia;
    
    private String tituloInformacaoLivre;
    
    private String linkInformacaoLivre;

}
