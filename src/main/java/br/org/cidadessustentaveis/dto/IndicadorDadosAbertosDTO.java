package br.org.cidadessustentaveis.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

import br.org.cidadessustentaveis.util.NumeroUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class IndicadorDadosAbertosDTO {

    private Long codigoIbge;

    private String nomeCidade;

    private String siglaEstado;

    private String nomeEixo;

    private Long idIndicador;

    private String nomeIndicador;

    private String formulaResultado;

    private String descricaoMetaOds;

    private int numeroOds;

    private String nomeOds;

    private String descricaoIndicador;
    
    private Short ano;

    private String valor;

    private String justificativa;

    private String nomeEstado;

    private String resultado;
    
    private String valorTexto;

    public List<Object> convertObjectToPropertiesList() {
        List<Object> row = new LinkedList<>();

        row.add(codigoIbge);
        row.add(nomeCidade);
        row.add(siglaEstado);
        row.add(nomeEstado);
        row.add(nomeEixo);
        row.add(idIndicador);
        row.add(nomeIndicador);
        row.add(formulaResultado);
        row.add(descricaoMetaOds);
        row.add(numeroOds);
        row.add(nomeOds);
        row.add(descricaoIndicador);
        row.add(ano);
        row.add(valor);
        row.add(justificativa);

        return row;
    }

    public String[] convertObjectToPropertiesArray() {
        String[] row = new String[15];

        row[0] = codigoIbge + "";
        row[1] = nomeCidade;
        row[2] = siglaEstado;
        row[3] = nomeEstado;
        row[4] = nomeEixo;
        row[5] = idIndicador + "";
        row[6] = nomeIndicador;
        row[7] = formulaResultado;
        row[8] = descricaoMetaOds;
        row[9] = numeroOds + "";
        row[10] = nomeOds;
        row[11] = descricaoIndicador;
        row[12] = ano + "";

        if(NumeroUtil.isANumber(valor)) {
            Double valorDouble = Double.parseDouble(valor);
            row[13] = new BigDecimal(valorDouble)
                            .setScale(2, RoundingMode.DOWN)
                            .toPlainString();
        } else {
            row[13] = valor;
        }

        row[14] = justificativa;

        return row;
    }

}
