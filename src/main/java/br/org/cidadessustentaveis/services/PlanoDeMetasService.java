package br.org.cidadessustentaveis.services;

import static br.org.cidadessustentaveis.util.VariavelPreenchidaUtil.VALOR_REF_NAO_SE_APLICA_INDICADOR;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.mail.EmailException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.AjusteGeralDTO;
import br.org.cidadessustentaveis.dto.ArquivoDTO;
import br.org.cidadessustentaveis.dto.PlanoDeMetasDTO;
import br.org.cidadessustentaveis.dto.PlanoDeMetasDetalhadoDTO;
import br.org.cidadessustentaveis.dto.PrefeituraPlanoMetasDTO;
import br.org.cidadessustentaveis.dto.UsuarioDTO;
import br.org.cidadessustentaveis.model.administracao.Alerta;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.enums.TipoAlerta;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.model.indicadores.PlanoDeMetas;
import br.org.cidadessustentaveis.model.indicadores.PlanoDeMetasDetalhado;
import br.org.cidadessustentaveis.model.indicadores.PlanoDeMetasDetalhadoHistorico;
import br.org.cidadessustentaveis.model.indicadores.ValorReferencia;
import br.org.cidadessustentaveis.repository.IndicadorPreenchidoRepository;
import br.org.cidadessustentaveis.repository.PlanoDeMetasDetalhadoHistoricoRepository;
import br.org.cidadessustentaveis.repository.PlanoDeMetasDetalhadoRepository;
import br.org.cidadessustentaveis.repository.PlanoDeMetasRepository;
import br.org.cidadessustentaveis.services.exceptions.BusinessLogicErrorException;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.EmailUtil;
import br.org.cidadessustentaveis.util.NumeroUtil;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;

@Service
public class PlanoDeMetasService {

	@Autowired
	private PlanoDeMetasRepository repository;
	
	@Autowired
	private PlanoDeMetasDetalhadoRepository planoDeMetasDetalhadoRepository;
	
	@Autowired
	private PlanoDeMetasDetalhadoHistoricoRepository planoDeMetasDetalhadoHistoricoRepository;

	@Autowired
	private IndicadorService indicadorService;

	@Autowired
	private IndicadorPreenchidoService indicadorPreenchidoService;

	@Autowired
	private PrefeituraService prefeituraService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private RelatorioPlanoDeMetasService relatorioPlanoDeMetasService;
	
	@Autowired
	private IndicadorPreenchidoRepository preenchidoRepository;
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private UsuarioContextUtil usuarioContextUtil;
	
	@Autowired
	private AjusteGeralService ajusteGeralService;
	
	@Autowired
	private EmailUtil emailUtil;
	
	@Autowired
	private CidadeService cidadeService;
	
	@Autowired
	private AlertaService alertaService;
	
	public static final String EMAIL_PLANO_METAS = "EMAIL-ALERTA-PLANO-METAS";
	
	public PlanoDeMetas buscarPorId(Long id) {
		Optional<PlanoDeMetas> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Plano de metas não encontrado!"));
	}
	
	public PlanoDeMetasDetalhado buscarPlanoDetalhadoPorId(Long id) {
		Optional<PlanoDeMetasDetalhado> obj = planoDeMetasDetalhadoRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Plano de metas não encontrado para este indicador!"));
	}
	
	public PlanoDeMetasDTO buscarCarregarPlanoDeMetasPorId(Long idPlanoDeMetas) {
		
		PlanoDeMetas planoRef = buscarPorId(idPlanoDeMetas);
		
		UsuarioDTO usuario = new UsuarioDTO();
		usuario.setNome(planoRef.getUsuario().getNome());
		usuario.setId(planoRef.getUsuario().getId());
		
		PlanoDeMetasDTO planoDeMetasDTO = PlanoDeMetasDTO.builder()
				.id(planoRef.getId())
				.statusPlanoDeMetas(planoRef.getStatusPlanoDeMetas())
				.nomeCidade(planoRef.getPrefeitura().getCidade().getNome())
				.populacao(planoRef.getPrefeitura().getCidade().getPopulacao())
				.idPrefeitura(planoRef.getPrefeitura().getId())
				.nomePrefeito(planoRef.getPrefeitura().getNome())
				.inicioMandato(planoRef.getPrefeitura().getInicioMandato())
				.fimMandato(planoRef.getPrefeitura().getFimMandato())
				.planosDeMetasDetalhados(new ArrayList<>())
				.usuario(usuario)
				.build();
		
		/* Calcula Anos do mandato */
		planoDeMetasDTO.setPrimeiroAnoMandato((short) planoDeMetasDTO.getInicioMandato().getYear());
		planoDeMetasDTO.setSegundoAnoMandato((short) (planoDeMetasDTO.getPrimeiroAnoMandato()+1));
		planoDeMetasDTO.setTerceiroAnoMandato((short) (planoDeMetasDTO.getSegundoAnoMandato()+1));
		planoDeMetasDTO.setQuartoAnoMandato((short) (planoDeMetasDTO.getTerceiroAnoMandato()+1));

		/*Carrega valores preenchidos de cada indicador*/
		for (PlanoDeMetasDetalhado planoDetalhadoRef: planoRef.getPlanosDeMetasDetalhados()) {
			PlanoDeMetasDetalhadoDTO planoDetalhado = new PlanoDeMetasDetalhadoDTO(planoDetalhadoRef);
			Indicador indicador = planoDetalhadoRef.getIndicador();
			List <IndicadorPreenchido> indicadoresPreenchidos = indicadorPreenchidoService.findByIndicadorAnoPrefeitura(indicador.getId(), planoDeMetasDTO.getPrimeiroAnoMandato(), planoDeMetasDTO.getQuartoAnoMandato(), planoDeMetasDTO.getIdPrefeitura());

			IndicadorPreenchido ultimoIndicadorPreenchido = null;
			for (IndicadorPreenchido indicadorPreenchido: indicadoresPreenchidos) {
				if (indicadorPreenchido.getAno().intValue() == planoDeMetasDTO.getPrimeiroAnoMandato().intValue()) {
					planoDetalhado.setValorPreenchidoPrimeiroAno(indicadorPreenchido.getResultado());
					planoDetalhado.setValorPreenchidoPrimeiroAnoApresentacao(indicadorPreenchido.getValorApresentacaoTabelas());
				}else if (indicadorPreenchido.getAno().intValue() == planoDeMetasDTO.getSegundoAnoMandato().intValue()) {
					planoDetalhado.setValorPreenchidoSegundoAno(indicadorPreenchido.getResultado());
					planoDetalhado.setValorPreenchidoSegundoAnoApresentacao(indicadorPreenchido.getValorApresentacaoTabelas());
				}else if (indicadorPreenchido.getAno().intValue() == planoDeMetasDTO.getTerceiroAnoMandato().intValue()) {
					planoDetalhado.setValorPreenchidoTerceiroAno(indicadorPreenchido.getResultado());
					planoDetalhado.setValorPreenchidoTerceiroAnoApresentacao(indicadorPreenchido.getValorApresentacaoTabelas());
				}else if (indicadorPreenchido.getAno().intValue() == planoDeMetasDTO.getQuartoAnoMandato().intValue()) {
					planoDetalhado.setValorPreenchidoQuartoAno(indicadorPreenchido.getResultado());
					planoDetalhado.setValorPreenchidoQuartoAnoApresentacao(indicadorPreenchido.getValorApresentacaoTabelas());
				}
				
				if (ultimoIndicadorPreenchido == null || ultimoIndicadorPreenchido.getAno() < indicadorPreenchido.getAno()) {
					ultimoIndicadorPreenchido = indicadorPreenchido;
					planoDetalhado.setUltimoValorIndicador(indicadorPreenchido.getValorApresentacaoTabelas());
				}
			}

			/* Calcula variações anuais */
			planoDetalhado.setVariacaoSegundoAno(calculaVariacao(planoDetalhado.getValorPreenchidoPrimeiroAno(), planoDetalhado.getValorPreenchidoSegundoAno()));
			planoDetalhado.setVariacaoTerceiroAno(calculaVariacao(planoDetalhado.getValorPreenchidoSegundoAno(), planoDetalhado.getValorPreenchidoTerceiroAno()));
			planoDetalhado.setVariacaoQuartoAno(calculaVariacao(planoDetalhado.getValorPreenchidoTerceiroAno(), planoDetalhado.getValorPreenchidoQuartoAno()));

			/* Carrega informações adicionais */			
			planoDetalhado.setDescricaoIndicador(indicador.getDescricao());
			
			planoDeMetasDTO.getPlanosDeMetasDetalhados().add(planoDetalhado);
		}
		return planoDeMetasDTO;
	}
	
	public PlanoDeMetasDTO buscarCarregarPlanoDeMetasPorIdLazyLoad(Long idPlanoDeMetas) {
		PlanoDeMetas planoRef = buscarPorId(idPlanoDeMetas);
		PlanoDeMetasDTO planoDeMetasDTO = new PlanoDeMetasDTO(planoRef);
		planoDeMetasDTO.setUsuario(null);
		return planoDeMetasDTO;
	}
	
	public PlanoDeMetasDetalhadoDTO buscarCarregarPlanoDeMetasDetalhadoPorIndicador(Long idPlanoDeMetas, Long idIndicador) {
		PlanoDeMetas planoDeMetasRef = buscarPorId(idPlanoDeMetas);

		/* Calcula Anos do mandato */
		Short primeiroAno = (short) planoDeMetasRef.getPrefeitura().getInicioMandato().getYear();
		Short segundoAno = (short) (primeiroAno + 1);
		Short terceiroAno = (short) (segundoAno + 1);
		Short quartoAno = (short) (terceiroAno + 1);

		Indicador indicador = indicadorService.listarById(idIndicador);
		
		/*Busca plano de metas detlahado de acordo com plano de metas e indicador*/
		List<PlanoDeMetasDetalhado> planoDeMetasDetalhadoRef = planoDeMetasDetalhadoRepository.findByIndicadorAndPlanoDeMetas(idPlanoDeMetas, idIndicador);
		if (planoDeMetasDetalhadoRef == null || planoDeMetasDetalhadoRef.isEmpty()) {
			throw new ObjectNotFoundException("Plano de metas não encontrado para este indicador!");
		}
		
		PlanoDeMetasDetalhadoDTO planoDetalhado = new PlanoDeMetasDetalhadoDTO(planoDeMetasDetalhadoRef.get(0));
		
		/*Carrega valores do indicador preenchidos pela prefeitura */
		List <IndicadorPreenchido> indicadoresPreenchidos = indicadorPreenchidoService.findByIndicadorAnoPrefeitura(indicador.getId(), primeiroAno, quartoAno, planoDeMetasRef.getPrefeitura().getId());
		IndicadorPreenchido ultimoIndicadorPreenchido = null;
		for (IndicadorPreenchido indicadorPreenchido: indicadoresPreenchidos) {

			if (indicadorPreenchido.getAno().intValue() == primeiroAno.intValue()) {
				planoDetalhado.setValorPreenchidoPrimeiroAno(indicadorPreenchido.getResultado());
			}else if (indicadorPreenchido.getAno().intValue() == segundoAno.intValue()) {
				planoDetalhado.setValorPreenchidoSegundoAno(indicadorPreenchido.getResultado());
			}else if (indicadorPreenchido.getAno().intValue() == terceiroAno.intValue()) {
				planoDetalhado.setValorPreenchidoTerceiroAno(indicadorPreenchido.getResultado());
			}else if (indicadorPreenchido.getAno().intValue() == quartoAno.intValue()) {
				planoDetalhado.setValorPreenchidoQuartoAno(indicadorPreenchido.getResultado());
			}

			if (ultimoIndicadorPreenchido == null || ultimoIndicadorPreenchido.getAno() < indicadorPreenchido.getAno()) {
				ultimoIndicadorPreenchido = indicadorPreenchido;
				planoDetalhado.setUltimoValorIndicador(indicadorPreenchido.getResultado());
			}
		}

		/* Calcula variações anuais */
		planoDetalhado.setVariacaoSegundoAno(calculaVariacao(planoDetalhado.getValorPreenchidoPrimeiroAno(), planoDetalhado.getValorPreenchidoSegundoAno()));
		planoDetalhado.setVariacaoTerceiroAno(calculaVariacao(planoDetalhado.getValorPreenchidoSegundoAno(), planoDetalhado.getValorPreenchidoTerceiroAno()));
		planoDetalhado.setVariacaoQuartoAno(calculaVariacao(planoDetalhado.getValorPreenchidoTerceiroAno(), planoDetalhado.getValorPreenchidoQuartoAno()));

		/* Carrega informações adicionais */
		planoDetalhado.setStatusUltimoValor("Status do último valor preenchido"); // substituir por ultimoIndicadorPreenchido.getStatus() ou ultimoIndicadorPreenchido.getCor()

		return planoDetalhado;
	}
	
	public PlanoDeMetas inserirPlanoDeMetas(PlanoDeMetasDTO planoDeMetasDTO) {
		
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmail(user);

		Prefeitura prefeitura = prefeituraService.buscarPorId(planoDeMetasDTO.getIdPrefeitura());
		
		List<PlanoDeMetas> planoDeMetasDaPrefeitura = repository.findByPrefeitura(prefeitura);
		if (planoDeMetasDaPrefeitura != null && !planoDeMetasDaPrefeitura.isEmpty()) {
			throw new DataIntegrityException("A prefeitura de "+ prefeitura.getCidade().getNome()+" já possui um plano de metas para o mandato de "+prefeitura.getInicioMandato()+" a "+prefeitura.getFimMandato());
		}

		PlanoDeMetas planoDeMetas = PlanoDeMetas.builder()
				.statusPlanoDeMetas(planoDeMetasDTO.getStatusPlanoDeMetas())
				.prefeitura(prefeitura)
				.planosDeMetasDetalhados(new ArrayList<>())
				.usuario(usuario)
				.dataCriacao(new Date())
				.build();
		
		alertaService.salvar(Alerta.builder()
				.mensagem("A prefeitura de " + prefeitura.getCidade().getNome() + " cadastrou um Plano de Metas.")
					.link("/painel-cidade/detalhes/" + prefeitura.getCidade().getId()) 
					.tipoAlerta(TipoAlerta.CADASTRO_PLANO_DE_METAS)
					.data(LocalDateTime.now())
					.build());

		
		planoDeMetas = repository.save(planoDeMetas);
				
		relatorioPlanoDeMetasService.inserirRelatorioPlanoDeMetas(usuario.getNome(), prefeitura.getCidade().getNome(), prefeitura.getCidade().getProvinciaEstado().getNome(), planoDeMetas.getId(), prefeitura.getCidade());
		
		return planoDeMetas;
	}
	
	public PlanoDeMetasDetalhado inserirPlanoDeMetasDetalhado(PlanoDeMetasDetalhadoDTO planoDeMetasDetalhadoDTO, Long idPlanoDeMetas, Indicador indicadorRef, PlanoDeMetas planoDeMetasRef) {
		
		PlanoDeMetasDetalhado planoDetalhado = PlanoDeMetasDetalhado.builder()
				.indicador(indicadorRef)
				.metaAnualPrimeiroAno(planoDeMetasDetalhadoDTO.getMetaAnualPrimeiroAno())
				.metaAnualSegundoAno(planoDeMetasDetalhadoDTO.getMetaAnualSegundoAno())
				.metaAnualTerceiroAno(planoDeMetasDetalhadoDTO.getMetaAnualTerceiroAno())
				.metaAnualQuartoAno(planoDeMetasDetalhadoDTO.getMetaAnualQuartoAno())
				.orcamentoPrevisto(planoDeMetasDetalhadoDTO.getOrcamentoPrevisto())
				.orcamentoExecutado(planoDeMetasDetalhadoDTO.getOrcamentoExecutado())
				.planoDeMetas(planoDeMetasRef)
				.build();
		
		return planoDeMetasDetalhadoRepository.save(planoDetalhado);
	}
	
	public void deletarPlanoDeMetas(Long id) {
		buscarPorId(id);
		try { 
			repository.deleteById(id);
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não foi possível excluir. O registro está relacionado com outra entidade");
		}			
	}
	
	public void deletarPlanoDeMetasDetalhado(Long id) {
		buscarPlanoDetalhadoPorId(id);
		try { 
			planoDeMetasDetalhadoRepository.deleteById(id);
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não foi possível excluir. O registro está relacionado com outra entidade");
		}			
	}
	
	public void editarPlanoDeMetas(PlanoDeMetasDTO planoDeMetasDTO, Long id) {
		PlanoDeMetas planoDeMetasRef = buscarPorId(id);
		planoDeMetasRef.setStatusPlanoDeMetas(planoDeMetasDTO.getStatusPlanoDeMetas());
		planoDeMetasRef.setApresentacao(planoDeMetasDTO.getApresentacao());
		planoDeMetasRef.setDescricao(planoDeMetasDTO.getDescricao());
		planoDeMetasRef.setArquivo(planoDeMetasDTO.getArquivo() == null ? null : planoDeMetasDTO.getArquivo().toEntityInsert());
		
		
		repository.save(planoDeMetasRef);
		
		try {
			this.enviarEmailDeNotificacaoDePlanoDeMetas(planoDeMetasDTO);
		} catch (Exception e) {
			
		}
	}
	
	public void editarPlanoDeMetasDetalhado(PlanoDeMetasDetalhadoDTO planoDeMetasDetalhadoDTO, Long id) throws Exception {
		PlanoDeMetasDetalhado planoDeMetasDetalhadoRef= buscarPlanoDetalhadoPorId(id);
		PlanoDeMetasDetalhado planoDeMetasDetalhadoRefAntigo = planoDeMetasDetalhadoRef.clone();
		planoDeMetasDetalhadoRef.setPlanoParaAlcancarProposta(planoDeMetasDetalhadoDTO.getPlanoParaAlcancarProposta());
		planoDeMetasDetalhadoRef.setMetaAnualPrimeiroAno(planoDeMetasDetalhadoDTO.getMetaAnualPrimeiroAno());
		planoDeMetasDetalhadoRef.setMetaAnualSegundoAno(planoDeMetasDetalhadoDTO.getMetaAnualSegundoAno());
		planoDeMetasDetalhadoRef.setMetaAnualTerceiroAno(planoDeMetasDetalhadoDTO.getMetaAnualTerceiroAno());
		planoDeMetasDetalhadoRef.setMetaAnualQuartoAno(planoDeMetasDetalhadoDTO.getMetaAnualQuartoAno());
		planoDeMetasDetalhadoRef.setOrcamentoPrevisto(planoDeMetasDetalhadoDTO.getOrcamentoPrevisto());
		planoDeMetasDetalhadoRef.setOrcamentoExecutado(planoDeMetasDetalhadoDTO.getOrcamentoExecutado());
		planoDeMetasDetalhadoRepository.save(planoDeMetasDetalhadoRef);
		gerarHistorico(planoDeMetasDetalhadoRefAntigo, planoDeMetasDetalhadoDTO.getJustificativa());
	}
	
	public void enviarEmailDeNotificacaoDePlanoDeMetas(PlanoDeMetasDTO planoDeMetasDTO) throws EmailException {
		List<AjusteGeralDTO> listaAjusteGeralDto = ajusteGeralService.buscarListaAjustes(EMAIL_PLANO_METAS);
		Prefeitura prefeitura = prefeituraService.buscarPorId(planoDeMetasDTO.getIdPrefeitura());
		
		List<String> listaDestinatarios = new ArrayList<>();
		
		for(AjusteGeralDTO ajuste : listaAjusteGeralDto) {
			listaDestinatarios.add(ajuste.getConteudo());
		}
		
		String mensagem = "A prefeitura de " + prefeitura.getCidade().getNome() + " editou/cadastrou seu Plano De Metas.";
		
		emailUtil.enviarEmailHTML(listaDestinatarios, "Notificação de Cadastro de Plano de Metas", mensagem);
	}
	
	public void gerarHistorico(PlanoDeMetasDetalhado planoDeMetasDetalhadoRef, String justificativa) throws Exception {
		PlanoDeMetasDetalhadoHistorico historico = new PlanoDeMetasDetalhadoHistorico();
		historico.setUsuario(usuarioContextUtil.getUsuario());
		historico.setDataHoraAlterado(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		historico.setJustificativa(justificativa);
		historico.setPlanoDeMetasDetalhado(planoDeMetasDetalhadoRef);
		historico.setIndicador(planoDeMetasDetalhadoRef.getIndicador() != null ? planoDeMetasDetalhadoRef.getIndicador() : null);
		historico.setPlanoParaAlcancarProposta(planoDeMetasDetalhadoRef.getPlanoParaAlcancarProposta());
		historico.setMetaAnualPrimeiroAno(planoDeMetasDetalhadoRef.getMetaAnualPrimeiroAno());
		historico.setMetaAnualQuartoAno(planoDeMetasDetalhadoRef.getMetaAnualQuartoAno());
		historico.setMetaAnualSegundoAno(planoDeMetasDetalhadoRef.getMetaAnualSegundoAno());
		historico.setMetaAnualTerceiroAno(planoDeMetasDetalhadoRef.getMetaAnualTerceiroAno());
		historico.setOrcamentoExecutado(planoDeMetasDetalhadoRef.getOrcamentoExecutado());
		historico.setOrcamentoPrevisto(planoDeMetasDetalhadoRef.getOrcamentoPrevisto());
		historico.setPlanoDeMetas(planoDeMetasDetalhadoRef.getPlanoDeMetas() != null ? planoDeMetasDetalhadoRef.getPlanoDeMetas() : null);
		
		if(planoDeMetasDetalhadoRef.getOrcamentoExecutado() != null && planoDeMetasDetalhadoRef.getOrcamentoPrevisto() != null 
				|| planoDeMetasDetalhadoRef.getMetaAnualPrimeiroAno() != null || planoDeMetasDetalhadoRef.getMetaAnualSegundoAno() != null
				|| planoDeMetasDetalhadoRef.getMetaAnualTerceiroAno() != null || planoDeMetasDetalhadoRef.getMetaAnualQuartoAno() != null)
		{
			planoDeMetasDetalhadoHistoricoRepository.save(historico);
		}
	}
	
	public PlanoDeMetasDetalhadoDTO criarPlanoDeMetasDetalhadoParaIndicador(Long idPlanoDeMetas, Long idIndicador) {
		
		PlanoDeMetas planoDeMetasRef = buscarPorId(idPlanoDeMetas);
		
		Long idPrefeitura = planoDeMetasRef.getPrefeitura().getId();
//		String user = SecurityContextHolder.getContext().getAuthentication().getName();
//		Usuario usuario  = usuarioService.buscarPorEmail(user);
//		Long idPrefeitura = usuario.getPrefeitura().getId();
		
		PlanoDeMetasDetalhadoDTO newPlanoDetalhado = new PlanoDeMetasDetalhadoDTO();
		
		/* Calcula Anos do mandato */
		Short primeiroAno = (short) planoDeMetasRef.getPrefeitura().getInicioMandato().getYear();
		Short segundoAno = (short) (primeiroAno+1);
		Short terceiroAno = (short) (segundoAno+1);
		Short quartoAno = (short) (terceiroAno+1);

		Indicador indicador = indicadorService.listarById(idIndicador);
		
		/*Carrega valores do indicador preenchidos pela prefeitura */
		List <IndicadorPreenchido> indicadoresPreenchidos = indicadorPreenchidoService.findByIndicadorAnoPrefeitura(indicador.getId(), primeiroAno, quartoAno, idPrefeitura);
		IndicadorPreenchido ultimoIndicadorPreenchido = null;
		for (IndicadorPreenchido indicadorPreenchido: indicadoresPreenchidos) {

			if (indicadorPreenchido.getAno().intValue() == primeiroAno.intValue()) {
				newPlanoDetalhado.setValorPreenchidoPrimeiroAno(indicadorPreenchido.getResultado());
				newPlanoDetalhado.setValorPreenchidoPrimeiroAnoApresentacao(indicadorPreenchido.getResultadoApresentacao());
			}else if (indicadorPreenchido.getAno().intValue() == segundoAno.intValue()) {
				newPlanoDetalhado.setValorPreenchidoSegundoAno(indicadorPreenchido.getResultado());
				newPlanoDetalhado.setValorPreenchidoSegundoAnoApresentacao(indicadorPreenchido.getResultadoApresentacao());
			}else if (indicadorPreenchido.getAno().intValue() == terceiroAno.intValue()) {
				newPlanoDetalhado.setValorPreenchidoTerceiroAno(indicadorPreenchido.getResultado());
				newPlanoDetalhado.setValorPreenchidoTerceiroAnoApresentacao(indicadorPreenchido.getResultadoApresentacao());
			}else if (indicadorPreenchido.getAno().intValue() == quartoAno.intValue()) {
				newPlanoDetalhado.setValorPreenchidoQuartoAno(indicadorPreenchido.getResultado());
				newPlanoDetalhado.setValorPreenchidoQuartoAnoApresentacao(indicadorPreenchido.getResultadoApresentacao());
			}

			if (ultimoIndicadorPreenchido == null || ultimoIndicadorPreenchido.getAno() < indicadorPreenchido.getAno()) {
				ultimoIndicadorPreenchido = indicadorPreenchido;
			}
		}

		/* Calcula variações anuais */
		newPlanoDetalhado.setVariacaoSegundoAno(calculaVariacao(newPlanoDetalhado.getValorPreenchidoPrimeiroAno(), newPlanoDetalhado.getValorPreenchidoSegundoAno()));
		newPlanoDetalhado.setVariacaoTerceiroAno(calculaVariacao(newPlanoDetalhado.getValorPreenchidoSegundoAno(), newPlanoDetalhado.getValorPreenchidoTerceiroAno()));
		newPlanoDetalhado.setVariacaoQuartoAno(calculaVariacao(newPlanoDetalhado.getValorPreenchidoTerceiroAno(), newPlanoDetalhado.getValorPreenchidoQuartoAno()));

		/* Carrega informações adicionais */
		newPlanoDetalhado.setIdIndicador(idIndicador);
		newPlanoDetalhado.setNomeIndicador(indicador.getNome());
		newPlanoDetalhado.setOds(indicador.getOds() != null ? indicador.getOds().getDescricao() : "");
		newPlanoDetalhado.setMetaOds(indicador.getMetaODS() != null ? indicador.getMetaODS().getDescricao() : "");
		newPlanoDetalhado.setDescricaoIndicador(indicador.getDescricao());
		
		if (ultimoIndicadorPreenchido != null) {
			newPlanoDetalhado.setUltimoValorIndicador(ultimoIndicadorPreenchido.getResultado());
			newPlanoDetalhado.setUltimoValorIndicadorApresentacao(ultimoIndicadorPreenchido.getResultadoApresentacao());
		}
		
		// newPlanoDetalhado.setStatusUltimoValor(ultimoIndicadorPreenchido.getReferenciaDescritiva()); // substituir por ultimoIndicadorPreenchido.getStatus() ou ultimoIndicadorPreenchido.getCor()
		Double valorResultado = 0.0;
		if(ultimoIndicadorPreenchido != null) {
			if (ultimoIndicadorPreenchido != null && ultimoIndicadorPreenchido.getResultadoReferencia() == null && ultimoIndicadorPreenchido.getResultado() != null && !ultimoIndicadorPreenchido.getIndicador().isMultiplo()) {
				valorResultado = Double.parseDouble(ultimoIndicadorPreenchido.getResultado());
			}else if (ultimoIndicadorPreenchido.getResultadoReferencia() == null && ultimoIndicadorPreenchido.getResultado() == null || ultimoIndicadorPreenchido.getIndicador().isMultiplo()) {
				valorResultado = Double.parseDouble("0");
			}else {
				valorResultado = ultimoIndicadorPreenchido.getResultadoReferencia();
			}
		}
		Optional<ValorReferencia> resultadoIndicador;
		Prefeitura prefeitura = planoDeMetasRef.getPrefeitura();
		resultadoIndicador = preenchidoRepository.findValorReferenciaByIndicadorPreenchido(prefeitura, ultimoIndicadorPreenchido, valorResultado);
		ValorReferencia referenciaIndicador = resultadoIndicador.isPresent() ? resultadoIndicador.get() : VALOR_REF_NAO_SE_APLICA_INDICADOR;

		newPlanoDetalhado.setStatusUltimoValor(referenciaIndicador.getLabel());
		newPlanoDetalhado.setCorUltimoValor(referenciaIndicador.getCor());

		
		/* Salva o plano de metas detalhado */
		PlanoDeMetasDetalhado newPlanoDetalhadoRef = inserirPlanoDeMetasDetalhado(newPlanoDetalhado, idPlanoDeMetas, indicador, planoDeMetasRef);
		newPlanoDetalhado.setId(newPlanoDetalhadoRef.getId());
		
		return newPlanoDetalhado;
	}
	
	public File gerarArquivo(long id) throws IOException {
		PlanoDeMetasDTO planoDeMetas = buscarCarregarPlanoDeMetasPorId(id);
		List<PlanoDeMetasDetalhadoDTO> listDetalhados = planoDeMetas.getPlanosDeMetasDetalhados();

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet abaPlanilha = workbook.createSheet("Variáveis");
		abaPlanilha = gerarCabecalho(abaPlanilha, planoDeMetas);
		int rownum = 0;
		for (PlanoDeMetasDetalhadoDTO item : listDetalhados) {
			Row row = abaPlanilha.createRow(++rownum);
			int indexTabela = 0;
			
			Cell cellUsuario = row.createCell(indexTabela++);
			cellUsuario.setCellValue(planoDeMetas.getUsuario().getNome());

			Cell cellODS = row.createCell(indexTabela++);
			cellODS.setCellValue(item.getOds());

			Cell cellMeta = row.createCell(indexTabela++);
			cellMeta.setCellValue(item.getMetaOds());

			List<Short> anos = NumeroUtil.getMandatos(planoDeMetas.getInicioMandato(), planoDeMetas.getFimMandato());
			
			int qtdAnos = anos.size();
			
			if(qtdAnos >0) {
				Cell cellValorAno= row.createCell(indexTabela++);
				cellValorAno.setCellValue(item.getValorPreenchidoPrimeiroAnoApresentacao() != null ? item.getValorPreenchidoPrimeiroAnoApresentacao() :"");
			}
			if(qtdAnos >1) {
				Cell cellValorAno= row.createCell(indexTabela++);
				cellValorAno.setCellValue(item.getValorPreenchidoSegundoAnoApresentacao() != null ? item.getValorPreenchidoSegundoAnoApresentacao() :"");
			}
			if(qtdAnos >2) {
				Cell cellValorAno= row.createCell(indexTabela++);
				cellValorAno.setCellValue(item.getValorPreenchidoTerceiroAnoApresentacao() != null ? item.getValorPreenchidoTerceiroAnoApresentacao() :"");
			}
			if(qtdAnos >3) {
				Cell cellValorAno= row.createCell(indexTabela++);
				cellValorAno.setCellValue(item.getValorPreenchidoQuartoAnoApresentacao() != null ? item.getValorPreenchidoQuartoAnoApresentacao() :"");
			}

			if(qtdAnos >0) {
				Cell cellMetaAno= row.createCell(indexTabela++);
				cellMetaAno.setCellValue(item.getMetaAnualPrimeiroAno() != null ? NumeroUtil.decimalToString(Double.parseDouble(item.getMetaAnualPrimeiroAno())) :"");
			}
			if(qtdAnos >1) {
				Cell cellMetaAno= row.createCell(indexTabela++);
				cellMetaAno.setCellValue(item.getMetaAnualSegundoAno() != null ? NumeroUtil.decimalToString(Double.parseDouble(item.getMetaAnualSegundoAno())) :"");
			}
			if(qtdAnos >2) {
				Cell cellMetaAno= row.createCell(indexTabela++);
				cellMetaAno.setCellValue(item.getMetaAnualTerceiroAno() != null ? NumeroUtil.decimalToString(Double.parseDouble(item.getMetaAnualTerceiroAno())) :"");
			}
			if(qtdAnos >3) {
				Cell cellMetaAno= row.createCell(indexTabela++);
				cellMetaAno.setCellValue(item.getMetaAnualQuartoAno() != null ? NumeroUtil.decimalToString(Double.parseDouble(item.getMetaAnualQuartoAno())) :"");
			}

			Cell cellMetaFinal= row.createCell(indexTabela++);
			cellMetaFinal.setCellValue(item.getMetaFinal() != null ? item.getMetaFinal() :"");

			if(qtdAnos >0) {
				Cell cellVariacaoAno= row.createCell(indexTabela++);
				cellVariacaoAno.setCellValue(item.getVariacaoPrimeiroAno() != null ? NumeroUtil.decimalToString(Double.parseDouble(item.getVariacaoPrimeiroAno().toString())) : "");
			}
			if(qtdAnos >1) {
				Cell cellVariacaoAno= row.createCell(indexTabela++);
				cellVariacaoAno.setCellValue(item.getVariacaoSegundoAno() != null ? NumeroUtil.decimalToString(Double.parseDouble(item.getVariacaoSegundoAno().toString())) : "");
			}
			if(qtdAnos >2) {
				Cell cellVariacaoAno= row.createCell(indexTabela++);
				cellVariacaoAno.setCellValue(item.getVariacaoTerceiroAno() != null ? NumeroUtil.decimalToString(Double.parseDouble(item.getVariacaoTerceiroAno().toString())): "");
			}
			if(qtdAnos >3) {
				Cell cellVariacaoAno= row.createCell(indexTabela++);
				cellVariacaoAno.setCellValue(item.getVariacaoQuartoAno() != null ? NumeroUtil.decimalToString(Double.parseDouble(item.getVariacaoQuartoAno().toString())): "");
			}

			Cell cellOrcamentoPrevisto= row.createCell(indexTabela++);
			cellOrcamentoPrevisto.setCellValue((item.getOrcamentoPrevisto() != null ? "R$ "+NumeroUtil.decimalToString(item.getOrcamentoPrevisto()): ""));

			Cell cellOrcamentoExecutado= row.createCell(indexTabela++);
			cellOrcamentoExecutado.setCellValue((item.getOrcamentoExecutado() != null ? "R$ "+ NumeroUtil.decimalToString(item.getOrcamentoExecutado()): ""));

			
		}
		for (int i = 0 ; i<=20 ;i++) {
			abaPlanilha.autoSizeColumn(i);
		}
		
		File arquivo = new File("arquivo_plano_metas_"+LocalDateTime.now().getNano()+".xlsx");
		FileOutputStream out = 
				new FileOutputStream(arquivo);
		workbook.write(out);
		workbook.close();
		out.close();

		return arquivo;
	}


	private XSSFSheet gerarCabecalho(XSSFSheet aba, PlanoDeMetasDTO dto) {
		Row row = aba.createRow(0);
		XSSFCellStyle style = aba.getWorkbook().createCellStyle();
		XSSFFont font = aba.getWorkbook().createFont();
		font.setBold(true);
		style.setFont(font);
		style.setLocked(true);

		int indexTabela = 0;
		Cell cellUsuario = row.createCell(indexTabela++);
		cellUsuario.setCellStyle(style);
		cellUsuario.setCellValue("Usuário");

		Cell cellODS = row.createCell(indexTabela++);
		cellODS.setCellStyle(style);
		cellODS.setCellValue("ODS");

		Cell cellMeta = row.createCell(indexTabela++);
		cellMeta.setCellStyle(style);
		cellMeta.setCellValue("Meta ODS");

		List<Short> anos = NumeroUtil.getMandatos(dto.getInicioMandato(), dto.getFimMandato());
		for(Short ano :anos) {
			
			Cell cellValorAno= row.createCell(indexTabela++);
			cellValorAno.setCellStyle(style);
			cellValorAno.setCellValue("Valor preenchido em "+ano);
		}
		
		Short anoFinal=0;
		for(Short ano :anos) {
			Cell cellMetaAno= row.createCell(indexTabela++);
			cellMetaAno.setCellStyle(style);
			cellMetaAno.setCellValue("Meta atual para "+ano);
			anoFinal=ano;
		}

		Cell cellMetaFinal= row.createCell(indexTabela++);
		cellMetaFinal.setCellStyle(style);
		cellMetaFinal.setCellValue("Meta final até"+anoFinal);
		
		int anoVariacao = 1;
		for(Short ano :anos) {
			Cell cellVariacaoAno= row.createCell(indexTabela++);
			cellVariacaoAno.setCellStyle(style);
			cellVariacaoAno.setCellValue("Variação do "+anoVariacao+"º ano(%)");
			anoVariacao++;
		}
		

		Cell cellOrcamentoPrevisto = row.createCell(indexTabela++);
		cellOrcamentoPrevisto.setCellStyle(style);
		cellOrcamentoPrevisto.setCellValue("Orçamento Previsto");

		Cell cellOrcamentoExecutado = row.createCell(indexTabela++);
		cellOrcamentoExecutado.setCellStyle(style);
		cellOrcamentoExecutado.setCellValue("Orçamento Previsto");
		
		return aba;
	}

		
	private Float calculaVariacao(String valor1, String valor2) {
		try {
			float ano1 = Float.valueOf(valor1);
			float ano2 = Float.valueOf(valor2);
			float diferenca = ano2 - ano1;
			return (diferenca*100)/ano1;
		} catch (Exception e) {
			return null;
		}
	}
	
	public PlanoDeMetasDTO buscarPlanoDeMetasPorPrefeitura(Long idPrefeitura) {
		PlanoDeMetas planoRef = repository.buscarPorIdPrefeitura(idPrefeitura);
		if (planoRef != null) {
			return buscarPlanoDeMetasPorPrefeituraAux(planoRef);
		}else {
			return criarPlanoDeMetas(idPrefeitura);
		}
	}
	
	public PlanoDeMetasDTO buscarPlanoDeMetasPorCidade(Long idCidade) {
		
		Prefeitura prefeitura = prefeituraService.buscarPorIdCidadeUltimaPrefeitura(idCidade);

		PlanoDeMetas planoRef = repository.buscarPorIdPrefeitura(prefeitura.getId());
		if (planoRef != null) {
			return buscarPlanoDeMetasPorPrefeituraAux(planoRef);
		}else {
			return null;
		}
	}
	
	
	public PlanoDeMetasDTO buscarPlanoDeMetasPorPrefeituraAux(PlanoDeMetas planoRef) {		
		
		PlanoDeMetasDTO planoDeMetasDTO = PlanoDeMetasDTO.builder()
				.id(planoRef.getId())
				.statusPlanoDeMetas(planoRef.getStatusPlanoDeMetas())
				.nomeCidade(planoRef.getPrefeitura().getCidade().getNome())
				.populacao(planoRef.getPrefeitura().getCidade().getPopulacao())
				.idPrefeitura(planoRef.getPrefeitura().getId())
				.nomePrefeito(planoRef.getPrefeitura().getNome())
				.inicioMandato(planoRef.getPrefeitura().getInicioMandato())
				.fimMandato(planoRef.getPrefeitura().getFimMandato())
				.planosDeMetasDetalhados(new ArrayList<>())
				.usuario(null)
				.apresentacao(planoRef.getApresentacao())
				.descricao(planoRef.getDescricao())
				.arquivo(planoRef.getArquivo() != null ? new ArquivoDTO(planoRef.getArquivo()) : null)
				.siglaEstado(planoRef.getPrefeitura().getCidade().getProvinciaEstado().getSigla())
				.build();

		
		/* Calcula Anos do mandato */
		planoDeMetasDTO.setPrimeiroAnoMandato((short) planoDeMetasDTO.getInicioMandato().getYear());
		planoDeMetasDTO.setSegundoAnoMandato((short) (planoDeMetasDTO.getPrimeiroAnoMandato()+1));
		planoDeMetasDTO.setTerceiroAnoMandato((short) (planoDeMetasDTO.getSegundoAnoMandato()+1));
		planoDeMetasDTO.setQuartoAnoMandato((short) (planoDeMetasDTO.getTerceiroAnoMandato()+1));

		/*Carrega valores preenchidos de cada indicador*/
		for (PlanoDeMetasDetalhado planoDetalhadoRef: planoRef.getPlanosDeMetasDetalhados()) {
			PlanoDeMetasDetalhadoDTO planoDetalhado = new PlanoDeMetasDetalhadoDTO(planoDetalhadoRef);
			Indicador indicador = planoDetalhadoRef.getIndicador();
			List <IndicadorPreenchido> indicadoresPreenchidos = indicadorPreenchidoService.findByIndicadorAnoPrefeitura(indicador.getId(), planoDeMetasDTO.getPrimeiroAnoMandato(), planoDeMetasDTO.getQuartoAnoMandato(), planoDeMetasDTO.getIdPrefeitura());

			IndicadorPreenchido ultimoIndicadorPreenchido = null;
			for (IndicadorPreenchido indicadorPreenchido: indicadoresPreenchidos) {
				if (indicadorPreenchido.getAno().intValue() == planoDeMetasDTO.getPrimeiroAnoMandato().intValue()) {
					planoDetalhado.setValorPreenchidoPrimeiroAno(indicadorPreenchido.getResultado());
					planoDetalhado.setValorPreenchidoPrimeiroAnoApresentacao(indicadorPreenchido.getResultadoApresentacao());
				}else if (indicadorPreenchido.getAno().intValue() == planoDeMetasDTO.getSegundoAnoMandato().intValue()) {
					planoDetalhado.setValorPreenchidoSegundoAno(indicadorPreenchido.getResultado());
					planoDetalhado.setValorPreenchidoSegundoAnoApresentacao(indicadorPreenchido.getResultadoApresentacao());
				}else if (indicadorPreenchido.getAno().intValue() == planoDeMetasDTO.getTerceiroAnoMandato().intValue()) {
					planoDetalhado.setValorPreenchidoTerceiroAno(indicadorPreenchido.getResultado());
					planoDetalhado.setValorPreenchidoTerceiroAnoApresentacao(indicadorPreenchido.getResultadoApresentacao());
				}else if (indicadorPreenchido.getAno().intValue() == planoDeMetasDTO.getQuartoAnoMandato().intValue()) {
					planoDetalhado.setValorPreenchidoQuartoAno(indicadorPreenchido.getResultado());
					planoDetalhado.setValorPreenchidoQuartoAnoApresentacao(indicadorPreenchido.getResultadoApresentacao());
				}
				
				if (ultimoIndicadorPreenchido == null || ultimoIndicadorPreenchido.getAno() < indicadorPreenchido.getAno()) {
					ultimoIndicadorPreenchido = indicadorPreenchido;
					planoDetalhado.setUltimoValorIndicador(indicadorPreenchido.getResultado());
					planoDetalhado.setUltimoValorIndicadorApresentacao(indicadorPreenchido.getResultadoApresentacao());
				}
			}

			/* Calcula variações anuais */
			planoDetalhado.setVariacaoSegundoAno(calculaVariacao(planoDetalhado.getValorPreenchidoPrimeiroAno(), planoDetalhado.getValorPreenchidoSegundoAno()));
			planoDetalhado.setVariacaoTerceiroAno(calculaVariacao(planoDetalhado.getValorPreenchidoSegundoAno(), planoDetalhado.getValorPreenchidoTerceiroAno()));
			planoDetalhado.setVariacaoQuartoAno(calculaVariacao(planoDetalhado.getValorPreenchidoTerceiroAno(), planoDetalhado.getValorPreenchidoQuartoAno()));

			/* Carrega informações adicionais */			
			planoDetalhado.setDescricaoIndicador(indicador.getDescricao());
			
			Double valorResultado = 0.0;
			
			if(ultimoIndicadorPreenchido != null) {
				if (ultimoIndicadorPreenchido != null && ultimoIndicadorPreenchido.getResultadoReferencia() == null && ultimoIndicadorPreenchido.getResultado() != null && !ultimoIndicadorPreenchido.getIndicador().isMultiplo()) {
					valorResultado = Double.parseDouble(ultimoIndicadorPreenchido.getResultado());
				}else if (ultimoIndicadorPreenchido.getResultadoReferencia() == null && ultimoIndicadorPreenchido.getResultado() == null || ultimoIndicadorPreenchido.getIndicador().isMultiplo()) {
					valorResultado = Double.parseDouble("0");
				}else {
					valorResultado = ultimoIndicadorPreenchido.getResultadoReferencia();
				}
			}
			
			Optional<ValorReferencia> resultadoIndicador;
			Prefeitura prefeitura = planoRef.getPrefeitura();
			resultadoIndicador = preenchidoRepository.findValorReferenciaByIndicadorPreenchido(prefeitura, ultimoIndicadorPreenchido, valorResultado);
			ValorReferencia referenciaIndicador = resultadoIndicador.isPresent() ? resultadoIndicador.get() : VALOR_REF_NAO_SE_APLICA_INDICADOR;
	
			planoDetalhado.setStatusUltimoValor(referenciaIndicador.getLabel());
			planoDetalhado.setCorUltimoValor(referenciaIndicador.getCor());
			
			planoDeMetasDTO.getPlanosDeMetasDetalhados().add(planoDetalhado);
		}
		return planoDeMetasDTO;
	}
	
	public PlanoDeMetasDTO criarPlanoDeMetas(Long idPrefeitura) {
		
		Prefeitura prefeitura = prefeituraService.buscarPorId(idPrefeitura);
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmail(user);
		/* usuario.setPrefeitura(null); */
		if (usuario.getPrefeitura() == null) {
			throw new BusinessLogicErrorException("Usuário logado não está associado a nenhuma prefeitura portanto não pode criar plano de metas.");
		}
		
		/* Calcula Anos do mandato */
		Short primeiroAno = (short) prefeitura.getInicioMandato().getYear();
		Short segundoAno = (short) (primeiroAno+1);
		Short terceiroAno = (short) (segundoAno+1);
		Short quartoAno = (short) (terceiroAno+1);
		
		/* Carrega informações */
		PlanoDeMetasDTO newPlanoDeMetasDTO = PlanoDeMetasDTO.builder()
				.nomeCidade(prefeitura.getCidade().getNome())
				.nomePrefeito(prefeitura.getNome())
				.inicioMandato(prefeitura.getInicioMandato())
				.fimMandato(prefeitura.getFimMandato())
				.idPrefeitura(prefeitura.getId())
				.populacao(prefeitura.getCidade().getPopulacao())
				.primeiroAnoMandato(primeiroAno)
				.segundoAnoMandato(segundoAno)
				.terceiroAnoMandato(terceiroAno)
				.quartoAnoMandato(quartoAno)
				.statusPlanoDeMetas("Não iniciado")
				.planosDeMetasDetalhados(new ArrayList<>())
				.usuario(new UsuarioDTO(usuario))
				.build();
		
		/* Salva o plano de metas */
		PlanoDeMetas planoDeMetasRef = inserirPlanoDeMetas(newPlanoDeMetasDTO);
		newPlanoDeMetasDTO.setId(planoDeMetasRef.getId());
		
		return newPlanoDeMetasDTO;
	}

	public List<Long> buscarIdsPlanoDeMetasPreenchidos() {
		return planoDeMetasDetalhadoRepository.buscarIdsPlanoDeMetasPreenchidos();
	}
	
	public List<PrefeituraPlanoMetasDTO> listaPrefeituraPlanoDeMetas() {
		
		Usuario usuario = null;
		Long idCidade = null;
		Prefeitura prefeitura = null;
		
		try {
			usuario = usuarioContextUtil.getUsuario();
			prefeitura = prefeituraService.buscarPrefeituraPorIdUsuario(usuario.getId());
			if(prefeitura != null) {
				idCidade = prefeitura.getCidade().getId();
			}
		} catch (Exception e) {
			usuario = null;
			prefeitura = null;
			e.printStackTrace();
		}

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<PrefeituraPlanoMetasDTO> query = cb.createQuery(PrefeituraPlanoMetasDTO.class);

		Root<Prefeitura> prefeituraRoot = query.from(Prefeitura.class);
		
		Join<Prefeitura, Cidade> joinCidade = prefeituraRoot.join("cidade", JoinType.INNER);
		Join<Cidade, ProvinciaEstado> joinEstado = joinCidade.join("provinciaEstado", JoinType.INNER);
		
		query.multiselect(joinCidade.get("id"), prefeituraRoot.get("id"), joinCidade.get("nome"), joinEstado.get("nome"), 
				prefeituraRoot.get("nome"), prefeituraRoot.get("inicioMandato"), prefeituraRoot.get("fimMandato"));

		List<Predicate> predicateList = new ArrayList<>();
		
		predicateList.add(cb.equal(prefeituraRoot.get("signataria"), true));

		if (idCidade != null) {
			Path<Long> cidade = joinCidade.get("id");
			predicateList.add(cb.equal(cidade, idCidade));
		}

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		List<Order> orderList = new ArrayList();
		orderList.add(cb.asc(joinEstado.get("nome")));
		orderList.add(cb.asc(joinCidade.get("nome")));
		query.orderBy(orderList);

		TypedQuery<PrefeituraPlanoMetasDTO> typedQuery = em.createQuery(query);
		List<PrefeituraPlanoMetasDTO> lista = typedQuery.getResultList();
		if(prefeitura != null){
			lista.get(0).setIdPrefeituraLogada(prefeitura.getId());
		}
		return lista;
	}

}
