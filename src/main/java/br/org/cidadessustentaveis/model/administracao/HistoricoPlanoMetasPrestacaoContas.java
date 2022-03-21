package br.org.cidadessustentaveis.model.administracao;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.enums.SimNao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="historico_upload_plano_e_prestacao")
@Data
@EntityListeners(ListenerAuditoria.class)
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoPlanoMetasPrestacaoContas {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name="plano_metas")
	private String planoMetas = SimNao.NAO.getDescricao();
	
	@Column(name="prestacao_contas")
	private String prestacaoContas = SimNao.NAO.getDescricao();
	
	@Column(name="data_hora_plano_metas")
	private LocalDateTime dataHoraPlanoMetas;
	
	@Column(name="data_hora_prestacao_contas")
	private LocalDateTime dataHoraPrestacaoContas;
	
	@Column(name="mandato")
	private String mandato;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prefeitura")
	@JsonBackReference
	private Prefeitura prefeitura;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario")
	@JsonBackReference
	private Usuario usuario;

	public HistoricoPlanoMetasPrestacaoContas(String nomeEstado, String nomeCidade, LocalDateTime inicioMandato, 
			LocalDateTime fimMandato, String planoMetas, LocalDateTime dataHoraPlanoMetas, String prestacaoContas, 
			LocalDateTime dataHoraPrestacaoContas) {
		this.planoMetas = planoMetas;
		this.prestacaoContas = prestacaoContas;
		this.dataHoraPlanoMetas = dataHoraPlanoMetas;
		this.dataHoraPrestacaoContas = dataHoraPrestacaoContas;
		this.mandato = inicioMandato.getYear() + " - " + fimMandato.getYear();
		ProvinciaEstado pe = new ProvinciaEstado();
		pe.setNome(nomeEstado);
		Cidade cidade = new Cidade();
		cidade.setNome(nomeCidade);
		cidade.setProvinciaEstado(pe);
		Prefeitura pref = new Prefeitura();
		prefeitura.setCidade(cidade);
	}
	
	
}
