package br.org.cidadessustentaveis.dto;


import java.util.Date;

import br.org.cidadessustentaveis.model.administracao.Cidade;
import lombok.Data;

@Data
public class FiltroIndicadoresDTO {

	private Long id;
	
	private String nome;
	
	private String descricao;
	
	private Cidade cidade;
	
	private String valor;
	
	private Integer popDe;
	
	private Integer popAte;
	
	private Date dataPreenchimento;
	
	private EixoDTO eixo;
	
	private ObjetivoDesenvolvimentoSustentavelDTO ods;
	
	private VariavelDTO variavel;
	
//	public FiltroIndicadoresDTO(Long id, String nome, String descricao, String cidade, Long valor, int popDe, int popAte, Date dataPreenchimento, Eixo eixo, ObjetivoDesenvolvimentoSustentavel ods, MetaObjetivoDesenvolvimentoSustentavel metaOds) {
//		this.id = id;
//		this.nome = nome;
//		this.descricao = descricao;
//		this.cidade = cidade;
//		this.valor = valor;
//		this.popDe = popDe;
//		this.popAte = popAte;
//		this.dataPreenchimento = dataPreenchimento;
//		this.eixo = eixo;
//		this.ods = ods;
//		this.metaOds = metaOds;
//	}
}
