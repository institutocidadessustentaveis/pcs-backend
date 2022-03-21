package br.org.cidadessustentaveis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class SeoDTO {
	private Long id;
	private String title;
	private String description;
	private String image;
	private String slug;
	private String site;
	private String url;
}
