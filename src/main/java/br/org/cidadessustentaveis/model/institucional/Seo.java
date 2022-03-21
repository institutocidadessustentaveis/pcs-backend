package br.org.cidadessustentaveis.model.institucional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Table(name="seo")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@Data
public class Seo {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String description;
	private String image;
	private String slug;
	private String site;
	private String url;
	
}
