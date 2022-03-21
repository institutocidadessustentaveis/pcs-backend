package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="DownloadsExportacoes")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(ListenerAuditoria.class)
public class DownloadsExportacoes implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "downloads_exportacoes_id_seq")
	@SequenceGenerator(name = "downloads_exportacoes_id_seq", sequenceName = "downloads_exportacoes_id_seq", allocationSize = 1)
	@Column(name="id",nullable=false)
	private Long id;
	
	@Column(name="nomeusuario")
	private String nomeUsuario;
	
	@Column(name="datahora")
	private LocalDateTime dataHora;
	
	@Column(name="nomearquivo")
	private String nomeArquivo;	
	
	@Transient
	private String usuarioLogado;
	
	public DownloadsExportacoes(Long id, String nomeArquivo) {
		this.id = id;
		this.nomeArquivo = nomeArquivo;
	}
}
