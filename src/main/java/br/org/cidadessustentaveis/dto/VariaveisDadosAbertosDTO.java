package br.org.cidadessustentaveis.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariaveisDadosAbertosDTO {

    private Long codigoIbge;

    private String nomeCidade;

    private Long id;

    private String uf;

    private String estado;

    private String tipo;

    private String unidade;

    private String nome;

    private Short ano;

    private Double valor;

    private String observacao;

    private String fonte;
    private String fonteMigracao;
    

    private String valor_texto;

    public List<Object> convertObjectToPropertiesList() {
        List<Object> row = new LinkedList<>();

        /*
            ATENÇÃO: MANTENHA A ORDEM DE ADIÇÃO DOS ITENS NA LISTA. A ORDEM É IMPORTANTE NA GERAÇÃO DOS XMLs.
            CASO SEJA NECESSÁRIO ADICIONAR MAIS COLUNAS, COLOQUE-AS NO FIM DA LISTA.
        */

        row.add(codigoIbge);
        row.add(nomeCidade);
        row.add(id);
        row.add(uf);
        row.add(estado);
        row.add(tipo);
        row.add(unidade);
        row.add(nome);
        row.add(ano);

        if(valor_texto != null) {
            row.add(valor_texto);
        } else if(valor != null) {
            row.add(new BigDecimal(valor).setScale(2, RoundingMode.DOWN).toPlainString());
        }     
        

        row.add(observacao);
        row.add(fonte);

        return row;
    }

    public String[] convertObjectToPropertiesArray() {
        String[] row = new String[12];

        row[0] = codigoIbge + "";
        row[1] = nomeCidade;
        row[2] = id + "";
        row[3] = uf;
        row[4] = estado;
        row[5] = tipo;
        row[6] = unidade;
        row[7] = nome;
        row[8] = ano + "";

        if(valor_texto != null) {
            row[9] = valor_texto;
        } else if(valor != null) {
            row[9] = new BigDecimal(valor).setScale(2, RoundingMode.DOWN).toPlainString();
        }    
        if(valor != null) {
            row[9] = new BigDecimal(valor).setScale(2, RoundingMode.DOWN).toPlainString();
        }

        row[10] = observacao;
        row[11] = fonteMigracao != null ? fonteMigracao : (fonte != null ? fonte : "") ;

        return row;
    }

}
