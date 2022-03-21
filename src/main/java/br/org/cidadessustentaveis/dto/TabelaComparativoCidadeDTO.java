package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.wololo.geojson.Feature;

import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.administracao.SubDivisao;
import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class TabelaComparativoCidadeDTO implements Serializable{

	private static final long serialVersionUID = -2522519978361722589L;
	
	private List<Mandato2DTO> listaMandatos;
	
}
