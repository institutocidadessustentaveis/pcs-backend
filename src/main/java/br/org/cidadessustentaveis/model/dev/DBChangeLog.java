package br.org.cidadessustentaveis.model.dev;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
@Data
@Entity(name="databasechangelog")
public class DBChangeLog {
	@Id
	private String id;
	private String author;
	private String filename;
	private String dateexecuted;
	private String orderexecuted;
	private String exectype;
	private String md5sum;
	private String description;
	private String comments;
	private String tag;
	private String liquibase;
	private String contexts;
	private String labels;
	private String deployment_id;
}
