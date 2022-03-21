package br.org.cidadessustentaveis.dto;

import lombok.Data;

@Data
public class TreeMapChartDTO  implements Comparable<TreeMapChartDTO> {
	private String name;
	private Double value;
	
	public TreeMapChartDTO(String nome, Double valor) {
		this.name = nome;
		this.value = valor;
	}

	public TreeMapChartDTO() {
		// TODO Auto-generated constructor stub
	}
	
	
	public int compareTo(TreeMapChartDTO outroTreeMap) {
		if (this.value < outroTreeMap.value) {
            return 1;
        }
        if (this.value > outroTreeMap.value) {
            return -1;
        }
        return 0;
	}
}
